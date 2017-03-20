package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.Phase;
import org.measure.platform.core.entity.Project;

/**
 * Service Interface for managing Phase.
 */
public interface PhaseService {

    /**
     * Save a phase.
     *
     * @param phase the entity to save
     * @return the persisted entity
     */
    Phase save(Phase phase);

    /**
     *  Get all the phases.
     *  
     *  @return the list of entities
     */
    List<Phase> findAll();

    /**
     *  Get the "id" phase.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Phase findOne(Long id);

    /**
     *  Delete the "id" phase.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

	List<Phase> findByProject(Project findOne);
}
