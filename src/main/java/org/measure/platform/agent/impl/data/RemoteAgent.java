package org.measure.platform.agent.impl.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.measure.smm.measure.model.SMMMeasure;

public class RemoteAgent {

	private Map<String, SMMMeasure> measures;

	private String label;

	private Date lastLifeSign;

	public RemoteAgent(String label) {
		this.label = label;
		this.lastLifeSign = new Date();
		this.measures = new HashMap<>();
	}

	public Map<String, SMMMeasure> getMeasures() {
		return measures;
	}

	public void setMeasures(Map<String, SMMMeasure> measures) {
		this.measures = measures;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Date getLastLifeSign() {
		return lastLifeSign;
	}

	public void setLastLifeSign(Date lastLifeSign) {
		this.lastLifeSign = lastLifeSign;
	}

}
