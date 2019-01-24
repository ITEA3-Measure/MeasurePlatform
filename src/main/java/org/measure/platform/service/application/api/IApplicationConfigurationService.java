package org.measure.platform.service.application.api;

import org.measure.platform.service.application.impl.dto.ApplicationConfiguration;

public interface IApplicationConfigurationService {
	

	public ApplicationConfiguration createApplication(ApplicationConfiguration applicationConfiguration);
	
	public ApplicationConfiguration updateApplication(ApplicationConfiguration applicationConfiguration);

	public void deleteApplicaionInstance(Long id);
	
	public ApplicationConfiguration getConfiguration(Long id);
	
	public ApplicationConfiguration getConfigurationByType(String applicationName);
	
}

