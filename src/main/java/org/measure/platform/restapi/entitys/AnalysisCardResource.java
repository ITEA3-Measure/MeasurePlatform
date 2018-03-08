package org.measure.platform.restapi.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.AnalysisCardService;
import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.AnalysisCard;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.analysis.api.IAlertEngineService;
import org.measure.platform.service.analysis.data.alert.AlertData;
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
 * REST controller for managing AnalysisCard.
 */
@RestController
@RequestMapping("/api")
public class AnalysisCardResource {
	private final Logger log = LoggerFactory.getLogger(AnalysisCardResource.class);

	@Inject
	private AnalysisCardService analysisCardService;
	
    @Inject
    private ProjectService projectService;
	

	/**
	 * POST /analysisCard : Create a new analysisCard.
	 * 
	 * @param analysisCard the analysisCard to create
	 * @return the ResponseEntity with status 201 (Created) and with body the new analysisCard, or with status 400 (Bad Request) if the analysisCard has already an ID
	 * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
	 */
	@PostMapping("/analysiscard")
	@Timed
	public ResponseEntity<AnalysisCard> createAnalysisCard(@Valid @RequestBody AnalysisCard analysisCard)
			throws URISyntaxException {
		log.debug("REST request to save AnalysisCard : {}", analysisCard);
		if (analysisCard.getId() != null) {
			return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("analysisCard", "idexists",
					"A new analysisCard cannot already have an ID")).body(null);
		}
		AnalysisCard result = analysisCardService.save(analysisCard);
		

		return ResponseEntity.created(new URI("/api/analysiscard/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("analysiscard", result.getId().toString()))
				.body(result);
	}

	/**
	 * PUT /analysisCards : Updates an existing analysisCard.
	 * 
	 * @param analysisCard the analysisCard to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated analysisCard, or with status 400 (Bad Request) if the analysisCard is not valid, 
	 * or with status 500 (Internal Server Error) if the analysisCard couldnt be updated
	 * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/analysiscard")
	@Timed
	public ResponseEntity<AnalysisCard> updateAnalysisCard(@Valid @RequestBody AnalysisCard analysisCard)
			throws URISyntaxException {
		log.debug("REST request to update AnalysisCard : {}", analysisCard);
		if (analysisCard.getId() == null) {
			return createAnalysisCard(analysisCard);
		}
		AnalysisCard result = analysisCardService.save(analysisCard);
		return ResponseEntity.ok()
				.headers(HeaderUtil.createEntityUpdateAlert("analysisCard", analysisCard.getId().toString()))
				.body(result);
	}

	/**
	 * GET /analysisCards : get all the analysisCards.
	 * 
	 * @return the ResponseEntity with status 200 (OK) and the list of  analysisCards in body
	 */
	@GetMapping("/analysiscard")
	@Timed
	public List<AnalysisCard> getAllAnalysisCards() {
		log.debug("REST request to get all AnalysisCards");
		return analysisCardService.findAll();
	}

    @GetMapping("/analysiscard/byproject/{id}")
    @Timed
    public List<AnalysisCard> getPhasesByProject(@PathVariable Long id) {
        return analysisCardService.findAllByProject(projectService.findOne(id));
    }

	/**
	 * GET /analysisCards/:id : get the "id" analysisCard.
	 * 
	 * @param id
	 *            the id of the analysisCard to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         analysisCard, or with status 404 (Not Found)
	 */
	@GetMapping("/analysiscard/{id}")
	@Timed
	public ResponseEntity<AnalysisCard> getAnalysisCard(@PathVariable Long id) {
		log.debug("REST request to get AnalysisCard : {}", id);
		AnalysisCard analysisCard = analysisCardService.findOne(id);
		return Optional.ofNullable(analysisCard).map(result -> new ResponseEntity<>(result, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /analysisCards/:id : delete the "id" analysisCard.
	 * 
	 * @param id
	 *            the id of the analysisCard to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/analysiscard/{id}")
	@Timed
	public ResponseEntity<Void> deleteAnalysisCard(@PathVariable Long id) {
		log.debug("REST request to delete AnalysisCard : {}", id);
		analysisCardService.delete(id);	
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("analysisCard", id.toString())).build();
	}

}
