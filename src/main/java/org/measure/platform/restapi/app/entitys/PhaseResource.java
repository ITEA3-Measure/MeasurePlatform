package org.measure.platform.restapi.app.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.PhaseService;
import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.Phase;
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
 * REST controller for managing Phase.
 */
@RestController
@RequestMapping("/api")
public class PhaseResource {

    private final Logger log = LoggerFactory.getLogger(PhaseResource.class);
        
    @Inject
    private PhaseService phaseService;
    
    @Inject
    private ProjectService projectService;


    /**
     * POST  /phases : Create a new phase.
     *
     * @param phase the phase to create
     * @return the ResponseEntity with status 201 (Created) and with body the new phase, or with status 400 (Bad Request) if the phase has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/phases")
    @Timed
    public ResponseEntity<Phase> createPhase(@Valid @RequestBody Phase phase) throws URISyntaxException {
        log.debug("REST request to save Phase : {}", phase);
        if (phase.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("phase", "idexists", "A new phase cannot already have an ID")).body(null);
        }
        Phase result = phaseService.save(phase);
        return ResponseEntity.created(new URI("/api/phases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("phase", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /phases : Updates an existing phase.
     *
     * @param phase the phase to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated phase,
     * or with status 400 (Bad Request) if the phase is not valid,
     * or with status 500 (Internal Server Error) if the phase couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/phases")
    @Timed
    public ResponseEntity<Phase> updatePhase(@Valid @RequestBody Phase phase) throws URISyntaxException {
        log.debug("REST request to update Phase : {}", phase);
        if (phase.getId() == null) {
            return createPhase(phase);
        }
        Phase result = phaseService.save(phase);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("phase", phase.getId().toString()))
            .body(result);
    }

    /**
     * GET  /phases : get all the phases.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of phases in body
     */
    @GetMapping("/phases")
    @Timed
    public List<Phase> getAllPhases() {
        log.debug("REST request to get all Phases");
        return phaseService.findAll();
    }
    

    @GetMapping("/phases/byproject/{id}")
    @Timed
    public List<Phase> getPhasesByProject(@PathVariable Long id) {	
        return phaseService.findByProject(projectService.findOne(id));
    }
    

    /**
     * GET  /phases/:id : get the "id" phase.
     *
     * @param id the id of the phase to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the phase, or with status 404 (Not Found)
     */
    @GetMapping("/phases/{id}")
    @Timed
    public ResponseEntity<Phase> getPhase(@PathVariable Long id) {
        log.debug("REST request to get Phase : {}", id);
        Phase phase = phaseService.findOne(id);
        return Optional.ofNullable(phase)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /phases/:id : delete the "id" phase.
     *
     * @param id the id of the phase to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/phases/{id}")
    @Timed
    public ResponseEntity<Void> deletePhase(@PathVariable Long id) {
        log.debug("REST request to delete Phase : {}", id);
        phaseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("phase", id.toString())).build();
    }

}
