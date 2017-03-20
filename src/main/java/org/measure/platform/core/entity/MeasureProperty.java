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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MeasureProperty.
 */
@Entity
@Table(name = "measure_property")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MeasureProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "property_name", nullable = false)
    private String propertyName;

    @Column(name = "property_value")
    private String propertyValue;

    @ManyToOne
    private MeasureInstance measureInstance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public MeasureProperty propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public MeasureProperty propertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
        return this;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public MeasureInstance getMeasureInstance() {
        return measureInstance;
    }

    public MeasureProperty measureInstance(MeasureInstance measureInstance) {
        this.measureInstance = measureInstance;
        return this;
    }

    public void setMeasureInstance(MeasureInstance measureInstance) {
        this.measureInstance = measureInstance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MeasureProperty measureProperty = (MeasureProperty) o;
        if(measureProperty.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, measureProperty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MeasureProperty{" +
            "id=" + id +
            ", propertyName='" + propertyName + "'" +
            ", propertyValue='" + propertyValue + "'" +
            '}';
    }
}
