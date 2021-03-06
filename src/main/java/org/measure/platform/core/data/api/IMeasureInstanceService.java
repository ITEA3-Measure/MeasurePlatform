package org.measure.platform.core.data.api;

import java.util.List;

import org.measure.platform.core.data.entity.MeasureInstance;

/**
 * Service Interface for managing MeasureInstance.
 */
public interface IMeasureInstanceService {
    /**
     * Save a measureInstance.
     * @param measureInstance the entity to save
     * @return the persisted entity
     */
    MeasureInstance save(MeasureInstance measureInstance);

    /**
     * Get all the measureInstances.
     * @return the list of entities
     */
    List<MeasureInstance> findAll();

    /**
     * Get the "id" measureInstance.
     * @param id the id of the entity
     * @return the entity
     */
    MeasureInstance findOne(Long id);

    /**
     * Delete the "id" measureInstance.
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Get all the measureInstances of a specified project.
     * @return the list of entities
     */
    List<MeasureInstance> findMeasureInstancesByProject(Long projectId);

    List<MeasureInstance> findMeasureInstanceByReference(String id);

    List<MeasureInstance> findMeasureInstancesByName(String name);

    List<MeasureInstance> findMeasureInstancesByApplicationInstance(Long applicationInstanceId);

	MeasureInstance findMeasureInstancesByApplicationInstance(Long id, String measureType);

}
