package org.measure.platform.service.analysis.api;

import java.util.List;

import org.measure.platform.core.entity.AlertEvent;
import org.measure.platform.service.analysis.data.Notification;

public interface INotificationService {
	
	/**
	 *  Suscribe to a notificationn.
	 */
	public void suscribe(AlertEvent suscribtion);
	
	
	/** 
	 * Unsuscribe to a notification
	 */
	public void unsuscribe();
	
	/**
	 * Get the list of notifications arrive between now and the last call of the service by the analysis tool.
	 * @return List of new Notifications.
	 * 
	 */
	public List<Notification> getNotifications();

	/**
	 * Allow the platform to send notification to analysis tool which have suscribe to this notification
	 * 
	 */
	public void notifyAnalysisTool();

}


