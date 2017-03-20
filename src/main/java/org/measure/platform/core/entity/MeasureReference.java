package org.measure.platform.core.entity;

import java.io.Serializable;

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
 * A MeasureProperty.
 */
@Entity
@Table(name = "measure_reference")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MeasureReference implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Integer numberRef;
	
	private String role;
		
	@Column(name = "filter_expression")
	private String filterExpression;

	@ManyToOne
	private MeasureInstance ownerInstance;
	
	
	@ManyToOne
	private MeasureInstance referencedInstance;
	
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNumberRef() {
        return numberRef;
    }

    public MeasureReference numberRef(Integer numberRef) {
        this.numberRef = numberRef;
        return this;
    }

    public void setNumberRef(Integer numberRef) {
        this.numberRef = numberRef;
    }
    


	public String getRole() {
        return role;
    }

    public MeasureReference role(String role) {
        this.role = role;
        return this;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
	public String getFilterExpression() {
        return filterExpression;
    }

    public MeasureReference filterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
        return this;
    }

    public void setFilterExpression(String filterExpression) {
        this.filterExpression = filterExpression;
    }
    
    public MeasureInstance getOwnerInstance() {
        return ownerInstance;
    }

    public MeasureReference ownerInstance(MeasureInstance ownerInstance) {
        this.ownerInstance = ownerInstance;
        return this;
    }

    public void setOwnerInstance(MeasureInstance ownerInstance) {
        this.ownerInstance = ownerInstance;
    }

    public MeasureInstance getReferencedInstance() {
        return referencedInstance;
    }

    public MeasureReference measureInstance(MeasureInstance referencedInstance) {
        this.referencedInstance = referencedInstance;
        return this;
    }

    public void setReferencedInstance(MeasureInstance referencedInstance) {
        this.referencedInstance = referencedInstance;
    }

	@Override
	public String toString() {
		return "MeasureReference{" + "id=" + id + '}';
	}
}
