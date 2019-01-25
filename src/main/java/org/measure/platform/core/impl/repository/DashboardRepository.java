package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.Dashboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Dashboard entity.
 */
public interface DashboardRepository extends JpaRepository<Dashboard,Long> {
	
    @Query(value = "SELECT DISTINCT dashboard.id, dashboard.dashboard_name, dashboard.dashboard_description, dashboard.mode, dashboard.kibana_id, dashboard.dashboard_content, dashboard.auto, dashboard.editable, dashboard.time_periode, dashboard.size, dashboard.project_id, dashboard.application_id, dashboard.manager_id FROM dashboard LEFT JOIN user_viewed_dashboard ON dashboard.id = user_viewed_dashboard.dashboard_id LEFT JOIN jhi_user ON user_viewed_dashboard.user_id = jhi_user.id WHERE ( dashboard.project_id = ?1 AND (dashboard.mode = 'OVERVIEW' OR dashboard.mode = 'APPLICATION') ) OR ( dashboard.project_id = ?1 AND dashboard.manager_id = ?2 ) OR ( dashboard.project_id = ?1 AND jhi_user.id = ?2 )", nativeQuery = true)
    List<Dashboard> findByProjectAndUser(@Param("projectId") Long projectId, @Param("userId") Long userId);

    @Query(value = "select i from Dashboard i where i.application = :application")
    List<Dashboard> findByApplication(@Param("application") Application application);
}