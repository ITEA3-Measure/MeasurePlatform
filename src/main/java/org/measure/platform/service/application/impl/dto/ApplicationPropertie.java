package org.measure.platform.service.application.impl.dto;


public class ApplicationPropertie {
	
	private String name;

	private String defaultValue;
	
	private String description;
	
	private ApplicationPropertiePropertyType type;
	
	private ApplicationPropertiePropertyEnum enumType;
	
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApplicationPropertiePropertyType getType() {
		return type;
	}

	public void setType(ApplicationPropertiePropertyType type) {
		this.type = type;
	}

	public ApplicationPropertiePropertyEnum getEnumType() {
		return enumType;
	}

	public void setEnumType(ApplicationPropertiePropertyEnum enumType) {
		this.enumType = enumType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
