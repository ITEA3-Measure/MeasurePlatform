package org.measure.platform.service.analysis.data.alert;

import java.util.Arrays;
import java.util.List;

public enum AlertType {	
	ANALYSIS_ENABLE(Arrays.asList(),Arrays.asList("ANALYSISID")),
	ANALYSIS_DESABLE(Arrays.asList(),Arrays.asList("ANALYSISID")),
	MEASURE_ADDED(Arrays.asList(),Arrays.asList("MEASUREID")),
	MEASURE_REMOVED(Arrays.asList(),Arrays.asList("MEASUREID")),
	MEASURE_SCHEDULED(Arrays.asList(),Arrays.asList("MEASUREID")),
	MEASURE_UNSCHEDULED(Arrays.asList(),Arrays.asList("MEASUREID"));
	
	private List<String> requestProperties;
	
	private List<String> responsProperties;

	
	private AlertType(List<String> requestProperties,List<String> responsProperties){	
		this.requestProperties = requestProperties;
		this.responsProperties = responsProperties;
	}
	
	public List<String> getRequestProperties(){
		return this.requestProperties;
	}
	

	public List<String> getResponsProperties() {
		return responsProperties;
	}

	public static AlertType fromString(String value){
		if("ANALYSIS_ENABLE".equals(value)){
			return ANALYSIS_ENABLE;			
		}else if("ANALYSIS_DESABLE".equals(value)){
			return ANALYSIS_DESABLE;			
		}else if("MEASURE_ADDED".equals(value)){
			return MEASURE_ADDED;		
		}else if("MEASURE_REMOVED".equals(value)){
			return MEASURE_REMOVED;		
		}else if("MEASURE_SCHEDULED".equals(value)){
			return MEASURE_SCHEDULED;			
		}else if("MEASURE_UNSCHEDULED".equals(value)){
			return MEASURE_UNSCHEDULED;			
		}
		
		return null;
	}
}
