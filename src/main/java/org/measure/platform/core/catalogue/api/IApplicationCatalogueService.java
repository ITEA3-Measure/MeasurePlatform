package org.measure.platform.core.catalogue.api;

import java.nio.file.Path;
import java.util.List;

import org.measure.smm.application.model.SMMApplication;

/**
 * Service Interface for managing Measurement Application repository.
 */
public interface IApplicationCatalogueService {

    List<SMMApplication> getAllApplications();

	void deleteApplication(String applicationName);

	void storeApplication(Path application);

	SMMApplication getApplication(String applicationName);


}
