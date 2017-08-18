package org.measure.platform.measurementstorage.api;

import java.util.List;

import org.measure.platform.restapi.app.services.dto.KibanaVisualisation;
import org.measure.smm.measure.api.IMeasurement;
import org.measure.smm.measure.model.SMMMeasure;

public interface IMeasurementStorage {
	public void putMeasurement(String index,String measureId,Boolean manageLast,IMeasurement measurement);
	public IMeasurement getLastMeasurement(String measureId);
	public List<IMeasurement> getMeasurement(String measureId, Integer numberRef,String filter);
	public List<KibanaVisualisation> findKibanaVisualisation();
	public List<KibanaVisualisation> findKibanaDashboard();
}
