package org.measure.platform.core.impl.repository;

import java.util.List;

import org.measure.platform.core.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Project entity.
 */
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Query(value= "select * from project where project.id in "
			+ "( select user_managed_project.project_id from user_managed_project where user_managed_project.user_id in "
				+ "( select jhi_user.id from jhi_user where jhi_user.login = ?#{principal.username} ) )"
	+ "union "
		+ "select * from project where project.id in "
			+ "( select user_invited_project.project_id from user_invited_project where user_invited_project.user_id in "
				+ "( select jhi_user.id from jhi_user where jhi_user.login = ?#{principal.username} ) )", nativeQuery= true)
	List<Project> findByOwnerIsCurrentUser();
}
