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
 * A Dashboard.
 */
@Entity
@Table(name = "dashboard")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dashboard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "dashboard_name", nullable = false)
    private String dashboardName;

    @Column(name = "dashboard_description")
    private String dashboardDescription;
    
    
    @Column(name = "dashboard_isexternal")
    private Boolean isExternal;
   
    @Column(name = "dashboard_content")
    private String content;

    @ManyToOne
    private Phase phase;

    @OneToMany(mappedBy = "dashboard")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MeasureView> views = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDashboardName() {
        return dashboardName;
    }

    public Dashboard dashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
        return this;
    }

    public void setDashboardName(String dashboardName) {
        this.dashboardName = dashboardName;
    }

    public String getDashboardDescription() {
        return dashboardDescription;
    }

    public Dashboard dashboardDescription(String dashboardDescription) {
        this.dashboardDescription = dashboardDescription;
        return this;
    }

    public void setDashboardDescription(String dashboardDescription) {
        this.dashboardDescription = dashboardDescription;
    }
    
    
    

    public Boolean getIsExternal() {
		return isExternal;
	}

	public void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Phase getPhase() {
        return phase;
    }

    public Dashboard phase(Phase phase) {
        this.phase = phase;
        return this;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public Set<MeasureView> getViews() {
        return views;
    }

    public Dashboard views(Set<MeasureView> measureViews) {
        this.views = measureViews;
        return this;
    }

    public Dashboard addViews(MeasureView measureView) {
        views.add(measureView);
        measureView.setDashboard(this);
        return this;
    }

    public Dashboard removeViews(MeasureView measureView) {
        views.remove(measureView);
        measureView.setDashboard(null);
        return this;
    }

    public void setViews(Set<MeasureView> measureViews) {
        this.views = measureViews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dashboard dashboard = (Dashboard) o;
        if(dashboard.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, dashboard.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Dashboard{" +
            "id=" + id +
            ", dashboardName='" + dashboardName + "'" +
            ", dashboardDescription='" + dashboardDescription + "'" +
            '}';
    }
}
