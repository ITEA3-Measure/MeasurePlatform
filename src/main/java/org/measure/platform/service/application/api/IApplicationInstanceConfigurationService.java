package org.measure.platform.service.application.api;

import org.measure.platform.service.application.impl.dto.ApplicationInstanceConfiguration;

public interface IApplicationInstanceConfigurationService {
	

	public ApplicationInstanceConfiguration createApplicaionInstance(ApplicationInstanceConfiguration applicationConfiguration);
	
	public ApplicationInstanceConfiguration updateApplicaionInstance(ApplicationInstanceConfiguration applicationConfiguration);

	public void deleteApplicaionInstance(Long id);
	
	public ApplicationInstanceConfiguration getApplicaionInstanceById(Long id);
	
	public ApplicationInstanceConfiguration getApplicaionInstanceByApplication(String applicationName);
	
}

