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
import org.measure.platform.core.api.entitys.enumeration.MeasureType;
import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;
import org.measure.platform.service.application.api.IApplicationInstanceConfigurationService;
import org.measure.platform.service.application.impl.dto.ApplicationInstanceConfiguration;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyType;
import org.measure.platform.service.application.impl.dto.ApplicationProperty;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyEnum;
import org.measure.platform.service.application.impl.dto.ApplicationPropertyEnumValue;
import org.measure.smm.application.model.ApplicationMeasure;
import org.measure.smm.application.model.SMMApplication;
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
	public ApplicationInstanceConfiguration createApplicaionInstance(
			ApplicationInstanceConfiguration applicationConfiguration) {
		Application application = getApplicationFromConfiguration(applicationConfiguration);

		SMMApplication applicationResource = applicationCatalogue.getApplication(application.getApplicationType());
		if(applicationResource.getMeasures() != null) {
			

			
			Set<MeasureInstance> measureInstances = new HashSet<MeasureInstance>();

			fillInMeasuresInstancesFields(applicationConfiguration, application, applicationResource, measureInstances);

			application.setInstances(measureInstances);

		}


		ApplicationInstanceConfiguration resultApplicationInstanceConfiguration = 
				getConfigurationFromApplication(applicationInstanceService.save(application));

		return resultApplicationInstanceConfiguration;

	}

	private void fillInMeasuresInstancesFields(ApplicationInstanceConfiguration applicationConfiguration,
			Application application, SMMApplication applicationResource, Set<MeasureInstance> measureInstances) {
		
		Map<String, ArrayList<MeasureProperty>> mapNameToMeasurePropertiesList = 
				new HashMap<String, ArrayList<MeasureProperty>>();
		
		for(ApplicationMeasure applicationMeasure : applicationResource.getMeasures().getMeasure()) {
			SMMMeasure measure = measureCatalogue.getMeasure(application.getApplicationType(), applicationMeasure.getName());		

			MeasureInstance measureInstance = new MeasureInstance();
			measureInstance.setInstanceName(application.getName() + "_" + measure.getName());
			measureInstance.setMeasureName(measure.getName());
			measureInstance.setMeasureVersion("1.0.0");


			switch(measure.getType()) {
			case BINARY : measureInstance.setMeasureType(MeasureType.BINARY); break;
			case COLLECTIVE :  measureInstance.setMeasureType(MeasureType.COLLECTIVE); break;
			case COUNTING :  measureInstance.setMeasureType(MeasureType.COUNTING); break;
			case DIRECT :  measureInstance.setMeasureType(MeasureType.DIRECT); break;
			case GRADE :   measureInstance.setMeasureType(MeasureType.GRADE); break;
			case RACKING :   measureInstance.setMeasureType(MeasureType.RACKING); break;
			case RATIO :  measureInstance.setMeasureType(MeasureType.RATIO); break;
			case RESCALED :  measureInstance.setMeasureType(MeasureType.RESCALED); break;
			}
			
			
			measureInstance.setApplication(application);
			measureInstance.setProject(application.getProject());
			
			Set<MeasureProperty> measureProperties = new HashSet<MeasureProperty>();
			for(ScopeProperty scopeProperty : measure.getScopeProperties()) {
				MeasureProperty measureProperty = new MeasureProperty();
				
//				measureProperty.setPropertyValue("hhhh");
				measureProperty.setPropertyName(scopeProperty.getName());
				measureProperty.setPropertyType(scopeProperty.getType().toString());
				measureProperty.setMeasureInstance(measureInstance);

				if(! mapNameToMeasurePropertiesList.containsKey(scopeProperty.getName())) {
					ArrayList<MeasureProperty> listOfProperties = new ArrayList<MeasureProperty>();
					listOfProperties.add(measureProperty);
					mapNameToMeasurePropertiesList.put(scopeProperty.getName(), listOfProperties);
				} else {
					mapNameToMeasurePropertiesList.get(scopeProperty.getName()).add(measureProperty);
				}
				
				measureProperties.add(measureProperty);
			}
			

			
			
			measureInstance.setProperties(measureProperties);

			measureInstances.add(measureInstance );
		}

		/*
		 * Add measure properties values based on the 
		 * applicationConfigurationProperty value of the property 
		 * with the same name
		 */
		for(ApplicationProperty applicationConfigurationProperty : applicationConfiguration.getProperties()) {
			ArrayList<MeasureProperty> listOfProperties = 
					mapNameToMeasurePropertiesList.get(applicationConfigurationProperty.getName());
			
			for(MeasureProperty measureProperty : listOfProperties) {
				measureProperty
				.setPropertyValue(applicationConfigurationProperty.getValue());
			}
		}
	}

	private Application getApplicationFromConfiguration(ApplicationInstanceConfiguration applicationConfiguration) {
		Application application = new Application();
		application.setEnable(applicationConfiguration.getIsEnable());
		application.setId(applicationConfiguration.getId());
		application.setApplicationType(applicationConfiguration.getApplicationType());
		application.setName(applicationConfiguration.getName());
		application.setDescription("Description field is deprecated !");
		application.setProject(applicationConfiguration.getProject());
		return application;
	}

	private ApplicationInstanceConfiguration getConfigurationFromApplication( Application application) {
		if( application == null) {
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

	@Override
	public ApplicationInstanceConfiguration updateApplicaionInstance(
			ApplicationInstanceConfiguration applicationConfiguration) {

		Application application = getApplicationFromConfiguration(applicationConfiguration);

		ApplicationInstanceConfiguration resultApplicationInstanceConfiguration = 
				getConfigurationFromApplication(applicationInstanceService.save(application));

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
		if(applicationResource.getMeasures() != null) {

			Map<String,ScopeProperty> mapPropertyNameScopeProperty = new HashMap<>();
			for(ApplicationMeasure applicationMeasure : applicationResource.getMeasures().getMeasure()) {
				SMMMeasure measure = measureCatalogue.getMeasure(applicationType, applicationMeasure.getName());		
				for(ScopeProperty scopeProperty : measure.getScopeProperties()) {
					if(!mapPropertyNameScopeProperty.containsKey(scopeProperty.getName())) {
						mapPropertyNameScopeProperty.put(scopeProperty.getName(), scopeProperty);
					}
				}	
			}

			List<ApplicationProperty> properties = new ArrayList<ApplicationProperty>();
			for(ScopeProperty scopeProperty :mapPropertyNameScopeProperty.values()) {
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
		case DATE :  applicationProperty.setType(ApplicationPropertyType.DATE);
		break;
		case FLOAT: applicationProperty.setType(ApplicationPropertyType.FLOAT);
		break;
		case STRING :  applicationProperty.setType(ApplicationPropertyType.STRING);
		break;
		case INTEGER: applicationProperty.setType(ApplicationPropertyType.INTEGER);
		break;
		case PASSWORD :  applicationProperty.setType(ApplicationPropertyType.PASSWORD);
		break;
		case DESABLE: applicationProperty.setType(ApplicationPropertyType.DESABLE);
		break;	
		case ENUM: applicationProperty.setType(ApplicationPropertyType.ENUM);

		List<ApplicationPropertyEnumValue> lisApplicationPropertyEnumValue = new ArrayList<ApplicationPropertyEnumValue>();
		for(ScopePropertyEnumValue scopePropertyEnumValue : scopeProperty.getEnumType().getEnumvalue()) {
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

	@Override
	public boolean activateApplication(Long id) {
		// TODO Auto-generated method stub
		// create the views here// create non editable dashboards
		return false;
	}

	@Override
	public boolean desactivateApplication(Long id) {
		// TODO Auto-generated method stub
		return false;
	}


}
