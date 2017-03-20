package org.measure.platform.smmengine.api;

import java.util.List;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.smm.remote.RemoteMeasureInstance;

public interface IShedulingService {

	Boolean scheduleMeasure(MeasureInstance measure);

	Boolean removeMeasure(Long measureInstanceId);

	Boolean isShedule(Long measureInstanceId);

	List<RemoteMeasureInstance> getSheduledRemoteMeasure(String agentId);

}
