package org.measure.platform.service.application.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.measure.platform.core.api.IApplicationCatalogueService;
import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.service.application.api.IApplicationInstanceService;
import org.measure.platform.service.application.impl.dto.ApplicationInstanceConfiguration;
import org.measure.smm.application.model.ApplicationMeasure;
import org.measure.smm.application.model.SMMApplication;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.measure.model.ScopeProperty;

public class ApplicationInstanceServiceImpl implements IApplicationInstanceService {
	
	@Inject
	private IApplicationCatalogueService applicationCatalogue;
	
	@Inject
	private IMeasureCatalogueService measureCatalogue;

	@Override
	public ApplicationInstanceConfiguration createApplicaionInstance(
			ApplicationInstanceConfiguration applicationConfiguration) {
		// TODO Auto-generated method stub 
		return null;
	}

	@Override
	public ApplicationInstanceConfiguration updateApplicaionInstance(
			ApplicationInstanceConfiguration applicationConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInstanceConfiguration deleteApplicaionInstance(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInstanceConfiguration getApplicaionInstanceById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInstanceConfiguration getApplicaionInstanceByApplication(String applicationType) {
		ApplicationInstanceConfiguration instance = new ApplicationInstanceConfiguration();
		instance.setApplicationType(applicationType);
		
		SMMApplication application = applicationCatalogue.getApplication(applicationType);
		if(application.getMeasures() != null) {
			
			Map<String,ScopeProperty> properties = new HashMap<>();
			for(ApplicationMeasure applicationMeasure : application.getMeasures().getMeasure()) {
				SMMMeasure measure = measureCatalogue.getMeasure(applicationType, applicationMeasure.getName());		
				for(ScopeProperty measureProp : measure.getScopeProperties()) {
					if(!properties.containsKey(measureProp.getName())) {
						properties.put(measureProp.getName(), measureProp);
					}
				}	
			}
			
			for(ScopeProperty measureProp :properties.values()) {
				//
				
			}
		}
	
	
		
		return instance;
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
