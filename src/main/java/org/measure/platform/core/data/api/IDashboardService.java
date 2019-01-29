package org.measure.platform.core.data.api;

import java.util.List;

import org.measure.platform.core.data.api.dto.DashboardDTO;
import org.measure.platform.core.data.entity.Dashboard;
import org.measure.platform.utils.domain.User;

/**
 * Service Interface for managing Dashboard.
 */
public interface IDashboardService {
    /**
     * Save a dashboard.
     * @param dashboard the entity to save
     * @return the persisted entity
     */
    Dashboard save(Dashboard dashboard);

    /**
     * Get all the dashboards.
     * @return the list of entities
     */
    List<Dashboard> findAll();

    /**
     * Get the "id" dashboard.
     * @param id the id of the entity
     * @return the entity
     */
    Dashboard findOne(Long id);

    /**
     * Delete the "id" dashboard.
     * @param id the id of the entity
     */
    void delete(Long id);
    
    List<Dashboard> findByProject(Long projectId);

    List<DashboardDTO> findDTOByProject(Long projectId);
    
    /**
     * Share Dashboard with User.
     * @param dashboard
     * @param userId
     * @return
     */
    public Dashboard shareDashboardWithUser(Dashboard dashboard, Long userId);
    
    /**
     * Remove User on Dashboard.
     * @param dashboard
     * @param userId
     * @return
     */
    public Dashboard removeUserOnDashboard(Dashboard dashboard, Long userId);
    
    /**
     * Check if current user has manager role on dashboard.
     * @param dashboardId
     */
    public boolean isCurrentUserHasManagerRole(Long dashboardId);
    
	List<Dashboard> findByApplication(Long applicationId);

}
