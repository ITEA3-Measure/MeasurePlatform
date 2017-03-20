package org.measure.platform.restapi.app.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.Project;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
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


	/**
	 * POST /projects : Create a new project.
	 *
	 * @param project
	 *            the project to create
	 * @return the ResponseEntity with status 201 (Created) and with body the
	 *         new project, or with status 400 (Bad Request) if the project has
	 *         already an ID
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
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
		System.out.println(" Owner :" + project.getOwner());
		Project result = projectService.save(project);
		return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("project", result.getId().toString())).body(result);
	}

	/**
	 * PUT /projects : Updates an existing project.
	 *
	 * @param project
	 *            the project to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated
	 *         project, or with status 400 (Bad Request) if the project is not
	 *         valid, or with status 500 (Internal Server Error) if the project
	 *         couldnt be updated
	 * @throws URISyntaxException
	 *             if the Location URI syntax is incorrect
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
	 *
	 * @return the ResponseEntity with status 200 (OK) and the list of projects
	 *         in body
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
	 *
	 * @param id
	 *            the id of the project to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         project, or with status 404 (Not Found)
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
	 *
	 * @param id
	 *            the id of the project to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/projects/{id}")
	@Timed
	public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
		log.debug("REST request to delete Project : {}", id);
		projectService.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("project", id.toString())).build();
	}

}
