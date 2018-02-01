package org.measure.platform.service.smmengine.api;

import java.util.List;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.smm.remote.RemoteMeasureInstance;

public interface ISchedulingService {
    Boolean scheduleMeasure(MeasureInstance measure);

    Boolean removeMeasure(Long measureInstanceId);

    Boolean isShedule(Long measureInstanceId);

    List<RemoteMeasureInstance> getSheduledRemoteMeasure(String agentId);

}
