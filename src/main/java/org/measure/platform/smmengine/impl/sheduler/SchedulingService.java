package org.measure.platform.smmengine.impl.sheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.measure.platform.agent.api.IAgentManager;
import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasurePropertyService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.measure.platform.smmengine.api.ILoggerService;
import org.measure.platform.smmengine.api.IShedulingService;
import org.measure.platform.smmengine.impl.measureexecution.MeasureExecutionService;
import org.measure.smm.log.MeasureLog;
import org.measure.smm.measure.api.IMeasure;
import org.measure.smm.remote.RemoteMeasureInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class SchedulingService implements IShedulingService {

	@Autowired
	private TaskScheduler taskScheduler;

	@Inject
	private MeasureExecutionService measureExecutionService;

	@Inject
	private IMeasureCatalogueService measureCatalogue;

	@Inject
	private MeasureInstanceService measureInstanceService;
	
	@Inject
	private MeasurePropertyService measurePropertyService;
	
	@Inject 
	IAgentManager agentService;


	@Inject
	private ILoggerService logger;

	private Map<String, List<Long>> remotsJobs;

	private Map<Long, ScheduledFuture> jobs;

	@PostConstruct
	public void doSomething() {
		this.jobs = new HashMap<>();
		this.remotsJobs = new HashMap<>();
	}

	@Override
	public synchronized Boolean scheduleMeasure(MeasureInstance measure) {

		if (measure.isIsShedule() != null && measure.isIsShedule() && measure.getShedulingExpression() != null
				&& measure.getShedulingExpression().matches("\\d+")) {
			if (measure.isIsRemote()) {		
				if(!agentService.isAlive(measure.getRemoteAdress())){
					return false;
				}
				scheduleRemoteMeasure(measure);
			} else {
				scheduleLocalExecution(measure);
			}
			return true;
		}
		return false;
	}

	@Override
	public synchronized List<RemoteMeasureInstance> getSheduledRemoteMeasure(String agentId) {
		List<RemoteMeasureInstance> result = new ArrayList<>();

		for (Long instanceId : this.remotsJobs.get(agentId)) {
			MeasureInstance instance = measureInstanceService.findOne(instanceId);
			RemoteMeasureInstance remoteM = new RemoteMeasureInstance();
			remoteM.setInstanceName(instance.getInstanceName());
			remoteM.setMeasureName(instance.getMeasureName());
			remoteM.setMeasureVersion(instance.getMeasureVersion());
			remoteM.setShedulingExpression(instance.getShedulingExpression());
			remoteM.setMeasureId(instance.getId());

			for (MeasureProperty prop : measurePropertyService.findByInstance(instance)) {
				remoteM.getProperties().put(prop.getPropertyName(), prop.getPropertyValue());
			}

			result.add(remoteM);

		}

		return result;
	}

	private synchronized void scheduleRemoteMeasure(MeasureInstance measure) {

		List<Long> measures = this.remotsJobs.get(measure.getRemoteAdress());
		if (measures == null) {
			measures = new ArrayList<>();
			this.remotsJobs.put(measure.getRemoteAdress(), measures);
		}
		measures.add(measure.getId());
	}

	private void scheduleLocalExecution(MeasureInstance measure) {
		Integer rate = Integer.valueOf(measure.getShedulingExpression());

		IMeasure measureImpl = measureCatalogue.getMeasureImplementation(measure.getMeasureName());

		ScheduledFuture job = taskScheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {

				MeasureLog log = measureExecutionService.executeMeasure(measure, measureImpl);
				logger.addMeasureExecutionLog(log);

				if (!log.isSuccess()) {
					removeMeasure(measure.getId());
				}

			}
		}, rate);
		this.jobs.put(measure.getId(), job);
	}

	@Override
	public synchronized Boolean removeMeasure(Long measureInstanceId) {
		// Stop Measures executed Localy
		ScheduledFuture job = jobs.get(measureInstanceId);
		if (job != null) {
			job.cancel(true);
			this.jobs.remove(measureInstanceId);
		} else {
			// Stop Measures executed Remotely

			for (List<Long> agentMeasures : this.remotsJobs.values()) {
				if (agentMeasures.contains(measureInstanceId)) {
					agentMeasures.remove(measureInstanceId);
				}
			}

		}
		return true;
	}

	@Override
	public synchronized Boolean isShedule(Long measureInstanceId) {
		if (jobs.containsKey(measureInstanceId)) {
			return true;
		}
		for (String agentId: this.remotsJobs.keySet()) {
			if (this.remotsJobs.get(agentId).contains(measureInstanceId)) {					
				return agentService.isAlive(agentId);
			}
		}
		return false;
	}

}