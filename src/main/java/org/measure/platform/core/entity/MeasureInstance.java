package org.measure.platform.core.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.measure.platform.core.api.entitys.enumeration.MeasureType;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A MeasureInstance.
 */
@Entity
@Table(name = "measure_instance")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MeasureInstance implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "instance_name", nullable = false)
	private String instanceName;

	@Column(name = "instance_description")
	private String instanceDescription;

	@NotNull
	@Column(name = "measure_name", nullable = false)
	private String measureName;

	@NotNull
	@Column(name = "measure_version", nullable = false)
	private String measureVersion;

	@Column(name = "is_shedule")
	private Boolean isShedule;

	@Column(name = "sheduling_expression")
	private String shedulingExpression;

	@Enumerated(EnumType.STRING)
	@Column(name = "measure_type")
	private MeasureType measureType;

	@Column(name = "manage_last_measurement")
	private Boolean manageLastMeasurement;
	
	@Column(name = "remote_adress")
	private String remoteAdress;
	
	@Column(name = "remote_label")
	private String remoteLabel;
	
	@Column(name = "is_remote")
	private Boolean isRemote;

	@ManyToOne
	private Project project;

	@OneToMany(mappedBy = "measureInstance")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<MeasureProperty> properties = new HashSet<>();

	@OneToMany(mappedBy = "ownerInstance")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<MeasureReference> ownedReferences = new HashSet<>();

	@OneToMany(mappedBy = "referencedInstance")
	@JsonIgnore
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private Set<MeasureReference> referenceInstances = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	public String getInstanceName() {
		return instanceName;
	}

	public MeasureInstance instanceName(String instanceName) {
		this.instanceName = instanceName;
		return this;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getInstanceDescription() {
		return instanceDescription;
	}

	public MeasureInstance instanceDescription(String instanceDescription) {
		this.instanceDescription = instanceDescription;
		return this;
	}

	public void setInstanceDescription(String instanceDescription) {
		this.instanceDescription = instanceDescription;
	}

	public String getMeasureName() {
		return measureName;
	}

	public MeasureInstance measureName(String measureName) {
		this.measureName = measureName;
		return this;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	public String getMeasureVersion() {
		return measureVersion;
	}

	public MeasureInstance measureVersion(String measureVersion) {
		this.measureVersion = measureVersion;
		return this;
	}

	public void setMeasureVersion(String measureVersion) {
		this.measureVersion = measureVersion;
	}

	public Boolean isIsShedule() {
		return isShedule;
	}

	public MeasureInstance isShedule(Boolean isShedule) {
		this.isShedule = isShedule;
		return this;
	}

	public void setIsShedule(Boolean isShedule) {
		this.isShedule = isShedule;
	}
	
	
	public Boolean isIsRemote() {
		return isRemote;
	}

	public MeasureInstance isRemote(Boolean isRemote) {
		this.isRemote = isRemote;
		return this;
	}

	public void setIsRemote(Boolean isRemote) {
		this.isRemote = isRemote;
	}
	
	public String getRemoteAdress() {
		return remoteAdress;
	}

	public MeasureInstance remoteAdress(String remoteAdress) {
		this.remoteAdress = remoteAdress;
		return this;
	}

	public void setRemoteAdress(String remoteIp) {
		this.remoteAdress = remoteIp;
	}
	
	
	public String getRemoteLabel() {
		return remoteLabel;
	}

	public MeasureInstance remoteLabel(String remoteLabel) {
		this.remoteLabel = remoteLabel;
		return this;
	}

	public void setRemoteLabel(String remoteLabel) {
		this.remoteLabel = remoteLabel;
	}

	public String getShedulingExpression() {
		return shedulingExpression;
	}

	public MeasureInstance shedulingExpression(String shedulingExpression) {
		this.shedulingExpression = shedulingExpression;
		return this;
	}

	public void setShedulingExpression(String shedulingExpression) {
		this.shedulingExpression = shedulingExpression;
	}

	public MeasureType getMeasureType() {
		return measureType;
	}

	public MeasureInstance measureType(MeasureType measureType) {
		this.measureType = measureType;
		return this;
	}

	public void setMeasureType(MeasureType measureType) {
		this.measureType = measureType;
	}

	public Boolean isManageLastMeasurement() {
		return manageLastMeasurement;
	}

	public MeasureInstance manageLastMeasurement(Boolean manageLastMeasurement) {
		this.manageLastMeasurement = manageLastMeasurement;
		return this;
	}

	public void setManageLastMeasurement(Boolean manageLastMeasurement) {
		this.manageLastMeasurement = manageLastMeasurement;
	}

	public Project getProject() {
		return project;
	}

	public MeasureInstance project(Project project) {
		this.project = project;
		return this;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<MeasureProperty> getProperties() {
		return properties;
	}

	public MeasureInstance properties(Set<MeasureProperty> measureProperties) {
		this.properties = measureProperties;
		return this;
	}

	public MeasureInstance addProperties(MeasureProperty measureProperty) {
		properties.add(measureProperty);
		measureProperty.setMeasureInstance(this);
		return this;
	}

	public MeasureInstance removeProperties(MeasureProperty measureProperty) {
		properties.remove(measureProperty);
		measureProperty.setMeasureInstance(null);
		return this;
	}

	public void setProperties(Set<MeasureProperty> measureProperties) {
		this.properties = measureProperties;
	}
	
	public MeasureInstance ownedReferences(Set<MeasureReference> ownedReferences) {
		this.ownedReferences = ownedReferences;
		return this;
	}

	public MeasureInstance addOwnedReferences(MeasureReference ownedReference) {
		ownedReferences.add(ownedReference);
		ownedReference.setOwnerInstance(this);
		return this;
	}

	public MeasureInstance removeOwnedReferences(MeasureReference ownedReference) {
		ownedReferences.remove(ownedReference);
		ownedReference.setOwnerInstance(null);
		return this;
	}

	public void setOwnedReferences(Set<MeasureReference> ownedReferences) {
		this.ownedReferences = ownedReferences;
	}
	
	
	
	public Set<MeasureReference> getOwnedReferences() {
		return this.ownedReferences;
	}

	public MeasureInstance referenceInstances(Set<MeasureReference> referenceInstances) {
		this.referenceInstances = referenceInstances;
		return this;
	}

	public MeasureInstance addReferenceInstances(MeasureReference referenceInstance) {
		ownedReferences.add(referenceInstance);
		referenceInstance.setReferencedInstance(this);
		return this;
	}

	public MeasureInstance removeReferenceInstances(MeasureReference referenceInstance) {
		referenceInstances.remove(referenceInstance);
		referenceInstance.setReferencedInstance(null);
		return this;
	}

	public void setReferenceInstances(Set<MeasureReference> referenceInstances) {
		this.referenceInstances = referenceInstances;
	}
	
	public Set<MeasureReference> getReferenceInstances() {
		return this.referenceInstances;
	}

	// public MeasureInstance getMeasureInstance() {
	// return measureInstance;
	// }
	//
	// public MeasureInstance measureInstance(MeasureInstance measureInstance) {
	// this.measureInstance = measureInstance;
	// return this;
	// }
	//
	// public void setMeasureInstance(MeasureInstance measureInstance) {
	// this.measureInstance = measureInstance;
	// }
	//
	// public Set<MeasureInstance> getDepends() {
	// return depends;
	// }
	//
	// public MeasureInstance depends(Set<MeasureInstance> measureInstances) {
	// this.depends = measureInstances;
	// return this;
	// }
	//
	// public MeasureInstance addDepends(MeasureInstance measureInstance) {
	// depends.add(measureInstance);
	// measureInstance.setMeasureInstance(this);
	// return this;
	// }
	//
	// public MeasureInstance removeDepends(MeasureInstance measureInstance) {
	// depends.remove(measureInstance);
	// measureInstance.setMeasureInstance(null);
	// return this;
	// }
	//
	// public void setDepends(Set<MeasureInstance> measureInstances) {
	// this.depends = measureInstances;
	// }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MeasureInstance measureInstance = (MeasureInstance) o;
		if (measureInstance.id == null || id == null) {
			return false;
		}
		return Objects.equals(id, measureInstance.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public String toString() {
		return "MeasureInstance{" + "id=" + id + ", instanceName='" + instanceName + "'" + ", instanceDescription='"
				+ instanceDescription + "'" + ", measureName='" + measureName + "'" + ", measureVersion='"
				+ measureVersion + "'" + ", isShedule='" + isShedule + "'" + ", shedulingExpression='"
				+ shedulingExpression + "'" + ", measureType='" + measureType + "'" + ", manageLastMeasurement='"
				+ manageLastMeasurement + "'" + '}';
	}
}
