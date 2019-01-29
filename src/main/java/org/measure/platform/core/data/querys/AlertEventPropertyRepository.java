package org.measure.platform.core.data.querys;

import java.util.List;

import org.measure.platform.core.data.entity.AlertEvent;
import org.measure.platform.core.data.entity.AlertEventProperty;
import org.measure.platform.core.data.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the AlertEventProperty entity.
 */
@SuppressWarnings("unused")
public interface AlertEventPropertyRepository extends JpaRepository<AlertEventProperty,Long> {

	@Query(value = "select i from AlertEventProperty i where i.alertevent = :alertEvent")
	List<AlertEventProperty> findByAlertEvent(@Param("alertEvent") AlertEvent alertEvent);

}
