package org.measure.platform.agent.api;

import java.util.List;

import org.measure.platform.restapi.app.services.dto.MeasureAgent;

public interface IAgentManager {	
	List<MeasureAgent> getAgents();
	public void registerLifeSign(String agentId);
	boolean isAlive(String agentId);
}
