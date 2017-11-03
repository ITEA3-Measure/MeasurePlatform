package org.measure.platform.agent.api;

import java.util.Collection;
import java.util.List;

import org.measure.platform.agent.impl.data.RemoteAgent;
import org.measure.smm.measure.model.SMMMeasure;

public interface IRemoteCatalogueService {
    void registerRemoteMeasure(SMMMeasure remoteMeasure, String agentName);

    List<SMMMeasure> getAllMeasures();

    RemoteAgent getAgent(String agentId);

    void unregisterAgent(String agentId);

    Collection<RemoteAgent> getAllAgents();

    SMMMeasure getMeasureByName(String measure, String agentId);

}
