package org.measure.platform.restapi.analysis.dto;


public class CardConfiguration {

	private String cardUrl;
	private String label;

	private Integer preferedWidth;	
	private Integer preferedHeight;
	
	
	
	public Integer getPreferedWidth() {
		return preferedWidth;
	}

	public void setPreferedWidth(Integer preferedWidth) {
		this.preferedWidth = preferedWidth;
	}

	public Integer getPreferedHeight() {
		return preferedHeight;
	}

	public void setPreferedHeight(Integer preferedHeight) {
		this.preferedHeight = preferedHeight;
	}

	public String getCardUrl() {
		return this.cardUrl;
	}

	public void setCardUrl(String cardUrl) {
		this.cardUrl = cardUrl;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
}
