package org.measure.platform.service.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.measure.platform.core.api.IApplicationCatalogueService;
import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.core.api.entitys.ApplicationInstanceService;
import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.enumeration.MeasureType;
import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.service.application.api.IApplicationInstanceConfigurationService;
import org.measure.platform.service.application.impl.dto.ApplicationInstanceConfiguration;
import org.measure.platform.service.application.impl.dto.ApplicationProperty;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyEnum;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyEnumValue;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyType;
import org.measure.platform.service.smmengine.api.ISchedulingService;
import org.measure.smm.application.model.ApplicationMeasure;
import org.measure.smm.application.model.SMMApplication;
import org.measure.smm.measure.model.DataSource;
import org.measure.smm.measure.model.Layout;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.measure.model.ScopeProperty;
import org.measure.smm.measure.model.ScopePropertyEnumValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApplicationInstanceConfigurationServiceImpl implements IApplicationInstanceConfigurationService {
	private final Logger log = LoggerFactory.getLogger(ApplicationInstanceConfigurationServiceImpl.class);

	@Inject
	private IApplicationCatalogueService applicationCatalogue;

	@Inject
	private IMeasureCatalogueService measureCatalogue;

	@Inject
	private ApplicationInstanceService applicationInstanceService;



	@Override
	public ApplicationInstanceConfiguration createApplicaionInstance(ApplicationInstanceConfiguration applicationConfiguration) {

		Application application = createApplication(applicationConfiguration);
		SMMApplication applicationResource = applicationCatalogue.getApplication(application.getApplicationType());

		if (applicationResource.getMeasures() != null) {
			for (ApplicationMeasure applicationMeasure : applicationResource.getMeasures().getMeasure()) {
				MeasureInstance measureInstance = createMeasureInstance(applicationMeasure, applicationConfiguration);
				measureInstance.setApplication(application);
				measureInstance.setProject(application.getProject());
				application.addInstances(measureInstance);
			}
		}

		applicationInstanceService.save(application);

		return getConfigurationFromApplication(application);

	}

	private Application createApplication(ApplicationInstanceConfiguration applicationConfiguration) {
		Application application = new Application();
		application.setEnable(applicationConfiguration.getIsEnable());
		application.setApplicationType(applicationConfiguration.getApplicationType());
		application.setName(applicationConfiguration.getName());
		application.setDescription("Description field is deprecated !");
		application.setProject(applicationConfiguration.getProject());
		return application;
	}

	private ApplicationInstanceConfiguration getConfigurationFromApplication(Application application) {
		if (application == null) {
			return null;
		}
		ApplicationInstanceConfiguration applicationInstanceConfiguration = new ApplicationInstanceConfiguration();
		applicationInstanceConfiguration.setIsEnable(application.isEnable());
		applicationInstanceConfiguration.setId(application.getId());
		applicationInstanceConfiguration.setApplicationType(application.getApplicationType());
		applicationInstanceConfiguration.setName(application.getName());
		applicationInstanceConfiguration.setProject(application.getProject());
		return applicationInstanceConfiguration;
	}

	private MeasureInstance createMeasureInstance(ApplicationMeasure measureDescriptor,ApplicationInstanceConfiguration applicationDescription) {
		SMMMeasure measure = measureCatalogue.getMeasure(applicationDescription.getApplicationType(),measureDescriptor.getName());

		MeasureInstance measureInstance = new MeasureInstance();
		measureInstance.setInstanceName(applicationDescription.getName() + "_" + measure.getName());
		measureInstance.setMeasureName(measure.getName());
		measureInstance.setMeasureVersion("1.0.0");
		measureInstance.setIsRemote(false);
		measureInstance.setIsShedule(false);

		switch (measure.getType()) {
		case BINARY:
			measureInstance.setMeasureType(MeasureType.BINARY);
			break;
		case COLLECTIVE:
			measureInstance.setMeasureType(MeasureType.COLLECTIVE);
			break;
		case COUNTING:
			measureInstance.setMeasureType(MeasureType.COUNTING);
			break;
		case DIRECT:
			measureInstance.setMeasureType(MeasureType.DIRECT);
			break;
		case GRADE:
			measureInstance.setMeasureType(MeasureType.GRADE);
			break;
		case RACKING:
			measureInstance.setMeasureType(MeasureType.RACKING);
			break;
		case RATIO:
			measureInstance.setMeasureType(MeasureType.RATIO);
			break;
		case RESCALED:
			measureInstance.setMeasureType(MeasureType.RESCALED);
			break;
		}

	 
		if (measureDescriptor.getScheduling() != null) {
	
			if("s".equals(measureDescriptor.getSchedulingUnit())){
				measureInstance.setShedulingExpression(String.valueOf(Integer.valueOf(measureDescriptor.getScheduling()) * 1000 ));
			}else if("m".equals(measureDescriptor.getSchedulingUnit())){
				measureInstance.setShedulingExpression(String.valueOf(Integer.valueOf(measureDescriptor.getScheduling()) * 60000 ));
			}else if("h".equals(measureDescriptor.getSchedulingUnit())){
				measureInstance.setShedulingExpression(String.valueOf(Integer.valueOf(measureDescriptor.getScheduling()) * 3600000 ));
			}else if("d".equals(measureDescriptor.getSchedulingUnit())){
				measureInstance.setShedulingExpression(String.valueOf(Integer.valueOf(measureDescriptor.getScheduling()) * 86400000 ));
			}
		} else {
			measureInstance.setShedulingExpression("1");
			measureInstance.setSchedulingUnit("h");
		}

		for (ScopeProperty scopeProperty : measure.getScopeProperties()) {
			MeasureProperty measureProperty = new MeasureProperty();
			measureProperty.setPropertyName(scopeProperty.getName());
			measureProperty.setPropertyType(scopeProperty.getType().toString());
			measureProperty.setMeasureInstance(measureInstance);
			measureProperty.setPropertyValue(applicationDescription.getPropertyValue(scopeProperty.getName()));
			measureInstance.addProperties(measureProperty);
			measureProperty.setMeasureInstance(measureInstance);
		}

		return measureInstance;
	}

	@Override
	public ApplicationInstanceConfiguration updateApplicaionInstance(ApplicationInstanceConfiguration applicationConfiguration) {

		Application application = createApplication(applicationConfiguration);
		ApplicationInstanceConfiguration resultApplicationInstanceConfiguration = getConfigurationFromApplication(applicationInstanceService.save(application));

		return resultApplicationInstanceConfiguration;

	}

	@Override
	public void deleteApplicaionInstance(Long id) {
		applicationInstanceService.delete(id);
	}

	@Override
	public ApplicationInstanceConfiguration getApplicaionInstanceById(Long id) {
		return getConfigurationFromApplication(applicationInstanceService.findOne(id));
	}

	@Override
	public ApplicationInstanceConfiguration getApplicaionInstanceByApplication(String applicationType) {
		ApplicationInstanceConfiguration applicationInstanceConfiguration = new ApplicationInstanceConfiguration();
		applicationInstanceConfiguration.setApplicationType(applicationType);

		SMMApplication applicationResource = applicationCatalogue.getApplication(applicationType);
		if (applicationResource.getMeasures() != null) {

			Map<String, ScopeProperty> mapPropertyNameScopeProperty = new HashMap<>();
			for (ApplicationMeasure applicationMeasure : applicationResource.getMeasures().getMeasure()) {
				SMMMeasure measure = measureCatalogue.getMeasure(applicationType, applicationMeasure.getName());
				for (ScopeProperty scopeProperty : measure.getScopeProperties()) {
					if (!mapPropertyNameScopeProperty.containsKey(scopeProperty.getName())) {
						mapPropertyNameScopeProperty.put(scopeProperty.getName(), scopeProperty);
					}
				}
			}

			List<ApplicationProperty> properties = new ArrayList<ApplicationProperty>();
			for (ScopeProperty scopeProperty : mapPropertyNameScopeProperty.values()) {
				ApplicationProperty applicationProperty = new ApplicationProperty();
				applicationProperty.setDefaultValue(scopeProperty.getDefaultValue());
				applicationProperty.setName(scopeProperty.getName());
				setApplicationPropertyType(applicationProperty, scopeProperty);

				properties.add(applicationProperty);
			}
			applicationInstanceConfiguration.setProperties(properties);

		}

		return applicationInstanceConfiguration;
	}

	private void setApplicationPropertyType(ApplicationProperty applicationProperty, ScopeProperty scopeProperty) {
		switch (scopeProperty.getType()) {
		case DATE:
			applicationProperty.setType(ApplicationPropertyType.DATE);
			break;
		case FLOAT:
			applicationProperty.setType(ApplicationPropertyType.FLOAT);
			break;
		case STRING:
			applicationProperty.setType(ApplicationPropertyType.STRING);
			break;
		case INTEGER:
			applicationProperty.setType(ApplicationPropertyType.INTEGER);
			break;
		case PASSWORD:
			applicationProperty.setType(ApplicationPropertyType.PASSWORD);
			break;
		case DESABLE:
			applicationProperty.setType(ApplicationPropertyType.DESABLE);
			break;
		case ENUM:
			applicationProperty.setType(ApplicationPropertyType.ENUM);

			List<ApplicationPropertyEnumValue> lisApplicationPropertyEnumValue = new ArrayList<ApplicationPropertyEnumValue>();
			for (ScopePropertyEnumValue scopePropertyEnumValue : scopeProperty.getEnumType().getEnumvalue()) {
				ApplicationPropertyEnumValue applicationPropertyEnumValue = new ApplicationPropertyEnumValue();
				applicationPropertyEnumValue.setLabel(scopePropertyEnumValue.getLabel());
				applicationPropertyEnumValue.setValue(scopePropertyEnumValue.getValue());
				lisApplicationPropertyEnumValue.add(applicationPropertyEnumValue);
			}
			ApplicationPropertyEnum enumType = new ApplicationPropertyEnum();
			enumType.setEnumvalue(lisApplicationPropertyEnumValue);
			applicationProperty.setEnumType(enumType);

			break;
		}
	}



}
