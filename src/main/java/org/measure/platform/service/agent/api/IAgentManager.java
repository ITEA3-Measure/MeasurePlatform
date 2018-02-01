package org.measure.platform.service.agent.api;

import java.util.List;

import org.measure.platform.restapi.measure.dto.MeasureAgent;

public interface IAgentManager {
    List<MeasureAgent> getAgents();

    void registerLifeSign(String agentId);

    boolean isAlive(String agentId);

}
