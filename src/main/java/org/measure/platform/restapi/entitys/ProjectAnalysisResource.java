package org.measure.platform.restapi.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.ProjectAnalysisService;
import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.ProjectAnalysis;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.analysis.api.IAlertEngineService;
import org.measure.platform.service.analysis.data.alert.AlertData;
import org.measure.platform.service.analysis.data.alert.AlertProperty;
import org.measure.platform.service.analysis.data.alert.AlertType;
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
 * REST controller for managing ProjectAnalysis.
 */
@RestController
@RequestMapping("/api")
public class ProjectAnalysisResource {
	private final Logger log = LoggerFactory.getLogger(ProjectAnalysisResource.class);

	@Inject
	private ProjectAnalysisService projectAnalysisService;
	
	@Inject
	private ProjectService projecService;
	
	@Inject
	private IAlertEngineService alertEngineService;
	
	/**
	 * POST /projectAnalysis : Create a new projectAnalysis.
	 * 
	 * @param projectAnalysis the projectAnalysis to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new projectAnalysis, or with status 400 (Bad Request) if the projectAnalysis has already an ID
	 * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/projectanalysis")
	@Timed
	public ResponseEntity<ProjectAnalysis> createProjectAnalysis(@Valid @RequestBody ProjectAnalysis projectAnalysis)
			throws URISyntaxException {
		log.debug("REST request to save ProjectAnalysis : {}", projectAnalysis);
		if (projectAnalysis.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("projectAnalysis", "idexists",
					"A new projectAnalysis cannot already have an ID")).body(null);
		}
		ProjectAnalysis result = projectAnalysisService.save(projectAnalysis);
		
		AlertData alert = new AlertData();
		alert.setAlertType(AlertType.ANALYSIS_ENABLE.name());
		alert.setProjectId(result.getProject().getId());
		alert.setAlertTool(projectAnalysis.getAnalysisToolId());
		alert.getProperties().add(new AlertProperty(AlertType.ANALYSIS_ENABLE.getResponsProperties().get(0), result.getId().toString()));
		alertEngineService.alert(alert);
		
		return ResponseEntity.created(new URI("/api/projectAnalysiss/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("projectAnalysis", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /projectAnalysiss : Updates an existing projectAnalysis.
	 * 
	 * @param projectAnalysis the projectAnalysis to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated projectAnalysis, or with status 400 (Bad Request) if the projectAnalysis is not valid, 
	 * or with status 500 (Internal Server Error) if the projectAnalysis couldnt be updated
	 * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/projectanalysis")
	@Timed
	public ResponseEntity<ProjectAnalysis> updateProjectAnalysis(@Valid @RequestBody ProjectAnalysis projectAnalysis)
			throws URISyntaxException {
		log.debug("REST request to update ProjectAnalysis : {}", projectAnalysis);
		if (projectAnalysis.getId() == null) {
			return createProjectAnalysis(projectAnalysis);
		}
		ProjectAnalysis result = projectAnalysisService.save(projectAnalysis);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("projectAnalysis", projectAnalysis.getId().toString()))
				.body(result);
	}

	/**
	 * GET /projectAnalysiss : get all the projectAnalysiss.
	 * 
	 * @return the ResponseEntity with status 200 (OK) and the list of  projectAnalysiss in body
	 */
	@GetMapping("/projectanalysis")
	@Timed
	public List<ProjectAnalysis> getAllProjectAnalysiss() {
		log.debug("REST request to get all ProjectAnalysiss");
		return projectAnalysisService.findAll();
	}

    @GetMapping("/projectanalysis/byproject/{id}")
    @Timed
    public List<ProjectAnalysis> getPhasesByProject(@PathVariable Long id) {
        return projectAnalysisService.findAllByProject(projecService.findOne(id));
    }

	/**
	 * GET /projectAnalysiss/:id : get the "id" projectAnalysis.
	 * 
	 * @param id
	 *            the id of the projectAnalysis to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         projectAnalysis, or with status 404 (Not Found)
	 */
	@GetMapping("/projectanalysis/{id}")
	@Timed
	public ResponseEntity<ProjectAnalysis> getProjectAnalysis(@PathVariable Long id) {
		log.debug("REST request to get ProjectAnalysis : {}", id);
		ProjectAnalysis projectAnalysis = projectAnalysisService.findOne(id);
		return Optional.ofNullable(projectAnalysis).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /projectAnalysiss/:id : delete the "id" projectAnalysis.
	 * 
	 * @param id
	 *            the id of the projectAnalysis to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/projectanalysis/{id}")
	@Timed
	public ResponseEntity<Void> deleteProjectAnalysis(@PathVariable Long id) {
		log.debug("REST request to delete ProjectAnalysis : {}", id);
		
		ProjectAnalysis analysis = projectAnalysisService.findOne(id);
		AlertData alert = new AlertData();
		alert.setAlertType(AlertType.ANALYSIS_DESABLE.name());
		alert.setProjectId(analysis.getProject().getId());
		alert.setAlertTool(analysis.getAnalysisToolId());
		alert.getProperties().add(new AlertProperty(AlertType.ANALYSIS_ENABLE.getResponsProperties().get(0),id.toString()));
		alertEngineService.alert(alert);
		
		projectAnalysisService.delete(id);
		
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("projectAnalysis", id.toString()))
				.build();
	}

}
