package org.measure.platform.measurementstorage.api;

import java.util.List;

import org.measure.smm.measure.api.IMeasurement;

public interface IMeasurementStorage {
	public void putMeasurement(String measureId,Boolean manageLast,IMeasurement measurement);
	public IMeasurement getLastMeasurement(String measureId);
	public List<IMeasurement> getMeasurement(String measureId, Integer numberRef,String filter);
}
