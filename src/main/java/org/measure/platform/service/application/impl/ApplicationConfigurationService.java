package org.measure.platform.service.application.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.measure.platform.core.catalogue.api.IApplicationCatalogueService;
import org.measure.platform.core.catalogue.api.IMeasureCatalogueService;
import org.measure.platform.core.data.api.IApplicationService;
import org.measure.platform.core.data.api.IMeasureInstanceService;
import org.measure.platform.core.data.api.IMeasurePropertyService;
import org.measure.platform.core.data.api.enumeration.MeasureType;
import org.measure.platform.core.data.entity.Application;
import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.core.data.entity.MeasureProperty;
import org.measure.platform.service.application.api.IApplicationConfigurationService;
import org.measure.platform.service.application.impl.dto.ApplicationConfiguration;
import org.measure.platform.service.application.impl.dto.ApplicationProperty;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyEnum;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyEnumValue;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyType;
import org.measure.smm.application.model.ApplicationMeasure;
import org.measure.smm.application.model.SMMApplication;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.measure.model.ScopeProperty;
import org.measure.smm.measure.model.ScopePropertyEnumValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ApplicationConfigurationService implements IApplicationConfigurationService {
	private final Logger log = LoggerFactory.getLogger(ApplicationConfigurationService.class);

	@Inject
	private IApplicationCatalogueService applicationCatalogue;

	@Inject
	private IMeasureCatalogueService measureCatalogue;

	@Inject
	private IApplicationService applicationService;
	
	@Inject
	private IMeasurePropertyService measurePropertyService;
	
	@Inject
	private IMeasureInstanceService measureService;
	
	@Override
	public ApplicationConfiguration createApplication(ApplicationConfiguration applicationConfiguration) {

		Application application = new Application();
		application.setEnable(applicationConfiguration.getIsEnable());
		application.setApplicationType(applicationConfiguration.getApplicationType());
		application.setName(applicationConfiguration.getName());
		application.setDescription("Description field is deprecated !");
		application.setProject(applicationConfiguration.getProject());
		
		SMMApplication applicationResource = applicationCatalogue.getApplication(application.getApplicationType());

		application = applicationService.save(application);
		applicationConfiguration.setId(application.getId());
		
		if (applicationResource.getMeasures() != null) {			
			for (ApplicationMeasure applicationMeasure : applicationResource.getMeasures().getMeasure()) {
				MeasureInstance measureInstance = createMeasureInstance(applicationMeasure, applicationConfiguration);
				measureInstance.setApplication(application);
				measureInstance.setProject(application.getProject());
				application.addInstances(measureInstance);
				measureService.save(measureInstance);
			}
		}

		

		return applicationConfiguration;

	}


	

	private MeasureInstance createMeasureInstance(ApplicationMeasure measureDescriptor,ApplicationConfiguration applicationDescription) {
		SMMMeasure measure = measureCatalogue.getMeasure(applicationDescription.getApplicationType(),measureDescriptor.getName());

		MeasureInstance measureInstance = new MeasureInstance();
		measureInstance.setInstanceName(formatMeasureName(applicationDescription,measure));
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

	private String formatMeasureName(ApplicationConfiguration applicationDescription, SMMMeasure measure) {
		return applicationDescription.getName().replaceAll(" ","") + "_"+measure.getName();
	}

	@Override
	public ApplicationConfiguration updateApplication(ApplicationConfiguration applicationConfiguration) {
		Application application = applicationService.findOne(applicationConfiguration.getId());
		for (MeasureInstance instance : application.getInstances()) {
			for (MeasureProperty property : instance.getProperties()) {
				property.setPropertyValue(applicationConfiguration.getPropertyValue(property.getPropertyName()));
				measurePropertyService.save(property);
			}
		}

		return applicationConfiguration;
	}

	@Override
	public void deleteApplicaionInstance(Long id) {
		applicationService.delete(id);
	}

	@Override
	public ApplicationConfiguration getConfiguration(Long id) {
		Application application = applicationService.findOne(id);
		
		// Get MeasureInstance Configuration form Application
		ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration();
		applicationConfiguration.setIsEnable(application.isEnable());
		applicationConfiguration.setId(application.getId());
		applicationConfiguration.setApplicationType(application.getApplicationType());
		applicationConfiguration.setName(application.getName());
		applicationConfiguration.setProject(application.getProject());
		

		Map<String,ApplicationProperty> properties = new HashMap<>();
		for(MeasureInstance instance : application.getInstances()) {			
			SMMMeasure measureConf = measureCatalogue.getMeasure(application.getApplicationType(), instance.getMeasureName());	
			for(MeasureProperty property : instance.getProperties()) {
				if(!properties.containsKey(property.getPropertyName())){
					ApplicationProperty applicationProperty = new ApplicationProperty();
					applicationProperty.setValue(property.getPropertyValue());
					applicationProperty.setName(property.getPropertyName());	
					initialiseApplicationPropertyType(applicationProperty, measureConf.findPropertyByName(property.getPropertyName()));
					properties.put(property.getPropertyName(),applicationProperty);
				}		
			}	
		}
		
		applicationConfiguration.setProperties(new ArrayList<>(properties.values()));
		
		return applicationConfiguration;
	}

	@Override
	public ApplicationConfiguration getConfigurationByType(String applicationType) {
		ApplicationConfiguration configuration = new ApplicationConfiguration();
		configuration.setApplicationType(applicationType);

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
				applicationProperty.setValue(scopeProperty.getDefaultValue());
				applicationProperty.setName(scopeProperty.getName());
				initialiseApplicationPropertyType(applicationProperty, scopeProperty);
				properties.add(applicationProperty);
			}
			configuration.setProperties(properties);
		}

		return configuration;
	}

	private void initialiseApplicationPropertyType(ApplicationProperty applicationProperty, ScopeProperty scopeProperty) {
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
