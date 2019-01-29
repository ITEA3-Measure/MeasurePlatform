package org.measure.platform.core.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "analysiscard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AnalysisCard {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "card_label", nullable = false)
	private String cardLabel;

	@NotNull
	@Column(name = "card_url", nullable = false)
	private String cardUrl;
	
	@Column(name = "prefered_width", nullable = false)
	private Integer preferedWidth;
	
	@Column(name = "prefered_height", nullable = false)
	private Integer preferedHeight;
	
    @ManyToOne
    private ProjectAnalysis projectanalysis;

	@OneToMany(mappedBy = "analysiscard")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<MeasureView> measureviews = new HashSet<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
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

	public String getCardLabel() {
		return cardLabel;
	}

	public void setCardLabel(String cardLabel) {
		this.cardLabel = cardLabel;
	}

	public String getCardUrl() {
		return cardUrl;
	}

	public void setCardUrl(String cardUrl) {
		this.cardUrl = cardUrl;
	}

	public Set<MeasureView> getMeasureviews() {
		return measureviews;
	}

	public void setMeasureviews(Set<MeasureView> measureviews) {
		this.measureviews = measureviews;
	}

	public ProjectAnalysis getProjectanalysis() {
		return projectanalysis;
	}

	public void setProjectanalysis(ProjectAnalysis projectanalysis) {
		this.projectanalysis = projectanalysis;
	}
	
	
}
