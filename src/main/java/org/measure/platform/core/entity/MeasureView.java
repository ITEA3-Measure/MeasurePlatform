package org.measure.platform.core.entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MeasureView.
 */
@Entity
@Table(name = "measure_view")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MeasureView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
      
    @Column(name = "viewcustom")
    private Boolean custom;
    
    @Column(name = "viewname")
    private String name;
    
    @Column(name = "viewdescription")
    private String description;
    
    @Column(name = "viewsize")
    private String size;
    
    @Column(name = "viewtype")
    private String type;
    
    @Column(name = "viewauto")
    private Boolean auto;
    
    @Column(name = "viewinterval")
    private String interval;
    
    @Column(name = "view_data")
    private String viewData;  
 
    @ManyToOne
    private Project projectoverview;
    
    
    @ManyToOne
    private Project project;

    @ManyToOne
    private Phase phaseoverview;
    
    
    @ManyToOne
    private Phase phase;

    @ManyToOne
    private Dashboard dashboard;
    
    @ManyToOne
    private MeasureInstance measureinstance;
         
    public Boolean isCustom() {
        return custom;
    }

	public MeasureView custom(Boolean custom) {
        this.custom = custom;
        return this;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }
    
    public String getName() {
        return name;
    }

	public MeasureView name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }
    

    public String getDescription() {
        return description;
    }

	public MeasureView description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getSize() {
        return size;
    }

	public MeasureView size(String size) {
        this.size = size;
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }
    
    
    public String getType() {
        return type;
    }

	public MeasureView type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
    public Boolean isAuto() {
        return auto;
    }

	public MeasureView auto(Boolean auto) {
        this.auto = auto;
        return this;
    }

    public void setAuto(Boolean auto) {
        this.auto = auto;
    }
    
    public String getInterval() {
        return interval;
    }

	public MeasureView interval(String interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    
    public MeasureView project(Project project) {
        this.project = project;
        return this;
    }
    
    public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
    public String getViewData() {
        return viewData;
    }

	public MeasureView viewData(String viewData) {
        this.viewData = viewData;
        return this;
    }

    public void setViewData(String viewData) {
        this.viewData = viewData;
    }
    
    public MeasureView phase(Phase phase) {
        this.phase = phase;
        return this;
    }
    
    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    
    public MeasureInstance getMeasureinstance() {
        return measureinstance;
    }

    public MeasureView measureInstance(MeasureInstance measureinstance) {
        this.measureinstance = measureinstance;
        return this;
    }

    public void setMeasureinstance(MeasureInstance measureinstance) {
        this.measureinstance = measureinstance;
    }

    
    public Dashboard getDashboard() {
        return dashboard;
    }

    public MeasureView dashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
        return this;
    }

    public void setDashboard(Dashboard dashboard) {
        this.dashboard = dashboard;
    }
    
    public MeasureView projectoverview(Project project) {
        this.projectoverview = project;
        return this;
    }
    
    public Project getProjectoverview() {
		return projectoverview;
	}

	public void setProjectoverview(Project projectoverview) {
		this.projectoverview = projectoverview;
	}
	
   public MeasureView phaseoverview(Phase phase) {
	    this.phaseoverview = phase;
	    return this;
	}
	   
	public Phase getPhaseoverview() {
		return phaseoverview;
	}

	public void setPhaseoverview(Phase phaseOverview) {
		this.phaseoverview = phaseOverview;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeasureView measureView = (MeasureView) o;
        if(measureView.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, measureView.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MeasureView{" +
            "id=" + id +
            ", viewData='" + viewData + "'" +
            '}';
    }
}
