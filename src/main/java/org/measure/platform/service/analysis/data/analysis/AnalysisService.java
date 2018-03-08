package org.measure.platform.service.analysis.data.analysis;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "AnalysisService")
public class AnalysisService {
		
	private String name;
		
	private String description;
		
	private String configurationURL;
	
	public AnalysisService(){
		
	}
	
	public AnalysisService(String name,String description){
		this.name = name;
		this.description = description;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlAttribute
	public String getConfigurationURL() {
		return configurationURL;
	}

	public void setConfigurationURL(String configurationURL) {
		this.configurationURL = configurationURL;
	}

}
