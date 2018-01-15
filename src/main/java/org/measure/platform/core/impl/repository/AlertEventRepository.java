package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.AlertEvent;
import org.measure.platform.core.entity.Phase;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.ProjectAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the AlertEvent entity.
 */
@SuppressWarnings("unused")
public interface AlertEventRepository extends JpaRepository<AlertEvent,Long> {

	@Query(value = "select i from AlertEvent i where i.project = :project")
	List<AlertEvent> findByProject(@Param("project") Project project);

	@Query(value = "select i from AlertEvent i where i.projectanalysis = :projectAnalysis")
	List<AlertEvent> findByProjectAnalysis(@Param("projectAnalysis") ProjectAnalysis projectAnalysis);

}
