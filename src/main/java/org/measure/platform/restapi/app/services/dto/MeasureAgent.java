package org.measure.platform.restapi.app.services.dto;

import java.util.ArrayList;
import java.util.List;

import org.measure.smm.measure.model.SMMMeasure;

public class MeasureAgent {
	
	private String agentName;
	
	private String agentAdress;
	
	private List<SMMMeasure> providedMeasures;

	public MeasureAgent(){
		this.providedMeasures = new ArrayList<>();
	}
	
	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentAdress() {
		return agentAdress;
	}

	public void setAgentAdress(String agentAdress) {
		this.agentAdress = agentAdress;
	}

	public List<SMMMeasure> getProvidedMeasures() {
		return providedMeasures;
	}

	public void setProvidedMeasures(List<SMMMeasure> providedMeasures) {
		this.providedMeasures = providedMeasures;
	}

}
