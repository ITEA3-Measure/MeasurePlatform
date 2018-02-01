package org.measure.platform.service.analysis.data;

/** 
 * External data analysis service which is integrated with Measure Platform.
 * COuld provide the url of a general configuration page
 */
public class AnalysisService {
	
	private String name;
	
	private String description;
	
	private String configurationURL;
	
	public AnalysisService(String name,String description){
		this.name = name;
		this.description = description;
	}
	

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

	public String getConfigurationURL() {
		return configurationURL;
	}

	public void setConfigurationURL(String configurationURL) {
		this.configurationURL = configurationURL;
	}

}
