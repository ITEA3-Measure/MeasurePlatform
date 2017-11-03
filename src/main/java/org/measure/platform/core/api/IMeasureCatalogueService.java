package org.measure.platform.core.api;

import java.nio.file.Path;
import java.util.List;

import org.measure.smm.measure.api.IMeasure;
import org.measure.smm.measure.model.SMMMeasure;

/**
 * Service Interface for managing measure repository.
 */
public interface IMeasureCatalogueService {
    void storeMeasure(Path measure);

    List<SMMMeasure> getAllMeasures();

    SMMMeasure getMeasure(String measureId);

    void deleteMeasure(String measureId);

    IMeasure getMeasureImplementation(String measureId);

}
