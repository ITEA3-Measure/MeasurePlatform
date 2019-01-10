package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.ApplicationService;
import org.measure.platform.core.entity.Application;
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
    private ApplicationRepository ApplicationRepository;

    @Inject
    private ProjectRepository projectRepository;

    /**
     * Save a Application.
     * @param application the entity to save
     * @return the persisted entity
     */
    public Application save(Application application) {
        log.debug("Request to save Application : {}", application);    
        Application result = ApplicationRepository.save(application);
        return result;
    }

    /**
     * Get all the Applications.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Application> findAll() {
        log.debug("Request to get all Applications");
        List<Application> result = ApplicationRepository.findAll();
        return result;
    }


    /**
     * Get one Application by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Application findOne(Long id) {
        log.debug("Request to get Application : {}", id);
        Application Application = ApplicationRepository.findOne(id);
        return Application;
    }

    /**
     * Delete the Application by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
        Application application = ApplicationRepository.findOne(id);
        // TODO delete sub app
        ApplicationRepository.delete(id);
    }
    
    @Override
    public List<Application> findApplicationsByProject(Long projectId) {
        Project project = projectRepository.getOne(projectId);
        List<Application> result = ApplicationRepository.findByProject(project);
        return result;
    }


}
