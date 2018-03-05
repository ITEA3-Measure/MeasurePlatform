package org.measure.platform.service.analysis.api;

import org.measure.platform.service.analysis.data.alert.AlertSubscription;

public interface IAlertSubscriptionManager {
	
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
	 * Reload registered Subscribtion form database at application startup
	 */
	void init();

}
