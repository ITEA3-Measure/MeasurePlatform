package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.AnalysisCard;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.ProjectAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the AnalysisCard entity.
 */
public interface AnalysisCardRepository extends JpaRepository<AnalysisCard,Long> {
    @Query(value = "select i from AnalysisCard i where i.projectanalysis = :projectAnalysis")
	List<AnalysisCard> findByProjectAnalysis(@Param("projectAnalysis") ProjectAnalysis projectAnalysis);

    @Query(value = "select i from AnalysisCard i join i.projectanalysis pa where pa.project = :project")
	List<AnalysisCard> findByProject(@Param("project")Project project);

}
