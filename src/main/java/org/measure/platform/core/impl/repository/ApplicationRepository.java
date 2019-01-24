package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the MeasureInstance entity.
 */
public interface ApplicationRepository extends JpaRepository<Application,Long> {
    @Query(value = "select i from Application i where i.project = :project")
    List<Application> findByProject(@Param("project") Project project);

    @Query(value = "select i from Application i where i.applicationType = :applicationtype")
    List<Application> findByApplicationType(@Param("applicationtype") String applicationype);

    @Query(value = "select i from Application i where i.name = :applicationInstanceName")
    List<Application> findByName(@Param("applicationInstanceName")String name);


}
