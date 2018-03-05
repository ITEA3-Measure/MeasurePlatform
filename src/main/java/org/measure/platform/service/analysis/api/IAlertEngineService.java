package org.measure.platform.service.analysis.api;

import org.measure.platform.service.analysis.data.alert.AlertData;
import org.measure.platform.service.analysis.data.alert.AlertReport;
import org.measure.platform.service.analysis.data.alert.AlertSubscription;

public interface IAlertEngineService {
	
	/**
	 * Allow an analysis tool to subscribe to a new alert
	 * @param suscribtion Alert subscription data
	 */
	public void subscribe(AlertSubscription suscribtion);
	
	
	/**
	 * Unsubscribe to an alert
	 * @param alertId Id of the registred alerte
	 */
	public void unsubscribe(AlertSubscription suscribtion);
	
	/**
	 * Get the list of alerts arrive between now and the last call of the service by the analysis tool.
	 * @param analysisTool name of the analysis tool
	 * @return List of new Notifications.
	 */
	public AlertReport getAlerts(String analysisTool);
	
	
	/**
	 * Publish a new alert
	 * @param alert Alert details
	 */
	public void alert(AlertData alert);



}


