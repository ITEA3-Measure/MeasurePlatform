package org.measure.platform.restapi.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.dto.RightAccessDTO;
import org.measure.platform.core.entity.dto.UserProjectDTO;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.utils.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {
    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    @Inject
    private ProjectService projectService;
    
    @Inject
    private UserService userService;

    /**
     * POST /projects : Create a new project.
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the
     * new project, or with status 400 (Bad Request) if the project has
     * already an ID
     * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @Timed
    public ResponseEntity<Project> createProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert("project", "idexists", "A new project cannot already have an ID"))
                    .body(null);
        }
        //System.out.println(" Owner :" + project.getOwner());
        Project result = projectService.save(project);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert("project", result.getId().toString())).body(result);
    }

    /**
     * PUT /projects : Updates an existing project.
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * project, or with status 400 (Bad Request) if the project is not
     * valid, or with status 500 (Internal Server Error) if the project
     * couldnt be updated
     * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @Timed
    public ResponseEntity<Project> updateProject(@Valid @RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            return createProject(project);
        }
        Project result = projectService.save(project);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("project", project.getId().toString()))
                        .body(result);
    }

    /**
     * GET /projects : get all the projects.
     * @return the ResponseEntity with status 200 (OK) and the list of projects
     * in body
     */
    @GetMapping("/projects")
    @Timed
    public List<Project> getAllProjects() {
        log.debug("REST request to get all Projects");
        return projectService.findAll();
    }

    @GetMapping("/ownerProjects")
    @Timed
    public List<Project> getAllProjectsByOwner() {
        log.debug("REST request to get all Projects");
        return projectService.findAllByOwner();
    }

    /**
     * GET /projects/:id : get the "id" project.
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the
     * project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectService.findOne(id);
        return Optional.ofNullable(project).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
                        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE /projects/:id : delete the "id" project.
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("project", id.toString())).build();
    }
    
    /**
     * Invite user into a project
     * @param RightAccessDTO
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/projects/invite-to-project")
    @Timed
    public ResponseEntity<Project> inviteToProject(@Valid @RequestBody RightAccessDTO rightAccess) throws URISyntaxException {
        log.debug("REST request to invite user to Project : {}", rightAccess.getProjectId());
        if(projectService.findOne(rightAccess.getProjectId()).getId() == null ) {
        	return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert("project", "idinexists", "The project didn't exist"))
                    .body(null);
        }
        Project result = projectService.inviteToProject(rightAccess);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("project", result.getId().toString())).body(result);
    }
    
    /**
     * Transform user role
     * @param projectId
     * @param userId
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/projects/upgrade-user-role")
    @Timed
    public ResponseEntity<Project> upgradeUserRole(@Valid @RequestBody RightAccessDTO rightAccess) {
    	Long projectId = rightAccess.getProjectId();
    	Long userId = rightAccess.getUserId();
        log.debug("REST request to transform user role : {}", projectId);
        if(projectService.findOne(projectId).getId() == null && userService.getUserWithAuthorities(userId) == null) {
        	return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert("project or user", "projectId inexists", "userId inexists"))
                    .body(null);
        }
        Project result = projectService.upgradeUserRole(projectId, userId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("project", result.getId().toString())).body(result);
    }
    
    /**
     * GET /projects : get all the users by projects.
     * @return the ResponseEntity with status 200 (OK) and the list of projects
     * in body
     */
    @GetMapping("/projects/{projectId}/users")
    @Timed
    public List<UserProjectDTO> getAllUsersByProject(@PathVariable Long projectId) {
        log.debug("REST request to get all users by projectId : {}", projectId);
        return projectService.findAllUsersByProject(projectId);
    }
    
    /**
     * GET /projects : get all the candidate users to project.
     * @return the ResponseEntity with status 200 (OK) and the list of projects
     * in body
     */
    @GetMapping("/projects/{projectId}/candidate-users")
    @Timed
    public List<UserProjectDTO> getCandidateUsersToProject(@PathVariable Long projectId) {
        log.debug("REST request to get candidate users by projectId : {}", projectId);
        return projectService.findCandidateUsersByProject(projectId);
    }
    
    /**
     * Delete user from a project
     * @param projectId
     * @param userId
     * @return
     * @throws URISyntaxException
     */
    @PutMapping("/projects/remove-from-project")
    @Timed
    public ResponseEntity<Project> removeUserFromProject(@Valid @RequestBody RightAccessDTO rightAccess) {
    	Long projectId = rightAccess.getProjectId();
    	Long userId = rightAccess.getUserId();
        log.debug("REST request to remove user from Project : {}", projectId);
        if(projectService.findOne(projectId).getId() == null && userService.getUserWithAuthorities(userId) == null) {
        	return ResponseEntity.badRequest().headers(
                    HeaderUtil.createFailureAlert("project or user", "projectId inexists", "userId inexists"))
                    .body(null);
        }
        Project result = projectService.deleteUserFromProject(projectId, userId);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("project", result.getId().toString())).body(result);
    }

}
