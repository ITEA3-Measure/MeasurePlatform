package org.measure.platform.service.application.api;

import org.measure.platform.service.application.impl.dto.ApplicationInstanceConfiguration;

public interface IApplicationInstanceService {
	

	public ApplicationInstanceConfiguration createApplicaionInstance(ApplicationInstanceConfiguration applicationConfiguration);
	
	public ApplicationInstanceConfiguration updateApplicaionInstance(ApplicationInstanceConfiguration applicationConfiguration);

	public ApplicationInstanceConfiguration deleteApplicaionInstance(Long id);
	
	public ApplicationInstanceConfiguration getApplicaionInstanceById(Long id);
	
	public ApplicationInstanceConfiguration getApplicaionInstanceByApplication(String applicationName);
	
	public boolean activateApplication(Long id);
	
	public boolean desactivateApplication(Long id);
}

