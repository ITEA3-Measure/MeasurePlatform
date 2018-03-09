package org.measure.platform.core.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "alert_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AlertEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "event_type", nullable = false)
    private String eventType;
    
    @NotNull
    @Column(name = "analysis_tool", nullable = false)
    private String analysisTool;
 
    @ManyToOne
    private Project project;
    
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "alertevent")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AlertEventProperty> alerteventpropertys = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventType() {
		return this.eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
	public String getAnalysisTool() {
		return this.analysisTool;
	}

	public void setAnalysisTool(String analysisTool) {
		this.analysisTool = analysisTool;
	}

	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<AlertEventProperty> getAlerteventpropertys() {
		return alerteventpropertys;
	}

	public void setAlerteventpropertys(Set<AlertEventProperty> alerteventpropertys) {
		this.alerteventpropertys = alerteventpropertys;
	} 
}
