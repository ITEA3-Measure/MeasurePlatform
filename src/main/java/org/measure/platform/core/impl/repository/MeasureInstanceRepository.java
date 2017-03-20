package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MeasureInstance entity.
 */
@SuppressWarnings("unused")
public interface MeasureInstanceRepository extends JpaRepository<MeasureInstance,Long> {
	
    @Query(value = "select i from MeasureInstance i where i.project = :project")
    List<MeasureInstance> findByProject(@Param("project")Project project);

    @Query(value = "select i from MeasureInstance i where i.measureName = :measureid")
	List<MeasureInstance> findByMeasure(@Param("measureid") String measureid);
}
