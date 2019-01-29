package org.measure.platform.core.data.querys;

import java.util.List;

import org.measure.platform.core.data.entity.Project;
import org.measure.platform.core.data.entity.ProjectAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the ProjectAnalysis entity.
 */
public interface ProjectAnalysisRepository extends JpaRepository<ProjectAnalysis,Long> {

	@Query(value = "select i from ProjectAnalysis i where i.project = :project")
	List<ProjectAnalysis> findAllByProject(@Param("project")Project project);

	@Query(value = "select i from ProjectAnalysis i where i.project = :project and i.analysisToolId = :analysisTool")
	List<ProjectAnalysis> findByProjectAndTool(@Param("project")Project project,@Param("analysisTool") String analysisTool);

	@Query(value = "select i from ProjectAnalysis i where i.analysisToolId = :analysisTool")
	List<ProjectAnalysis> findAllByAnalysisTool(String analysisToolId);

}
