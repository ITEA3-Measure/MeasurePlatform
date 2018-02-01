package org.measure.platform;

import javax.inject.Inject;

import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.service.measurement.api.IElasticsearchIndexManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @Inject
    private IMeasureCatalogueService catalogueService;
	
    @Inject
    private IElasticsearchIndexManager indexManager;
	/**
	 * This event is executed as late as conceivably possible to indicate that
	 * the application is ready to service requests.
	 */
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		
		// Update All Kibana index if required
		indexManager.updateIndex(catalogueService.getAllMeasures());
		
		return;
	}

}