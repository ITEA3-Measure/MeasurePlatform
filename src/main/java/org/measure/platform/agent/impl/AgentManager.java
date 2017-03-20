package org.measure.platform.agent.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.measure.platform.agent.api.IAgentManager;
import org.measure.platform.agent.api.IRemoteCatalogueService;
import org.measure.platform.agent.impl.data.RemoteAgent;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.restapi.app.services.dto.MeasureAgent;
import org.measure.platform.smmengine.api.IShedulingService;
import org.measure.smm.measure.model.SMMMeasure;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableScheduling
public class AgentManager implements IAgentManager {

	@Inject
	IRemoteCatalogueService remoteCatalogue;

	@Inject
	MeasureInstanceService measureInstanceService;

	@Inject
	IShedulingService shedulingService;
		
	@Scheduled(fixedRate = 20000)
	public void reportCurrentTime() {
		for (RemoteAgent agent : remoteCatalogue.getAllAgents()) {
			if (new Date().getTime() - agent.getLastLifeSign().getTime() > 20000) {
				remoteCatalogue.unregisterAgent(agent.getAdress());
				for (SMMMeasure measure : agent.getMeasures().values()) {
					for (MeasureInstance measureInstance : measureInstanceService.findMeasureInstanceByReference(measure.getName())) {
						shedulingService.removeMeasure(measureInstance.getId());
					}
				}
			}
		}
	}

	
	@Override
	public List<MeasureAgent> getAgents() {
		Map<String, MeasureAgent> map = new HashMap<>();
		for (SMMMeasure measure : this.remoteCatalogue.getAllMeasures()) {

			MeasureAgent agent = map.get(measure.getCallbackAdress());
			if (agent == null) {
				agent = new MeasureAgent();
				agent.setAgentName(measure.getCallbackLable());
				agent.setAgentAdress(measure.getCallbackAdress());
				map.put(measure.getCallbackAdress(), agent);
			}
			agent.getProvidedMeasures().add(measure);
		}
		List<MeasureAgent> agents = new ArrayList<>(map.values());
		agents.sort(new Comparator<MeasureAgent>() {
			@Override
			public int compare(MeasureAgent o1, MeasureAgent o2) {
				return o1.getAgentName().compareTo(o2.getAgentName());
			}
		});

		return agents;
	}

	@Override
	public void registerLifeSign(String agentId) {
		remoteCatalogue.getAgent(agentId).setLastLifeSign(new Date());
	}
	
	@Override
	public boolean isAlive(String agentId){
		return remoteCatalogue.getAgent(agentId) != null;
	}

}
