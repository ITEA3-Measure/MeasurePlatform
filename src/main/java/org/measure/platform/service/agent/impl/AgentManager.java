package org.measure.platform.service.agent.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.data.api.IMeasureInstanceService;
import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.restapi.measure.dto.MeasureAgent;
import org.measure.platform.service.agent.api.IAgentManager;
import org.measure.platform.service.agent.api.IRemoteCatalogueService;
import org.measure.platform.service.agent.data.RemoteAgent;
import org.measure.platform.service.smmengine.api.ISchedulingService;
import org.measure.smm.measure.model.SMMMeasure;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableScheduling
public class AgentManager implements IAgentManager {
    @Inject
     IRemoteCatalogueService remoteCatalogue;

    @Inject
     IMeasureInstanceService measureInstanceService;

    @Inject
     ISchedulingService shedulingService;

    @Scheduled(fixedRate = 20000)
    public void reportCurrentTime() {
        for (RemoteAgent agent : remoteCatalogue.getAllAgents()) {
            if (new Date().getTime() - agent.getLastLifeSign().getTime() > 20000) {
                remoteCatalogue.unregisterAgent(agent.getLabel());
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
        List<MeasureAgent> agents = new ArrayList<>();
        
        
        for(RemoteAgent agent : remoteCatalogue.getAllAgents()){
            MeasureAgent magent = new MeasureAgent();
            magent.setAgentName(agent.getLabel());
            magent.setProvidedMeasures(new ArrayList<>(agent.getMeasures().values()));
            agents.add(magent);
        }
        
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
    public boolean isAlive(String agentId) {
        return remoteCatalogue.getAgent(agentId) != null;
    }

}
