package org.measure.platform.restapi.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.data.api.IApplicationService;
import org.measure.platform.core.data.entity.Application;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Application instance.
 */
@RestController
@RequestMapping("/api")
public class ApplicationInstanceResource {
    private final Logger log = LoggerFactory.getLogger(ApplicationInstanceResource.class);

    @Inject
    private IApplicationService applicationInstanceService;

    
    /**
     * POST  /application-instances : Create a new applicationInstance.
     * @param applicationInstance the applicationInstance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new applicationInstance, or with status 400 (Bad Request) if the applicationInstance has already an ID
     * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
     */
//    @PostMapping("/application-instances")
//    @Timed
//    public ResponseEntity<Application> createApplicationInstance(@Valid @RequestBody Application applicationInstance) throws URISyntaxException {
//        log.debug("REST request to save Application : {}", applicationInstance);
//        if (applicationInstance.getId() != null) {
//            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("applicationInstance", "idexists", "A new applicationInstance cannot already have an ID")).body(null);
//        }
//        
//        // Save Application Instance
//        Application result = applicationInstanceService.save(applicationInstance);
//        
//        return ResponseEntity.created(new URI("/api/application-instances/" + result.getId()))
//                    .headers(HeaderUtil.createEntityCreationAlert("applicationInstance", result.getId().toString()))
//                    .body(result);
//    }

    /**
     * PUT  /application-instances : Updates an existing applicationInstance.
     * @param applicationInstance the applicationInstance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated applicationInstance,
     * or with status 400 (Bad Request) if the applicationInstance is not valid,
     * or with status 500 (Internal Server Error) if the applicationInstance couldnt be updated
     * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
     */
//    @PutMapping("/application-instances")
//    @Timed
//    public ResponseEntity<Application> updateApplicationInstance(@Valid @RequestBody Application applicationInstance) throws URISyntaxException {
//        log.debug("REST request to update Application : {}", applicationInstance);
//        if (applicationInstance.getId() == null) {
//            return createApplicationInstance(applicationInstance);
//        }
//        Application result = applicationInstanceService.save(applicationInstance);
//        return ResponseEntity.ok()
//                    .headers(HeaderUtil.createEntityUpdateAlert("applicationInstance", applicationInstance.getId().toString()))
//                    .body(result);
//    }


    @GetMapping("/project-application-instances/{id}")
    @Timed
    public List<Application> getProjectApplicationInstances(@PathVariable(name="id") Long id) {
        log.debug("REST request to get all Application instances");      
        List<Application> result = applicationInstanceService.findApplicationInstancesByProject(id);
        return result;
    }
    
    
    @GetMapping("/existing-application/{name}")
    @Timed
    public Application isExistingApplication(@PathVariable(name="name") String name) { 
    	List<Application> result =  applicationInstanceService.findApplicationInstancesByName(name) ;
        if(result!= null && !result.isEmpty()){
        	return result.get(0);
        }
        return null;
    }
    

    /**
     * GET  /application-instances/:id : get the "id" applicationInstance.
     * @param id the id of the Application to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the applicationInstance, or with status 404 (Not Found)
     */
    @GetMapping("/application-instances/{id}")
    @Timed
    public ResponseEntity<Application> getApplicationInstance(@PathVariable Long id) {
        log.debug("REST request to get Application : {}", id);
        Application applicationInstance = applicationInstanceService.findOne(id);
        return Optional.ofNullable(applicationInstance)
                    .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}