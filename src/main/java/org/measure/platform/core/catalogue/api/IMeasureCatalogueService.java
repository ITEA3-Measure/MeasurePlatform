package org.measure.platform.core.catalogue.api;

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

    void deleteMeasure(String measureId);
    
    SMMMeasure getMeasure(String application,String measure);

    IMeasure getMeasureImplementation(String application,String measure);

}
