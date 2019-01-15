package org.measure.platform.core.entity;

import java.io.Serializable;
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
@Table(name = "application")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Application implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
      
    @NotNull
    @Column(name = "application_type", nullable = false)
    private String applicationType;
    
    
    @NotNull
    @Column(name = "application_name", nullable = false)
    private String name;
  
  
    @NotNull
    @Column(name = "application_description", nullable = false)
    private String description;
    
    @Column(name = "enable")
    private Boolean enable;
    
    @ManyToOne
    private Project project;
    
    @OneToMany(mappedBy = "application")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MeasureInstance> instances = new HashSet<>();
    
    @OneToMany(mappedBy = "application")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Dashboard> dashboards = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getApplicationType() {
        return applicationType;
    }

    public Application applicationType(String applicationType) {
        this.applicationType = applicationType;
        return this;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }    
    
    public String getName() {
        return name;
    }

    public Application name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public String getDescription() {
        return description;
    }

    public Application description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEnable() {
        return enable;
    }

    public Application enable(Boolean enable) {
        this.enable = enable;
        return this;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
    
    public Project getProject() {
        return project;
    }

    public Application project(Project project) {
        this.project = project;
        return this;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Application instances(Set<MeasureInstance> measureInstances) {
        this.instances = measureInstances;
        return this;
    }

    public Application addInstances(MeasureInstance measureInstance) {
        instances.add(measureInstance);
        measureInstance.setApplication(this);
        return this;
    }

    public Application removeInstances(MeasureInstance measureInstance) {
        instances.remove(measureInstance);
        measureInstance.setApplication(null);
        return this;
    }
    

    public void setInstances(Set<MeasureInstance> measureInstances) {
        this.instances = measureInstances;
    }
    
    
    public Set<Dashboard> getDashboards() {
        return dashboards;
    }

    public Application dashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
        return this;
    }

    public Application addDashboards(Dashboard dashboard) {
        dashboards.add(dashboard);
        dashboard.setApplication(this);
        return this;
    }

    public Application removeDashboards(Dashboard dashboard) {
        dashboards.remove(dashboard);
        dashboard.setApplication(null);
        return this;
    }

    public void setDashboards(Set<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }
}
    
