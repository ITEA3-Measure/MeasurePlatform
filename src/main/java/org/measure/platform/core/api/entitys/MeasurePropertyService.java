package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureProperty;

/**
 * Service Interface for managing MeasureProperty.
 */
public interface MeasurePropertyService {

    /**
     * Save a measureProperty.
     *
     * @param measureProperty the entity to save
     * @return the persisted entity
     */
    MeasureProperty save(MeasureProperty measureProperty);

    /**
     *  Get all the measureProperties.
     *  
     *  @return the list of entities
     */
    List<MeasureProperty> findAll();

    /**
     *  Get the "id" measureProperty.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MeasureProperty findOne(Long id);

    /**
     *  Delete the "id" measureProperty.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    List<MeasureProperty> findByInstance(MeasureInstance instance);
}
