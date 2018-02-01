package org.measure.platform.restapi.analysis.dto;

import java.util.List;

public class AnalysisConfiguration {
	private Long projectAnalysisId;
	private String viewUrl;
	private String configurationUrl;
	private List<CardConfiguration> cards;
	
	public Long getProjectAnalysisId() {
		return this.projectAnalysisId;
	}
	public void setProjectAnalysisId(Long projectAnalysisId) {
		this.projectAnalysisId = projectAnalysisId;
	}
	public String getViewUrl() {
		return this.viewUrl;
	}
	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}
	public String getConfigurationUrl() {
		return this.configurationUrl;
	}
	public void setConfigurationUrl(String configurationUrl) {
		this.configurationUrl = configurationUrl;
	}
	public List<CardConfiguration> getCards() {
		return this.cards;
	}
	public void setCards(List<CardConfiguration> cards) {
		this.cards = cards;
	}
	
	

}
