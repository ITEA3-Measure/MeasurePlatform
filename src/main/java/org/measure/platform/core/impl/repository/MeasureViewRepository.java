package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Phase;
import org.measure.platform.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MeasureView entity.
 */
@SuppressWarnings("unused")
public interface MeasureViewRepository extends JpaRepository<MeasureView,Long> {
	
	@Query(value = "select i from MeasureView i where i.project = :project")
	List<MeasureView> findByProject(@Param("project")Project project);
	
	@Query(value = "select i from MeasureView i where i.projectoverview = :project")
	List<MeasureView> findByProjectOverview(@Param("project")Project project);

	@Query(value = "select i from MeasureView i where i.phase = :phase")
	List<MeasureView> findByPhase(@Param("phase")Phase phase);
	
	@Query(value = "select i from MeasureView i where i.phaseoverview = :phase")
	List<MeasureView> findByPhaseOverview(@Param("phase")Phase phase);
	
	@Query(value = "select i from MeasureView i where i.dashboard = :dashboard")
	List<MeasureView> findByDashboard(@Param("dashboard")Dashboard dashboard);

	@Query(value = "select i from MeasureView i where i.measureinstance = :minstance")
	List<MeasureView> findByMeasure(@Param("minstance")MeasureInstance minstance);

}
