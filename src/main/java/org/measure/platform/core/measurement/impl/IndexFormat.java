package org.measure.platform.core.measurement.impl;

public class IndexFormat {

	public static final String ES_BASE_INDEX = "measure.default.init";
	public static final String KIBANA_BASE_INDEX = "measure.*";
	public static final String PREFIX_INDEX = "measure.";

	public static String getMeasureInstanceIndex(String instanceName) {
		return IndexFormat.PREFIX_INDEX + instanceName.toLowerCase();
	}
}
