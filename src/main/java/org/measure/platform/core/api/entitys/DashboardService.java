package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.Dashboard;

/**
 * Service Interface for managing Dashboard.
 */
public interface DashboardService {

    /**
     * Save a dashboard.
     *
     * @param dashboard the entity to save
     * @return the persisted entity
     */
    Dashboard save(Dashboard dashboard);

    /**
     *  Get all the dashboards.
     *  
     *  @return the list of entities
     */
    List<Dashboard> findAll();

    /**
     *  Get the "id" dashboard.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Dashboard findOne(Long id);

    /**
     *  Delete the "id" dashboard.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

	List<Dashboard> findByPhase(Long id);
}
