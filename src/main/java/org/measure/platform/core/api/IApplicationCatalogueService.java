package org.measure.platform.core.api;

import java.util.List;

import org.measure.smm.measurementapplication.model.SMMApplication;

/**
 * Service Interface for managing Measurement Application repository.
 */
public interface IApplicationCatalogueService {
    //void storeApplication(Path measure);

    List<SMMApplication> getAllApplications();

    //SMMMeasure getMeasure(String measureId);

    //void deleteMeasure(String measureId);

}
