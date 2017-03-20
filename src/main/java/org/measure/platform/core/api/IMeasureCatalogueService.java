package org.measure.platform.core.api;

import java.nio.file.Path;
import java.util.List;

import org.measure.smm.measure.api.IMeasure;
import org.measure.smm.measure.model.SMMMeasure;

/**
 * Service Interface for managing measure repository.
 */
public interface IMeasureCatalogueService {
	
	public void storeMeasure(Path measure);
	
	public List<SMMMeasure> getAllMeasures();
	
	public SMMMeasure getMeasure(String measureId);

	public void deleteMeasure(String measureId);
	
	public IMeasure getMeasureImplementation(String measureId);

}
