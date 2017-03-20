package org.measure.platform.restapi.app.services;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.agent.api.IAgentManager;
import org.measure.platform.agent.api.IRemoteCatalogueService;
import org.measure.platform.restapi.app.services.dto.MeasureAgent;
import org.measure.platform.smmengine.api.IRemoteMeasureExecutionService;
import org.measure.platform.smmengine.api.IShedulingService;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.remote.RemoteMeasureInstanceList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/remote-measure")
public class RemoteMeasureResource {

	@Inject
	IRemoteCatalogueService remoteCatalogue;

	@Inject
	IShedulingService schedulingService;

	@Inject
	IRemoteMeasureExecutionService remoteExecutionService;
	
	@Inject 
	IAgentManager agentService;

	@PutMapping("/registration")
	@Timed
	public void registerMeasure(@Valid @RequestBody SMMMeasure measureDefinition) {
		try{
			this.remoteCatalogue.registerRemoteMeasure(measureDefinition);
			this.agentService.registerLifeSign(measureDefinition.getCallbackAdress());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * GET /agents : get all the agents.
	 */
	@PostMapping("/execution-list")
	@Timed
	public @ResponseBody RemoteMeasureInstanceList getMeasureExecutions(@RequestParam("id") String agentId) {		
		RemoteMeasureInstanceList list = new RemoteMeasureInstanceList();
		list.getRemoteInstances().addAll(schedulingService.getSheduledRemoteMeasure(agentId));
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
}