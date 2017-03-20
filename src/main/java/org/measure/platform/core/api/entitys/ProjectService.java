package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.Project;

/**
 * Service Interface for managing Project.
 */
public interface ProjectService {

    /**
     * Save a project.
     *
     * @param project the entity to save
     * @return the persisted entity
     */
    Project save(Project project);

    /**
     *  Get all the projects.
     *  
     *  @return the list of entities
     */
    List<Project> findAll();
    
    /**
     *  Get all the projects of current owner.
     *  
     *  @return the list of entities
     */
    List<Project>  findAllByOwner();

    /**
     *  Get the "id" project.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Project findOne(Long id);

    /**
     *  Delete the "id" project.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
