package org.measure.platform.service.analysis.data.alert;

public class AlertProperty {

	private String property;
	private String value;
	
	public AlertProperty(String property,String value){
		this.property= property;
		this.value = value;
	}

	public String getProperty() {
		return this.property;
	}

	public String getValue() {
		return this.value;
	}
}
