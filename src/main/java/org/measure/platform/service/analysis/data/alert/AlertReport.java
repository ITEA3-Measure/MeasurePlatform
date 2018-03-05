package org.measure.platform.service.analysis.data.alert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Notification collected by the Measure Platform and consume by the external analysis tools.
 */
public class AlertReport {
	
	private Date from;
	
	private List<AlertData> alerts = new ArrayList<>();

	public Date getFrom() {
		return this.from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public List<AlertData> getAlerts() {
		return this.alerts;
	}

	public void setAlerts(List<AlertData> alerts) {
		this.alerts = alerts;
	}

}
