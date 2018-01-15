package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.ProjectAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the ProjectAnalysis entity.
 */
@SuppressWarnings("unused")
public interface ProjectAnalysisRepository extends JpaRepository<ProjectAnalysis,Long> {

	@Query(value = "select i from ProjectAnalysis i where i.project = :project")
	List<ProjectAnalysis> findAllByProject(@Param("project")Project project);

}
