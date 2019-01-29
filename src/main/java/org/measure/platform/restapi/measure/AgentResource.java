package org.measure.platform.restapi.measure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.data.api.IMeasureInstanceService;
import org.measure.platform.core.data.api.IMeasurePropertyService;
import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.core.data.entity.MeasureProperty;
import org.measure.platform.restapi.measure.dto.MeasureAgent;
import org.measure.platform.service.agent.api.IAgentManager;
import org.measure.platform.service.agent.api.IRemoteCatalogueService;
import org.measure.platform.service.smmengine.api.IRemoteMeasureExecutionService;
import org.measure.platform.service.smmengine.api.ISchedulingService;
import org.measure.platform.service.smmengine.impl.measureexecution.RemoteExecutionCache;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.remote.RemoteMeasureExternal;
import org.measure.smm.remote.RemoteMeasureInstanceList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/remote-measure")
public class AgentResource {
	@Inject
	IRemoteCatalogueService remoteCatalogue;

	@Inject
	ISchedulingService schedulingService;

	@Inject
	IRemoteMeasureExecutionService remoteExecutionService;

	@Inject
	IMeasureInstanceService instanceService;
	
	@Inject
	IMeasurePropertyService propertyService;

	@Inject
	RemoteExecutionCache remoteExecutionCache;

	@Inject
	IAgentManager agentService;

	@PutMapping("/registration")
	@Timed
	public void registerMeasure(@Valid @RequestBody SMMMeasure measureDefinition) {
		try {
			this.remoteCatalogue.registerRemoteMeasure(measureDefinition, measureDefinition.getAgentId());
			this.agentService.registerLifeSign(measureDefinition.getAgentId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * GET /agents : get all the agents.
	 */
	@PostMapping("/execution-list")
	@Timed
	public @ResponseBody RemoteMeasureInstanceList getMeasureExecutions(@RequestParam("id") String agentId) {

		// Add Scheduling Request
		RemoteMeasureInstanceList list = new RemoteMeasureInstanceList();
		list.getRemoteInstances().addAll(schedulingService.getSheduledRemoteMeasure(agentId));

		// Add Single Execution Request
		list.getSingleExecutions().addAll(remoteExecutionCache.getPendingExecutione(agentId));

		return list;
	}
	
	@PutMapping("/measure-execution")
	@Timed
	public void registerMeasurement(@RequestBody MeasureLog executionResult) {
		remoteExecutionService.registerRemoteExecution(executionResult);
	}

	/**
	 * GET /agents : get all the agents.
	 */
	@GetMapping("/agent-list")
	@Timed
	public List<MeasureAgent> getAllAgents() {
		return agentService.getAgents();
	}

	@Timed
	@RequestMapping(value = "/externalexecution", method = RequestMethod.GET)
	public ResponseEntity executeMeasure(@RequestParam("id") String id, @RequestParam("date") String date, @RequestParam("dateField") String dateField) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);

			String format = "yyyy-MM-dd'T'HH:mm:ssz";
			SimpleDateFormat parser = new SimpleDateFormat(format);

			Date logDate = null;
			try {
				logDate = parser.parse(date);
			} catch (ParseException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			MeasureInstance minstance = instanceService.findOne(instanceId);

			if (minstance != null && logDate != null) {
				RemoteMeasureExternal externalMeasure = new RemoteMeasureExternal();
				externalMeasure.setAlternateDate(logDate);
				externalMeasure.setDateField(dateField);
				externalMeasure.setMeasureId(minstance.getId());
				externalMeasure.setInstanceName(minstance.getInstanceName());
				externalMeasure.setMeasureName(minstance.getMeasureName());
				externalMeasure.setMeasureVersion(minstance.getMeasureVersion());
				Map<String, String> properties = new HashMap<>();
				if (minstance.getProperties() != null) {
					for (MeasureProperty prop : propertyService.findByInstance(minstance)) {
						properties.put(prop.getPropertyName(), prop.getPropertyValue());
					}
				}
				externalMeasure.setProperties(properties);
				remoteExecutionCache.registerSingleMeasureExecution(externalMeasure);

				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			}

		}

		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

}
