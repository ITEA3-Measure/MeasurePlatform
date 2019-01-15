package org.measure.platform.core.impl.entitys;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.AlertEventService;
import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.api.entitys.NotificationService;
import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.AlertEvent;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Notification;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.impl.repository.ProjectRepository;
import org.measure.platform.service.analysis.api.IAlertSubscriptionManager;
import org.measure.platform.service.analysis.api.IAnalysisCatalogueService;
import org.measure.platform.service.analysis.data.alert.AlertSubscription;
import org.measure.platform.service.analysis.data.alert.AlertType;
import org.measure.platform.service.analysis.data.analysis.RegistredAnalysisService;
import org.measure.platform.service.measurement.api.IElasticsearchIndexManager;
import org.measure.platform.utils.domain.User;
import org.measure.platform.utils.security.ProjectsUsersRolesConstants;
import org.measure.platform.utils.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {
    private final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private MeasureInstanceService measureInstanceService;
    
    @Inject
    private IElasticsearchIndexManager indexManager;

    @Inject
    private DashboardService dashboardService;

    @Inject
    private MeasureViewService viewService;

    @Inject
    private NotificationService notifService;
    
    
    @Inject
    private AlertEventService alertEventService;
   
    
    @Inject
    private IAlertSubscriptionManager subscriptionManager;
    
    @Inject
    private IAnalysisCatalogueService analysisCatalogue;
    
    @Inject
    private UserService userService;
    

    /**
     * Save a project.
     * @param project the entity to save
     * @return the persisted entity
     */
    public Project save(Project project) {
        log.debug("Request to save Project : {}", project);
    	User user = userService.findByCurrentLoggedIn();
        project.addManagers(user);
        
        if(project.getId() == null) {
        	Project result = projectRepository.save(project);
        	
        	for(RegistredAnalysisService service : analysisCatalogue.getAllAnalysisService()){
        		AlertSubscription suscribtion = new AlertSubscription();
    			suscribtion.setAnalysisTool(service.getName());
    			suscribtion.setProjectId(result.getId());
    			suscribtion.setEventType(AlertType.ANALYSIS_ENABLE);
    			subscriptionManager.subscribe(suscribtion);
    			
    			
    			suscribtion = new AlertSubscription();
    			suscribtion.setAnalysisTool(service.getName());
    			suscribtion.setProjectId(result.getId());
    			suscribtion.setEventType(AlertType.ANALYSIS_DESABLE);
    			subscriptionManager.subscribe(suscribtion);
        	}
        	
        	Dashboard dashboard = new Dashboard();
        	dashboard.setProject(result);
        	dashboard.setEditable(false);
        	dashboard.setMode("OVERVIEW");
        	dashboard.setDashboardName("Overview");
        	dashboardService.save(dashboard);
        	
        	return result;
        }else{
        	return projectRepository.save(project);
        }

    }

    /**
     * Get all the projects.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Project> findAll() {
        log.debug("Request to get all Projects");
        List<Project> result = projectRepository.findAll();
        return result;
    }

    /**
     * Get all the projects.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Project> findAllByOwner() {
        return projectRepository.findByOwnerIsCurrentUser();
    }

    /**
     * Get one project by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Project findOne(Long id) {
        log.debug("Request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        return project;
    }

    /**
     * Delete the  project by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
        Project project = projectRepository.findOne(id);
        for(Dashboard dashboard : dashboardService.findByProject(id)){
        	dashboardService.delete(dashboard.getId());
        }
        
        for(MeasureView view : viewService.findByProjectOverview(id)){
            viewService.delete(view.getId());
        }
        
        for(MeasureView view : viewService.findByProject(id)){
            viewService.delete(view.getId());
        }
                        
        for (MeasureInstance instance : measureInstanceService.findMeasureInstancesByProject(id)) {
            measureInstanceService.delete(instance.getId());
            indexManager.deleteIndex(instance);
        }
        
        for (Notification notif : notifService.findAllByProject(project)) {
            notifService.delete(notif.getId());
        }
        
        for (AlertEvent event : alertEventService.findAllByProject(project)) {
        	alertEventService.delete(event.getId());
        }

        projectRepository.delete(id);
    }
    
    @Override
    public Project inviteIntoProject(Long projectId, Long userId, String role) {
    	log.debug("Request to invite user into Project : {}", projectId);
    	User user = userService.findOne(userId);
    	Project project = projectRepository.findOne(projectId);
    	if (role.equals(ProjectsUsersRolesConstants.INVITER)) {
        	project.addInviters(user);
		}
    	if (role.equals(ProjectsUsersRolesConstants.MANAGER)) {
    		project.addManagers(user);
    	}
    	Project result = projectRepository.save(project);
    	return result;
    }

	@Override
	public boolean transformUserRole(Long projectId, Long userId) {
		Project project = projectRepository.getOne(projectId);
		Set<User> inviters = project.getInviters();
		for (User user : inviters) {
			if(user.getId() == userId) {
				project.removeInviters(user);
				project.addManagers(user);
				return true;
			}
		}
		return false;
	}

	@Override
	public void deleteUserFromProject(Long projectId, Long userId) {
		Project project = projectRepository.getOne(projectId);
		User currentUser = userService.findByCurrentLoggedIn();
		if(currentUser.getId() != userId) {
			for (User user : project.getInviters()) {
				if (user.getId() == userId) {
					project.removeInviters(user);
					return;
				}
			}
			for (User manager : project.getManagers()) {
				if (manager.getId() == userId) {
					project.removeManagers(manager);
					return;
				}
			}
		}
		return;		
	}

}
