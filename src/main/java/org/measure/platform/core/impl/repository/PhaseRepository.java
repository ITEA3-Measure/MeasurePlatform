package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Phase;
import org.measure.platform.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Phase entity.
 */
@SuppressWarnings("unused")
public interface PhaseRepository extends JpaRepository<Phase,Long> {

	@Query(value = "select i from Phase i where i.project = :project")
	List<Phase> findByProject(@Param("project")Project project);

}
