package org.measure.platform.core.data.api.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.measure.platform.core.data.entity.Application;
import org.measure.platform.core.data.entity.Dashboard;
import org.measure.platform.core.data.entity.MeasureView;
import org.measure.platform.core.data.entity.Project;
import org.measure.platform.utils.domain.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DashboardDTO {

	private Long id;
    private String dashboardName;
    private String dashboardDescription;
    private String mode;
    private String kibanaId;
    private String content;
    private Boolean auto;
    private Boolean editable;
    private String size;
    private String timePeriode;
    private Project project;
    private Application application;
    private Set<MeasureView> views = new HashSet<>();
    private Set<User> users = new HashSet<>();
    private Set<Long> inviters = new HashSet<>();
    private Boolean hasEditionRole;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDashboardName() {
		return dashboardName;
	}

	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}

	public String getDashboardDescription() {
		return dashboardDescription;
	}

	public void setDashboardDescription(String dashboardDescription) {
		this.dashboardDescription = dashboardDescription;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getKibanaId() {
		return kibanaId;
	}

	public void setKibanaId(String kibanaId) {
		this.kibanaId = kibanaId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean isAuto() {
        return auto;
    }

    public DashboardDTO auto(Boolean auto) {
        this.auto = auto;
        return this;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }
    
    public Boolean isEditable() {
        return editable;
    }

    public DashboardDTO editable(Boolean editable) {
        this.editable = editable;
        return this;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }
    
    public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getTimePeriode() {
		return timePeriode;
	}

	public void setTimePeriode(String timePeriode) {
		this.timePeriode = timePeriode;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Application getApplication() {
		return application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Set<MeasureView> getViews() {
		return views;
	}

	public void setViews(Set<MeasureView> views) {
		this.views = views;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public Set<Long> getInviters() {
		return inviters;
	}

	public void setInviters(Set<Long> inviters) {
		this.inviters = inviters;
	}

	public Boolean getHasEditionRole() {
		return hasEditionRole;
	}

	public void setHasEditionRole(Boolean hasEditionRole) {
		this.hasEditionRole = hasEditionRole;
	}
	
}
