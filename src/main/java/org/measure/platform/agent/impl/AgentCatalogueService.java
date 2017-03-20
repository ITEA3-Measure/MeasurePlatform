package org.measure.platform.agent.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.measure.platform.agent.api.IRemoteCatalogueService;
import org.measure.platform.agent.impl.data.RemoteAgent;
import org.measure.smm.measure.model.SMMMeasure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "singleton")
public class AgentCatalogueService implements IRemoteCatalogueService {

	private final Logger log = LoggerFactory.getLogger(AgentCatalogueService.class);	
	
	private Map<String,RemoteAgent> agentMap = new HashMap<>();

	@Override
	public void registerRemoteMeasure(SMMMeasure remoteMeasure) {	
		
		
		RemoteAgent agent = agentMap.get(remoteMeasure.getCallbackAdress());
		
		if(agent == null){
			agent = new RemoteAgent(remoteMeasure.getCallbackLable(),remoteMeasure.getCallbackAdress());
			agentMap.put(remoteMeasure.getCallbackAdress(), agent);
		}
		
		
		if(!agent.getMeasures().containsKey(remoteMeasure.getName())){
			agent.getMeasures().put(remoteMeasure.getName(),remoteMeasure);
		}
		
		log.info("Register Remote Measure \"" +remoteMeasure.getName()+ "\" form " + remoteMeasure.getCallbackLable());
	}

	@Override
	public List<SMMMeasure> getAllMeasures() {
		List<SMMMeasure> result = new ArrayList<>();	
		for(RemoteAgent agent : agentMap.values()){
			result.addAll(agent.getMeasures().values());
		}
		return result;
	}

	@Override
	public RemoteAgent getAgent(String agentId){
		return agentMap.get(agentId);
	}
	
	@Override
	public void unregisterAgent(String agentId) {		
		agentMap.remove(agentId);
	}

	@Override
	public void unregisterRemoteMeasure(String measureName, String callbackAdress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<RemoteAgent> getAllAgents() {
		return this.agentMap.values();
	}
	


}
