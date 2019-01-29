package org.measure.platform.service.analysis.impl;

import java.util.Date;

import javax.inject.Inject;

import org.measure.platform.core.data.api.IProjectAnalysisService;
import org.measure.platform.core.data.entity.ProjectAnalysis;
import org.measure.platform.service.analysis.api.IAnalysisCatalogueService;
import org.measure.platform.service.analysis.data.analysis.RegistredAnalysisService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableScheduling
public class AnalysisCatalogueManager {
	
	@Inject
	private IAnalysisCatalogueService analyseCatalogue;
	
	@Inject 
	private IProjectAnalysisService analysisService;
	
	/**
	 * Remove Analysis Service form Catalogue which are not active from more than 1 minute
	 */
    @Scheduled(fixedRate = 30000)
    public void reportCurrentTime() {
    	 for (RegistredAnalysisService service : analyseCatalogue.getAllAnalysisService()) {
             if (new Date().getTime() - service.getLiveSign().getTime() > 30000) {
            	analyseCatalogue.unregisterAnalysisService(service);        	 
             }
         }
    	 
    	 
    	 for(ProjectAnalysis analyse : analysisService.findAll()) {
    		if( analyseCatalogue.getAnalysisServiceByName(analyse.getAnalysisToolId()) == null) {
    			analysisService.delete(analyse.getId());
    		}
    	 }
    }

}
