package org.measure.platform.core.measurement.api;

import java.util.List;

import org.measure.platform.restapi.measure.dto.KibanaVisualisation;
import org.measure.smm.measure.api.IMeasurement;

public interface IMeasurementStorage {
    
    void putMeasurement(String measureInstance, IMeasurement measurement);

    List<IMeasurement> getMeasurement(String measureId, Integer numberRef, String filter);

    List<KibanaVisualisation> findKibanaVisualisation();

    List<KibanaVisualisation> findKibanaDashboard();

	List<IMeasurement> getMeasurementPage(String measureInstance, Integer size, Integer page, String filter);
	
	
	IMeasurement getLastMeasurement(String measureInstance);

}
