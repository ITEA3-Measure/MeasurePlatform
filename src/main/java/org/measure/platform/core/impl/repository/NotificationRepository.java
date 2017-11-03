package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Notification;
import org.measure.platform.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Phase entity.
 */
@SuppressWarnings("unused")
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    @Query(value = "select i from Notification i where i.project = :project and i.isValidated = false")
    List<Notification> findNewByProject(@Param("project") Project project);

    @Query(value = "select i from Notification i where i.project = :project")
    List<Notification> findByProject(@Param("project") Project project);

}
