package org.measure.platform.measurementstorage.api;

import java.util.List;

import org.measure.platform.restapi.app.services.dto.KibanaVisualisation;
import org.measure.smm.measure.api.IMeasurement;

public interface IMeasurementStorage {
    void putMeasurement(String index, String measureId, Boolean manageLast, IMeasurement measurement);

    IMeasurement getLastMeasurement(String measureId);

    List<IMeasurement> getMeasurement(String measureId, Integer numberRef, String filter);

    List<KibanaVisualisation> findKibanaVisualisation();

    List<KibanaVisualisation> findKibanaDashboard();

}
