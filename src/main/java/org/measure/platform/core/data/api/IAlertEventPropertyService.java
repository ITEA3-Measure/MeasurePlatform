package org.measure.platform.core.data.api;

import java.util.List;

import org.measure.platform.core.data.entity.AlertEvent;
import org.measure.platform.core.data.entity.AlertEventProperty;

/**
 * Service Interface for managing AlertEvent.
 */
public interface IAlertEventPropertyService {
    /**
     * Save a alert event property.
     * @param project the entity to save
     * @return the persisted entity
     */
    AlertEventProperty save(AlertEventProperty project);

    /**
     * Get all the alert event property.
     * @return the list of entities
     */
    List<AlertEventProperty> findAll();

    /**
     * Get all the alert events of current owner.
     * @return the list of entities
     */
    List<AlertEventProperty> findAllByAlertEvent(AlertEvent alertEvent);

    /**
     * Get the "id" alert event property.
     * @param id the id of the entity
     * @return the entity
     */
    AlertEventProperty findOne(Long id);

    /**
     * Delete the "id" alert event  property.
     * @param id the id of the entity
     */
    void delete(Long id);

	

}
