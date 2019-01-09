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

    @Column(name = "mode")
    private String mode;

    @Column(name = "viewname")
    private String name;

    @Column(name = "viewdescription")
    private String description;

    @Column(name = "width")
    private String width;
    
    @Column(name = "height")
    private String height;
    
    @Column(name = "font_size")
    private String fontSize;

    @Column(name = "viewtype")
    private String type;

    @Column(name = "viewauto")
    private Boolean auto;

    @Column(name = "time_periode")
    private String timePeriode;

    @Column(name = "time_agregation")
    private String timeAgregation;

    @Column(name = "view_data")
    private String viewData;

    @Column(name = "kibana_name")
    private String kibanaName;

    @Column(name = "visualised_property")
    private String visualisedProperty = "value";

    @Column(name = "date_index")
    private String dateIndex = "postDate";

    @Column(name = "color")
    private String color = "color";

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
    
    @ManyToOne
    private AnalysisCard analysiscard;


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

    public String getMode() {
        return mode;
    }

    public MeasureView mode(String mode) {
        this.mode = mode;
        return this;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
	public String getFontSize() {
		return fontSize;
	}

	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
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

    public String getKibanaName() {
        return kibanaName;
    }

    public MeasureView kibanaName(String kibanaName) {
        this.kibanaName = kibanaName;
        return this;
    }

    public void setKibanaName(String kibanaName) {
        this.kibanaName = kibanaName;
    }

    public String getColor() {
        return color;
    }

    public MeasureView color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getTimePeriode() {
        return timePeriode;
    }

    public MeasureView timePeriode(String timePeriode) {
        this.timePeriode = timePeriode;
        return this;
    }

    public void setTimePeriode(String timePeriode) {
        this.timePeriode = timePeriode;
    }

    public String getTimeAgregation() {
        return timeAgregation;
    }

    public MeasureView interval(String timeAgregation) {
        this.timeAgregation = timeAgregation;
        return this;
    }

    public void setTimeAgregation(String timeAgregation) {
        this.timeAgregation = timeAgregation;
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

    public String getVisualisedProperty() {
        return visualisedProperty;
    }

    public MeasureView visualisedProperty(String visualisedProperty) {
        this.visualisedProperty = visualisedProperty;
        return this;
    }

    public void setVisualisedProperty(String visualisedProperty) {
        this.visualisedProperty = visualisedProperty;
    }

    public String getDateIndex() {
        return dateIndex;
    }

    public MeasureView dateIndex(String dateIndex) {
        this.dateIndex = dateIndex;
        return this;
    }

    public void setDateIndex(String dateIndex) {
        this.dateIndex = dateIndex;
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
    
    
    public AnalysisCard getAnalysiscard() {
		return analysiscard;
	}

	public void setAnalysiscard(AnalysisCard analysiscard) {
		this.analysiscard = analysiscard;
	}

	public Boolean getAuto() {
		return auto;
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
