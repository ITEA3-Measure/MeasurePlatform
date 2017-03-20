package org.measure.platform.restapi.app.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasurePropertyService;
import org.measure.platform.core.entity.MeasureProperty;
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
 * REST controller for managing MeasureProperty.
 */
@RestController
@RequestMapping("/api")
public class MeasurePropertyResource {

    private final Logger log = LoggerFactory.getLogger(MeasurePropertyResource.class);
        
    @Inject
    private MeasurePropertyService measurePropertyService;
    
    @Inject
    private MeasureInstanceService measureInstanceService;

    /**
     * POST  /measure-properties : Create a new measureProperty.
     *
     * @param measureProperty the measureProperty to create
     * @return the ResponseEntity with status 201 (Created) and with body the new measureProperty, or with status 400 (Bad Request) if the measureProperty has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measure-properties")
    @Timed
    public ResponseEntity<MeasureProperty> createMeasureProperty(@Valid @RequestBody MeasureProperty measureProperty) throws URISyntaxException {
        log.debug("REST request to save MeasureProperty : {}", measureProperty);
        if (measureProperty.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("measureProperty", "idexists", "A new measureProperty cannot already have an ID")).body(null);
        }
        MeasureProperty result = measurePropertyService.save(measureProperty);
        return ResponseEntity.created(new URI("/api/measure-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("measureProperty", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /measure-properties : Updates an existing measureProperty.
     *
     * @param measureProperty the measureProperty to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measureProperty,
     * or with status 400 (Bad Request) if the measureProperty is not valid,
     * or with status 500 (Internal Server Error) if the measureProperty couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/measure-properties")
    @Timed
    public ResponseEntity<MeasureProperty> updateMeasureProperty(@Valid @RequestBody MeasureProperty measureProperty) throws URISyntaxException {
        log.debug("REST request to update MeasureProperty : {}", measureProperty);
        if (measureProperty.getId() == null) {
            return createMeasureProperty(measureProperty);
        }
        MeasureProperty result = measurePropertyService.save(measureProperty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("measureProperty", measureProperty.getId().toString()))
            .body(result);
    }

    /**
     * GET  /measure-properties : get all the measureProperties.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of measureProperties in body
     */
    @GetMapping("/measure-properties")
    @Timed
    public List<MeasureProperty> getAllMeasureProperties() {
        log.debug("REST request to get all MeasureProperties");
        return measurePropertyService.findAll();
    }

    /**
     * GET  /measure-properties/:id : get the "id" measureProperty.
     *
     * @param id the id of the measureProperty to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the measureProperty, or with status 404 (Not Found)
     */
    @GetMapping("/measure-properties/{id}")
    @Timed
    public ResponseEntity<MeasureProperty> getMeasureProperty(@PathVariable Long id) {
        log.debug("REST request to get MeasureProperty : {}", id);
        MeasureProperty measureProperty = measurePropertyService.findOne(id);
        return Optional.ofNullable(measureProperty)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /measure-properties/:id : delete the "id" measureProperty.
     *
     * @param id the id of the measureProperty to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measure-properties/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeasureProperty(@PathVariable Long id) {
        log.debug("REST request to delete MeasureProperty : {}", id);
        measurePropertyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("measureProperty", id.toString())).build();
    }
    
    /**
     * GET  /measure-properties : get all the measureProperties.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of measureProperties in body
     */
    @GetMapping("/measure-properties/byinstance/{id}")
    @Timed
    public List<MeasureProperty> getMeasurePropertiesByInstanceId(@PathVariable Long id) {	
    	return measurePropertyService.findByInstance(measureInstanceService.findOne(id));
    }

}
