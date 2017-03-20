package org.measure.platform.restapi.app.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.smmengine.api.IShedulingService;
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
 * REST controller for managing MeasureInstance.
 */
@RestController
@RequestMapping("/api")
public class MeasureInstanceResource {

    private final Logger log = LoggerFactory.getLogger(MeasureInstanceResource.class);
        
    @Inject
    private MeasureInstanceService measureInstanceService;
    
    @Inject
	private IShedulingService shedulingService;

    /**
     * POST  /measure-instances : Create a new measureInstance.
     *
     * @param measureInstance the measureInstance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new measureInstance, or with status 400 (Bad Request) if the measureInstance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measure-instances")
    @Timed
    public ResponseEntity<MeasureInstance> createMeasureInstance(@Valid @RequestBody MeasureInstance measureInstance) throws URISyntaxException {
        log.debug("REST request to save MeasureInstance : {}", measureInstance);
        if (measureInstance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("measureInstance", "idexists", "A new measureInstance cannot already have an ID")).body(null);
        }
        MeasureInstance result = measureInstanceService.save(measureInstance);
        return ResponseEntity.created(new URI("/api/measure-instances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("measureInstance", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /measure-instances : Updates an existing measureInstance.
     *
     * @param measureInstance the measureInstance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measureInstance,
     * or with status 400 (Bad Request) if the measureInstance is not valid,
     * or with status 500 (Internal Server Error) if the measureInstance couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/measure-instances")
    @Timed
    public ResponseEntity<MeasureInstance> updateMeasureInstance(@Valid @RequestBody MeasureInstance measureInstance) throws URISyntaxException {
        log.debug("REST request to update MeasureInstance : {}", measureInstance);
        if (measureInstance.getId() == null) {
            return createMeasureInstance(measureInstance);
        }
        MeasureInstance result = measureInstanceService.save(measureInstance);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("measureInstance", measureInstance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /measure-instances : get all the measureInstances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of measureInstances in body
     */
    @GetMapping("/measure-instances")
    @Timed
    public List<MeasureInstance> getAllMeasureInstances() {
        log.debug("REST request to get all MeasureInstances");
        return measureInstanceService.findAll();
    }
    
    
    
    
    /**
     * GET  /measure-instances : get all the measureInstances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of measureInstances in body
     */
    @GetMapping("/measure-instances/bymeasure/{measureRef}")
    @Timed
    public List<MeasureInstance> getMeasureInstancesByMeasureReference(@PathVariable(name="measureRef") String measureRef) {
    	return measureInstanceService.findMeasureInstanceByReference(measureRef);
    }
    
    @GetMapping("/project-measure-instances/{id}")
    @Timed
    public List<MeasureInstance> getProjectMeasureInstances(@PathVariable(name="id") Long id) {
        log.debug("REST request to get all MeasureInstances");      
        List<MeasureInstance> result = measureInstanceService.findMeasureInstancesByProject(id);        
        return result;
    }

    /**
     * GET  /measure-instances/:id : get the "id" measureInstance.
     *
     * @param id the id of the measureInstance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the measureInstance, or with status 404 (Not Found)
     */
    @GetMapping("/measure-instances/{id}")
    @Timed
    public ResponseEntity<MeasureInstance> getMeasureInstance(@PathVariable Long id) {
        log.debug("REST request to get MeasureInstance : {}", id);
        MeasureInstance measureInstance = measureInstanceService.findOne(id);
        return Optional.ofNullable(measureInstance)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /measure-instances/:id : delete the "id" measureInstance.
     *
     * @param id the id of the measureInstance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measure-instances/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeasureInstance(@PathVariable Long id) {
        log.debug("REST request to delete MeasureInstance : {}", id);
        this.shedulingService.removeMeasure(id);
        this.measureInstanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("measureInstance", id.toString())).build();
    }

}
