package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.AlertEventService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.api.entitys.ProjectAnalysisService;
import org.measure.platform.core.entity.AlertEvent;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.ProjectAnalysis;
import org.measure.platform.core.impl.repository.ProjectAnalysisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Project.
 */
@Service
@Transactional
public class ProjectAnalysisServiceImpl implements  ProjectAnalysisService {
    private final Logger log = LoggerFactory.getLogger(ProjectAnalysisServiceImpl.class);

    @Inject
    private  ProjectAnalysisRepository  projectAnalysisRepository;

    @Inject
    private AlertEventService alertEventService;
    
    @Inject
    private MeasureViewService measureViewService;
    
    /**
     * Save a ProjectAnalysis.
     * @param project the entity to save
     * @return the persisted entity
     */
    public ProjectAnalysis save(ProjectAnalysis projectAnalysis) {
        log.debug("Request to save Project : {}", projectAnalysis);
        ProjectAnalysis result = projectAnalysisRepository.save(projectAnalysis);
        return result;
    }

    /**
     * Get all the ProjectAnalysis.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ProjectAnalysis> findAll() {
        log.debug("Request to get all ProjectAnalysis");
        List<ProjectAnalysis> result = projectAnalysisRepository.findAll();
        return result;
    }


    /**
     * Get one project by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ProjectAnalysis findOne(Long id) {
        log.debug("Request to get ProjectAnalysis : {}", id);
        ProjectAnalysis projectAnalysis = projectAnalysisRepository.findOne(id);
        return projectAnalysis;
    }

    /**
     * Delete the  project by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
    	ProjectAnalysis projectAnalysis = projectAnalysisRepository.findOne(id);
 
        for(MeasureView view : measureViewService.findByProjectAnalysis(id)){
        	measureViewService.delete(view.getId());
        }
        
        for(AlertEvent event : alertEventService.findAllByProjectAnalysis(projectAnalysis)){
        	alertEventService.delete(event.getId());
        }
        
 
        projectAnalysisRepository.delete(id);
    }

	@Override
	public List<ProjectAnalysis> findAllByProject(Project project) {
		return projectAnalysisRepository.findAllByProject(project);
	}

}
