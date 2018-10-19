package org.measure.platform.service.agent.api;

import java.util.Collection;
import java.util.List;

import org.measure.platform.service.agent.data.RemoteAgent;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.remote.RemoteMeasureExternal;

public interface IRemoteCatalogueService {
    void registerRemoteMeasure(SMMMeasure remoteMeasure, String agentName);

    List<SMMMeasure> getAllMeasures();

    RemoteAgent getAgent(String agentId);

    void unregisterAgent(String agentId);

    Collection<RemoteAgent> getAllAgents();

    SMMMeasure getMeasureByName(String measure, String agentId);
}
