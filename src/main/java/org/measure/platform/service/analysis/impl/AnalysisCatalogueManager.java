package org.measure.platform.service.analysis.impl;

import java.util.Date;

import javax.inject.Inject;

import org.measure.platform.service.analysis.api.IAnalysisCatalogueService;
import org.measure.platform.service.analysis.data.RegistredAnalysisService;
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
	
	/**
	 * Remove Analysis Service form Catalogue which are not active from more than 1 minute
	 */
    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
    	 for (RegistredAnalysisService service : analyseCatalogue.getAllAnalysisService()) {
             if (new Date().getTime() - service.getLiveSign().getTime() > 60000) {
            	 analyseCatalogue.unregisterAnalysisService(service);   
             }
         }
    }

}
