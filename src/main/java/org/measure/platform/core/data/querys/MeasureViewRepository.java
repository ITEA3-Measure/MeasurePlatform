package org.measure.platform.core.data.querys;

import java.util.List;

import org.measure.platform.core.data.entity.AnalysisCard;
import org.measure.platform.core.data.entity.Dashboard;
import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.core.data.entity.MeasureView;
import org.measure.platform.core.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MeasureView entity.
 */
@SuppressWarnings("unused")
public interface MeasureViewRepository extends JpaRepository<MeasureView,Long> {
    @Query(value = "select i from MeasureView i where i.project = :project")
    List<MeasureView> findByProject(@Param("project") Project project);

    @Query(value = "select i from MeasureView i where i.projectoverview = :project")
    List<MeasureView> findByProjectOverview(@Param("project") Project project);

    @Query(value = "select i from MeasureView i where i.dashboard = :dashboard")
    List<MeasureView> findByDashboard(@Param("dashboard") Dashboard dashboard);

    @Query(value = "select i from MeasureView i where i.measureinstance = :minstance")
    List<MeasureView> findByMeasure(@Param("minstance") MeasureInstance minstance);

    @Query(value = "select i from MeasureView i where i.analysiscard = :analysisCard")
	List<MeasureView> findByAnalysisCard(@Param("analysisCard") AnalysisCard analysisCard);

    @Query(value = "select i from MeasureView i where i.measureinstance = :measureinstance AND i.defaultView = true")
  	List<MeasureView> findDefaultViewByMeasureInstance(@Param("measureinstance") MeasureInstance measureinstance);
}
