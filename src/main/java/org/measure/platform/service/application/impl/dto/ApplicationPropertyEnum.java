package org.measure.platform.service.application.impl.dto;

import java.util.ArrayList;
import java.util.List;

public class ApplicationPropertyEnum {
	private List<ApplicationPropertyEnumValue> enumvalue = new ArrayList<>();

	public List<ApplicationPropertyEnumValue> getEnumvalue() {
		return enumvalue;
	}

	public void setEnumvalue(List<ApplicationPropertyEnumValue> enumvalue) {
		this.enumvalue = enumvalue;
	}
}
