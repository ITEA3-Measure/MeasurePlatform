package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.Application;

/**
 * Service Interface for managing ApplicationInstance.
 */
public interface ApplicationService {
    /**
     * Save an applicationInstance.
     * @param applicationInstance the entity to save
     * @return the persisted entity
     */
    Application save(Application applicationInstance);

    /**
     * Get all the applicationInstances.
     * @return the list of entities
     */
    List<Application> findAll();

    /**
     * Get the "id" applicationInstance.
     * @param id the id of the entity
     * @return the entity
     */
    Application findOne(Long id);

    /**
     * Delete the "id" applicationInstance.
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get all the applicationInstances of a specified project.
     * @return the list of entities
     */
    List<Application> findApplicationInstancesByProject(Long projectId);

    List<Application> findApplicationInstanceByApplicationType(String applicationtype);

    List<Application> findApplicationInstancesByName(String name);

}
