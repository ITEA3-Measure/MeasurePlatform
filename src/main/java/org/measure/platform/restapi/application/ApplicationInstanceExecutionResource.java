package org.measure.platform.restapi.application;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.ApplicationInstanceService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.service.analysis.data.alert.AlertData;
import org.measure.platform.service.analysis.data.alert.AlertProperty;
import org.measure.platform.service.analysis.data.alert.AlertType;
import org.measure.platform.service.application.api.IApplicationInstanceConfigurationService;
import org.measure.platform.service.application.api.IApplicationScheduling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/application-instance/scheduling")

public class ApplicationInstanceExecutionResource {

	private final Logger log = LoggerFactory.getLogger(ApplicationInstanceExecutionResource.class);
	
	
	@Inject
	private IApplicationScheduling applicationInstanceService;
	
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public Boolean startApplicationInstanceScheduling(@RequestParam("id") String id) {
    	log.debug("Request to start scheduling application instance id : " + id);

        if (id.matches("\\d+")) {
            Long instanceId = Long.valueOf(id);
            
            return applicationInstanceService.startApplication(instanceId);
        }
        return false;
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    public Boolean stopApplicationInstanceScheduling(@RequestParam("id") String id) {
    	log.debug("Request to stop scheduling application instance id : " + id);
        if (id.matches("\\d+")) {
            Long instanceId = Long.valueOf(id);    
            return applicationInstanceService.stopApplication(instanceId);

        }
        return false;
    }


}
