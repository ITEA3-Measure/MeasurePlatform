package org.measure.platform.service.application.impl.dto;

import java.util.ArrayList;
import java.util.List;

public class ApplicationInstanceConfiguration {
	
	private int id;
	
	private String name;
	
	private String applicationType;
	
	private List<ApplicationPropertie> properties;
	
	private Boolean isEnable;
	
	public ApplicationInstanceConfiguration() {
		this.properties = new ArrayList<>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApplicationType() {
		return applicationType;
	}

	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}

	public List<ApplicationPropertie> getProperties() {
		return properties;
	}

	public void setProperties(List<ApplicationPropertie> properties) {
		this.properties = properties;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	
	

}
