package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.Application;

/**
 * Service Interface for managing Application.
 */
public interface ApplicationService {
    /**
     * Save a application.
     * @param application the entity to save
     * @return the persisted entity
     */
	Application save(Application measureInstance);

    /**
     * Get all the application.
     * @return the list of entities
     */
    List<Application> findAll();

    /**
     * Get the "id" application.
     * @param id the id of the entity
     * @return the entity
     */
    Application findOne(Long id);

    /**
     * Delete the "id" application.
     * @param id the id of the entity
     */
    void delete(Long id);

    List<Application> findApplicationsByProject(Long projectId);


}
