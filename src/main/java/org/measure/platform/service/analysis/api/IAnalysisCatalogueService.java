package org.measure.platform.service.analysis.api;

import java.util.List;

import org.measure.platform.service.analysis.data.analysis.AnalysisService;
import org.measure.platform.service.analysis.data.analysis.RegistredAnalysisService;

public interface IAnalysisCatalogueService {
	
	
	/**
	 * Register a new AnalysisService
	 * @param service The service to register
	 */
	public void registerAnalysisService(AnalysisService service);
	
	/**
	 * Unregister an analysis service
	 * @param service The service to unregister
	 */
	public void unregisterAnalysisService(AnalysisService service);
	
	/**
	 * Find an Analysis Service by is name
	 * @param name The name of the required service
	 * @return The find analysis service
	 */
	public AnalysisService getAnalysisServiceByName(String name);
	
	/**
	 * Find all registred analysis service
	 * @return List of all analysis services
	 */
	public List<RegistredAnalysisService> getAllAnalysisService();

}
