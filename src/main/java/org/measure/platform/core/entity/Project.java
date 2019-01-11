package org.measure.platform.core.entity;

import java.io.Serializable;
import java.time.ZonedDateTime;
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
import org.measure.platform.utils.domain.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Project implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "project_name", nullable = false)
    private String projectName;

    @Column(name = "project_description")
    private String projectDescription;

    @Column(name = "project_image")
    private String projectImage;

    @Column(name = "creation_date")
    private ZonedDateTime creationDate;

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dashboard> dashboards = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Notification> notifications = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MeasureInstance> instances = new HashSet<>();

    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MeasureView> views = new HashSet<>();

    @OneToMany(mappedBy = "projectoverview")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MeasureView> overviews = new HashSet<>();
    
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ProjectAnalysis> projectanalysis = new HashSet<>();
    
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AlertEvent> alertevents = new HashSet<>();
    
    
    @OneToMany(mappedBy = "project")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Application> applications = new HashSet<>();
    
    @OneToMany(mappedBy = "associatedProject")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dashboard> dashboards = new HashSet<>();

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public Project projectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public Project projectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
        return this;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Project creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getProjectImage() {
        return projectImage;
    }

    public Project projectImage(String projectImage) {
        this.projectImage = projectImage;
        return this;
    }

    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }

    public Set<Dashboard> getDashboards() {
        return dashboards;
    }

    public Project dashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
        return this;
    }

    public Project addDashboards(Dashboard dashboard) {
        dashboards.add(dashboard);
        dashboard.setProject(this);
        return this;
    }

    public Project removeDashboards(Dashboard dashboard) {
        dashboards.remove(dashboard);
        dashboard.setProject(null);
        return this;
    }

    public void setDashboard(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public Project notifications(Set<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    public Project addNotifications(Notification phase) {
        notifications.add(phase);
        phase.setProject(this);
        return this;
    }

    public Project removeNotifications(Notification notification) {
        notifications.remove(notification);
        notification.setProject(null);
        return this;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public Set<MeasureInstance> getInstances() {
        return instances;
    }

    public Project instances(Set<MeasureInstance> measureInstances) {
        this.instances = measureInstances;
        return this;
    }

    public Project addInstances(MeasureInstance measureInstance) {
        instances.add(measureInstance);
        measureInstance.setProject(this);
        return this;
    }

    public Project removeInstances(MeasureInstance measureInstance) {
        instances.remove(measureInstance);
        measureInstance.setProject(null);
        return this;
    }

    public void setInstances(Set<MeasureInstance> measureInstances) {
        this.instances = measureInstances;
    }

    public Set<Application> getApplications() {
        return applications;
    }

    public Project applications(Set<Application> applications) {
        this.applications = applications;
        return this;
    }

    public Project addApplications(Application application) {
    	applications.add(application);
        application.setProject(this);
        return this;
    }

    public Project removeApplications(Application application) {
    	applications.remove(application);
    	application.setProject(null);
        return this;
    }

    public void setApplications(Set<Application> applications) {
        this.applications = applications;
    }  
    
    public User getOwner() {
        return owner;
    }

    public Project owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Set<MeasureView> getViews() {
        return views;
    }

    public Project views(Set<MeasureView> measureViews) {
        this.views = measureViews;
        return this;
    }

    public Project addViews(MeasureView measureView) {
        views.add(measureView);
        measureView.setProject(this);
        return this;
    }

    public Project removeViews(MeasureView measureView) {
        views.remove(measureView);
        measureView.setProject(null);
        return this;
    }

    public void setViews(Set<MeasureView> measureViews) {
        this.views = measureViews;
    }

    public Set<MeasureView> getOverviews() {
        return overviews;
    }

    public Project overviews(Set<MeasureView> measureViews) {
        this.overviews = measureViews;
        return this;
    }

    public Project addOverviews(MeasureView measureView) {
        overviews.add(measureView);
        measureView.setProjectoverview(this);
        return this;
    }

    public Project removeOverviews(MeasureView measureView) {
        overviews.remove(measureView);
        measureView.setProjectoverview(null);
        return this;
    }

    public void setOverviews(Set<MeasureView> measureViews) {
        this.overviews = measureViews;
    }
    
    
    public Set<ProjectAnalysis> getProjectanalysis() {
		return projectanalysis;
	}

	public void setProjectanalysis(Set<ProjectAnalysis> projectanalysis) {
		this.projectanalysis = projectanalysis;
	}
	
	public Set<AlertEvent> getAlertevents() {
		return alertevents;
	}

	public void setAlertevents(Set<AlertEvent> alertevents) {
		this.alertevents = alertevents;
	}
	
	public Set<Dashboard> getDashboards() {
		return dashboards;
	}

	public void setDashboards(Set<Dashboard> dashboards) {
		this.dashboards = dashboards;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project project = (Project) o;
        if(project.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, project.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Project{" +
                    "id=" + id +
                    ", projectName='" + projectName + "'" +
                    ", projectDescription='" + projectDescription + "'" +
                    ", creationDate='" + creationDate + "'" +
                    ", projectImage='" + projectImage + "'" +
                    '}';
    }

}
