package org.measure.platform.core.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
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

/**
 * A Phase.
 */
@Entity
@Table(name = "phase")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Phase implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "phase_name", nullable = false)
	private String phaseName;

	@Column(name = "phase_description")
	private String phaseDescription;

	@ManyToOne
	private Project project;

	@OneToMany(mappedBy = "phase")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<Dashboard> dashboards = new HashSet<>();

	@OneToMany(mappedBy = "phase")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<MeasureView> views = new HashSet<>();
	
	
	@OneToMany(mappedBy = "phaseoverview")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<MeasureView> overviews = new HashSet<>();

	@Column(name = "phase_order")
	private String order;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}

	public String getPhaseName() {
		return phaseName;
	}

	public Phase phaseName(String phaseName) {
		this.phaseName = phaseName;
		return this;
	}

	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}

	public String getPhaseDescription() {
		return phaseDescription;
	}

	public Phase phaseDescription(String phaseDescription) {
		this.phaseDescription = phaseDescription;
		return this;
	}

	public void setPhaseDescription(String phaseDescription) {
		this.phaseDescription = phaseDescription;
	}

	public Project getProject() {
		return project;
	}

	public Phase project(Project project) {
		this.project = project;
		return this;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<Dashboard> getDashboards() {
		return dashboards;
	}

	public Phase dashboards(Set<Dashboard> dashboards) {
		this.dashboards = dashboards;
		return this;
	}

	public Phase addDashboards(Dashboard dashboard) {
		dashboards.add(dashboard);
		dashboard.setPhase(this);
		return this;
	}

	public Phase removeDashboards(Dashboard dashboard) {
		dashboards.remove(dashboard);
		dashboard.setPhase(null);
		return this;
	}

	public void setDashboards(Set<Dashboard> dashboards) {
		this.dashboards = dashboards;
	}

	public Set<MeasureView> getViews() {
		return views;
	}

	public Phase views(Set<MeasureView> measureViews) {
		this.views = measureViews;
		return this;
	}

	public Phase addViews(MeasureView measureView) {
		views.add(measureView);
		measureView.setPhase(this);
		return this;
	}
	
	public Phase removeViews(MeasureView measureView) {
		views.remove(measureView);
		measureView.setPhase(null);
		return this;
	}
	
	public void setViews(Set<MeasureView> measureViews) {
		this.views = measureViews;
	}

	public Set<MeasureView> getOverviews() {
		return overviews;
	}
	
	public Phase overviews(Set<MeasureView> measureViews) {
		this.overviews = measureViews;
		return this;
	}

	public Phase addOverviews(MeasureView measureView) {
		overviews.add(measureView);
		measureView.setPhaseoverview(this);
		return this;
	}
	
	public Phase removeOverviews(MeasureView measureView) {
		overviews.remove(measureView);
		measureView.setPhaseoverview(null);
		return this;
	}

	public void setOverviews(Set<MeasureView> measureViews) {
		this.overviews = measureViews;
	}



	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Phase phase = (Phase) o;
		if (phase.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, phase.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "Phase{" + "id=" + id + ", phaseName='" + phaseName + "'" + ", phaseDescription='" + phaseDescription
				+ "'}";
	}
}
