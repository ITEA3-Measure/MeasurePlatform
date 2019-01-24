package org.measure.platform.service.application.impl.dto;


public class ApplicationProperty {
	
	private String name;
	
	private String description;
	
	private ApplicationPropertyType type;
	
	private ApplicationPropertyEnum enumType;
	
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ApplicationPropertyType getType() {
		return type;
	}

	public void setType(ApplicationPropertyType type) {
		this.type = type;
	}

	public ApplicationPropertyEnum getEnumType() {
		return enumType;
	}

	public void setEnumType(ApplicationPropertyEnum enumType) {
		this.enumType = enumType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
}
