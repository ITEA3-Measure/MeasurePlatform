package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.ApplicationService;
import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.impl.repository.ApplicationRepository;
import org.measure.platform.core.impl.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Application.
 */
@Service
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    private final Logger log = LoggerFactory.getLogger(ApplicationServiceImpl.class);

    @Inject
    private ApplicationRepository applicationInstanceRepository;

    @Inject
    private ProjectService projectService;
    
    
    @Inject
    private MeasureInstanceService instanceService;
    
    
    @Inject
    private DashboardService dashboardService;



    /**
     * Save a applicationInstance.
     * @param applicationInstance the entity to save
     * @return the persisted entity
     */
    public Application save(Application applicationInstance) {
        log.debug("Request to save Application instance : {}", applicationInstance); 

        Application result = applicationInstanceRepository.save(applicationInstance);

        return result;
    }

    /**
     * Get all the Application instances.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Application> findAll() {
        log.debug("Request to get all Application instances");
        List<Application> result = applicationInstanceRepository.findAll();
        return result;
    }

    @Override
    public List<Application> findApplicationInstancesByProject(Long projectId) {
        Project project = projectService.findOne(projectId);
        List<Application> result = applicationInstanceRepository.findByProject(project);
        return result;
    }

    /**
     * Get one applicationInstance by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Application findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        Application applicationInstance = applicationInstanceRepository.findOne(id);
        return applicationInstance;
    }

    /**
     * Delete the Application by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
  	
    	Application application = findOne(id);
    	
    	for(MeasureInstance instance : application.getInstances()) {
    		instanceService.delete(instance.getId());
    	}
    	
    	
    	for(Dashboard dashboard : application.getDashboards()) {
    		dashboardService.delete(dashboard.getId());
    	}
        
        applicationInstanceRepository.delete(id);
    }

	@Override
	public List<Application> findApplicationInstancesByName(String name) {
		return  applicationInstanceRepository.findByName(name);
	}

	@Override
	public List<Application> findApplicationInstanceByApplicationType(String applicationtype) {
        List<Application> result = applicationInstanceRepository.findByApplicationType(applicationtype);
        return result;
	}


}
