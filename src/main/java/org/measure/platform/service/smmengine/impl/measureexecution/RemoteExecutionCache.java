package org.measure.platform.service.smmengine.impl.measureexecution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.measure.smm.remote.RemoteMeasureExternal;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class RemoteExecutionCache {
	
	private Map<String,List<RemoteMeasureExternal>> pendingExecutions = new HashMap<>();

	public List<RemoteMeasureExternal> getPendingExecutione(String agentId) {
		 List<RemoteMeasureExternal>  result =  pendingExecutions.get(agentId);
		 if(result != null){
			 pendingExecutions.put(agentId, new ArrayList<>());		 
			 return result;
		 }	 
		return new ArrayList<>();
	}
	
	
	public void registerSingleMeasureExecution(RemoteMeasureExternal executionRequest) {
		String agentId = executionRequest.getMeasureName().substring(executionRequest.getMeasureName().indexOf("(") +1, executionRequest.getMeasureName().length() - 1) ;

		List<RemoteMeasureExternal> agentExecs = pendingExecutions.get(agentId);
		if (agentExecs == null) {
			agentExecs = new ArrayList<>();
		}
		agentExecs.add(executionRequest);
		pendingExecutions.put(agentId, agentExecs);
	}
}
