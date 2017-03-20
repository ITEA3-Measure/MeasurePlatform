package org.measure.platform.restapi.app.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.entity.MeasureView;
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
 * REST controller for managing MeasureView.
 */
@RestController
@RequestMapping("/api")
public class MeasureViewResource {

    private final Logger log = LoggerFactory.getLogger(MeasureViewResource.class);
        
    @Inject
    private MeasureViewService measureViewService;

    /**
     * POST  /measure-views : Create a new measureView.
     *
     * @param measureView the measureView to create
     * @return the ResponseEntity with status 201 (Created) and with body the new measureView, or with status 400 (Bad Request) if the measureView has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measure-views")
    @Timed
    public ResponseEntity<MeasureView> createMeasureView(@RequestBody MeasureView measureView) throws URISyntaxException {
        log.debug("REST request to save MeasureView : {}", measureView);
        if (measureView.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("measureView", "idexists", "A new measureView cannot already have an ID")).body(null);
        }
        MeasureView result = measureViewService.save(measureView);
        return ResponseEntity.created(new URI("/api/measure-views/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("measureView", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /measure-views : Updates an existing measureView.
     *
     * @param measureView the measureView to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measureView,
     * or with status 400 (Bad Request) if the measureView is not valid,
     * or with status 500 (Internal Server Error) if the measureView couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/measure-views")
    @Timed
    public ResponseEntity<MeasureView> updateMeasureView(@RequestBody MeasureView measureView) throws URISyntaxException {
        log.debug("REST request to update MeasureView : {}", measureView);
        if (measureView.getId() == null) {
            return createMeasureView(measureView);
        }
        MeasureView result = measureViewService.save(measureView);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("measureView", measureView.getId().toString()))
            .body(result);
    }

    /**
     * GET  /measure-views : get all the measureViews.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of measureViews in body
     */
    @GetMapping("/measure-views")
    @Timed
    public List<MeasureView> getAllMeasureViews() {
        log.debug("REST request to get all MeasureViews");
        return measureViewService.findAll();
    }

    /**
     * GET  /measure-views/:id : get the "id" measureView.
     *
     * @param id the id of the measureView to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the measureView, or with status 404 (Not Found)
     */
    @GetMapping("/measure-views/{id}")
    @Timed
    public ResponseEntity<MeasureView> getMeasureView(@PathVariable Long id) {
        log.debug("REST request to get MeasureView : {}", id);
        MeasureView measureView = measureViewService.findOne(id);
        return Optional.ofNullable(measureView)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /measure-views/:id : delete the "id" measureView.
     *
     * @param id the id of the measureView to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measure-views/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeasureView(@PathVariable Long id) {
        log.debug("REST request to delete MeasureView : {}", id);
        measureViewService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("measureView", id.toString())).build();
    }
    
    
    @GetMapping("/measureview/byproject/{id}")
    @Timed
    public List<MeasureView> getMeasureViewByProject(@PathVariable Long id) {	
        return measureViewService.findByProject(id);
    }
    
    @GetMapping("/measureview/byprojectoverview/{id}")
    @Timed
    public List<MeasureView> getMeasureViewByProjectOverview(@PathVariable Long id) {	
        return measureViewService.findByProjectOverview(id);
    }
    
    @GetMapping("/measureview/byphase/{id}")
    @Timed
    public List<MeasureView> getMeasureViewByPhase(@PathVariable Long id) {	
        return measureViewService.findByPhase(id);
    }
    
    @GetMapping("/measureview/byphaseoverview/{id}")
    @Timed
    public List<MeasureView> getMeasureViewByPhaseOverview(@PathVariable Long id) {	
        return measureViewService.findByPhaseOverview(id);
    }
    
    @GetMapping("/measureview/bydashboard/{id}")
    @Timed
    public List<MeasureView> getMeasureViewByDashboard(@PathVariable Long id) {	
        return measureViewService.findByDashboard(id);
    }

}
