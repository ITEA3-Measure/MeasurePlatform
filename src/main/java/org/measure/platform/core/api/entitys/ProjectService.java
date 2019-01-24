package org.measure.platform.core.api.entitys;

import java.util.List;

import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.dto.RightAccessDTO;
import org.measure.platform.core.entity.dto.UserProjectDTO;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Service Interface for managing Project.
 */
public interface ProjectService {
    /**
     * Save a project.
     * @param project the entity to save
     * @return the persisted entity
     */
    Project save(Project project);

    /**
     * Get all the projects.
     * @return the list of entities
     */
    List<Project> findAll();

    /**
     * Get all the projects of current owner.
     * @return the list of entities
     */
    List<Project> findAllByOwner();

    /**
     * Get the "id" project.
     * @param id the id of the entity
     * @return the entity
     */
    Project findOne(Long id);

    /**
     * Delete the "id" project.
     * @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Invite user into Project
     * @param project
     * @return
     */
    Project inviteToProject(RightAccessDTO rightAccess);
    
    /**
     * Get all the users by project.
     * @return the list of entities
     */
    public List<UserProjectDTO> findAllUsersByProject(Long projectId);
    
    /**
     * Get the candidates users to a project.
     * @return the list of entities
     */
    public List<UserProjectDTO> findCandidateUsersByProject(Long projectId);
    
    /**
     * Transfer inviter to manager user
     * @param projectId
     * @param userId
     * @return
     */
   public Project transformUserRole(Long projectId, Long userId);
    
    /**
     * Transfer user role
     * @param projectId
     * @param userId
     * @return
     */
    public Project deleteUserFromProject(Long projectId, Long userId);
    
    public boolean isCurrentUserHasManagerRole(@PathVariable Long projectId);
    
}
