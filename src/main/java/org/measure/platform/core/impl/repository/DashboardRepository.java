package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Dashboard entity.
 */
public interface DashboardRepository extends JpaRepository<Dashboard,Long> {
    @Query(value = "select i from Dashboard i where i.project = :project")
    List<Dashboard> findByProject(@Param("project") Project project);

}
