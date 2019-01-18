package org.measure.platform.service.application.impl.dto;

import java.util.ArrayList;
import java.util.List;

public class ApplicationInstanceConfiguration {
	
	private Long id;
	
	private String name;
	
	private String applicationType;
	
	private List<ApplicationProperty> properties;
	
	private Boolean isEnable;
	
	public ApplicationInstanceConfiguration() {
		this.properties = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public List<ApplicationProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<ApplicationProperty> properties) {
		this.properties = properties;
	}

	public Boolean getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	
	

}
