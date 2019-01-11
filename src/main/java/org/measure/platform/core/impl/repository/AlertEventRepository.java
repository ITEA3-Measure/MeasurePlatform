package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.AlertEvent;
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

	@Query(value = "select i from AlertEvent i where i.project = :project")
	List<AlertEvent> findByProjectAnalysis(@Param("project") Project project);

	@Query(value = "select i from AlertEvent i where i.analysisTool = :analysisTool and  i.project = :project and i.eventType = :eventType")
	List<AlertEvent> findByProjectAndEventType(@Param("project") Project project,@Param("analysisTool") String analysisTool,@Param("eventType") String eventType);

	
	@Query(value = "select i from AlertEvent i join i.alerteventpropertys pr where i.analysisTool = :analysisTool and  i.project = :project and i.eventType = :eventType and pr.name = :propName and pr.value = :propValue")
	List<AlertEvent> findByProjectEventTypeAndProp(@Param("project") Project project,@Param("analysisTool") String analysisTool,@Param("eventType") String eventType,@Param("propName")String propName,@Param("propValue") String propValue);
	
	

}
