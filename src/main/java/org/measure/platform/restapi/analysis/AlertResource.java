package org.measure.platform.restapi.analysis;

import java.net.URISyntaxException;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.analysis.api.IAlertEngineService;
import org.measure.platform.service.analysis.api.IAlertSubscriptionManager;
import org.measure.platform.service.analysis.data.alert.AlertReport;
import org.measure.platform.service.analysis.data.alert.AlertSubscription;
import org.measure.platform.service.analysis.data.analysis.AnalysisService;
import org.measure.platform.service.analysis.impl.AlertSubscriptionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/analysis/alert")
public class AlertResource {

	private final Logger log = LoggerFactory.getLogger(AlertResource.class);

	
	@Inject
	private IAlertEngineService alertEngineService;
	
	@Inject
	private IAlertSubscriptionManager alertSubscriptionManager;
	
	/**
	 * PUT /subscribe : Subscribe to Alerts Events
	 * @param suscribtion Subscription informations
	 */
    @PutMapping("/subscribe")
    @Timed
    public void suscribeToNotification(@Valid @RequestBody AlertSubscription suscribtion) {
        log.debug("REST request to suscribe to AlertResource : {}", suscribtion);
        alertSubscriptionManager.subscribe(suscribtion);     
    }
    /**
     * PUT /unsubscribe : Unsubscribe to Alerts Events
     * @param suscribtion Subscription informations
     */
    @PutMapping("/unsubscribe")
    @Timed
    public void unsubscribeToNotification(@Valid @RequestBody AlertSubscription suscribtion ) {
        log.debug("REST request to unsubscribe to AlertResource : {}", suscribtion);
        alertSubscriptionManager.unsubscribe(suscribtion);     
    }
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public AlertReport startMeasureSheduling(@RequestParam("id") String analysisTool) {   
        return alertEngineService.getAlerts(analysisTool);
    }

}
