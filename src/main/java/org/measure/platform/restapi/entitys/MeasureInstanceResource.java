package org.measure.platform.restapi.entitys;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.NotificationService;
import org.measure.platform.core.api.entitys.enumeration.NotificationType;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.Notification;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.analysis.api.IAlertEngineService;
import org.measure.platform.service.analysis.data.alert.AlertData;
import org.measure.platform.service.analysis.data.alert.AlertProperty;
import org.measure.platform.service.analysis.data.alert.AlertType;
import org.measure.platform.service.smmengine.api.ISchedulingService;
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
	private IAlertEngineService alertEngineService;
	
    @Inject
    private MeasureInstanceService measureInstanceService;

    @Inject
    private ISchedulingService shedulingService;

    @Inject
    private NotificationService notificationService;


//    @Inject
//    private IMeasureCatalogueService catalogueService;
//
//    @Inject
//    private IRemoteCatalogueService remoteCatalogueService;

    /**
     * POST  /measure-instances : Create a new measureInstance.
     * @param measureInstance the measureInstance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new measureInstance, or with status 400 (Bad Request) if the measureInstance has already an ID
     * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/measure-instances")
    @Timed
    public ResponseEntity<MeasureInstance> createMeasureInstance(@Valid @RequestBody MeasureInstance measureInstance) throws URISyntaxException {
        log.debug("REST request to save MeasureInstance : {}", measureInstance);
        if (measureInstance.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("measureInstance", "idexists", "A new measureInstance cannot already have an ID")).body(null);
        }
        
        // Save Measure Instance
        MeasureInstance result = measureInstanceService.save(measureInstance);
               
        
        // Create Index mapping for measure Unite
//        SMMMeasure measureDefinition =  null;     
//        if(measureInstance.isIsRemote()){
//            measureDefinition = remoteCatalogueService.getMeasureByName(measureInstance.getMeasureName(), measureInstance.getRemoteLabel());
//        }else{
//            measureDefinition = catalogueService.getMeasure(measureInstance.getMeasureName());
//        }
        
        
	
		AlertData alert = new AlertData();
		alert.setAlertType(AlertType.MEASURE_ADDED.name());
		alert.setProjectId(measureInstance.getProject().getId());		
		alert.getProperties().add(new AlertProperty(AlertType.MEASURE_ADDED.getProperties().get(0), result.getInstanceName()));
		alertEngineService.alert(alert);
        
        // Create Notification related to the creation of the  new Measure Instance
        Notification notif = new Notification();
        notif.setNotificationTitle("Measure Registred");
        notif.setNotificationContent("The "+measureInstance.getInstanceName()+ " has bed registre into the project");       
        LocalDateTime ldt = LocalDateTime.ofInstant( new Date().toInstant(), ZoneId.systemDefault());   
        notif.setNotificationDate(ZonedDateTime.of(ldt,ZoneId.systemDefault()));
        notif.setNotificationType(NotificationType.INFO);
        notif.setProject(result.getProject());
        notificationService.save(notif);
        return ResponseEntity.created(new URI("/api/measure-instances/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert("measureInstance", result.getId().toString()))
                    .body(result);
    }

    /**
     * PUT  /measure-instances : Updates an existing measureInstance.
     * @param measureInstance the measureInstance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated measureInstance,
     * or with status 400 (Bad Request) if the measureInstance is not valid,
     * or with status 500 (Internal Server Error) if the measureInstance couldnt be updated
     * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
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
    
    
    @GetMapping("/existing-measure/{name}")
    @Timed
    public MeasureInstance isExistingMeasure(@PathVariable(name="name") String name) { 
    	List<MeasureInstance> result =  measureInstanceService.findMeasureInstancesByName(name) ;
        if(result!= null && !result.isEmpty()){
        	return result.get(0);
        }
        return null;
    }


    /**
     * GET  /measure-instances/:id : get the "id" measureInstance.
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
     * @param id the id of the measureInstance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/measure-instances/{id}")
    @Timed
    public ResponseEntity<Void> deleteMeasureInstance(@PathVariable Long id) {
        log.debug("REST request to delete MeasureInstance : {}", id);     
        
        MeasureInstance measureInstance = measureInstanceService.findOne(id);
             
		AlertData alert = new AlertData();
		alert.setAlertType(AlertType.MEASURE_ADDED.name());
		alert.setProjectId(measureInstance.getProject().getId());		
		alert.getProperties().add(new AlertProperty(AlertType.MEASURE_REMOVED.getProperties().get(0), measureInstance.getInstanceName()));
		alertEngineService.alert(alert);
		
        try{
            Notification notif = new Notification();
            notif.setNotificationTitle("Measure Deletion");
            notif.setNotificationContent("The "+measureInstance.getInstanceName()+ " has bean removed form the project");       
            LocalDateTime ldt = LocalDateTime.ofInstant( new Date().toInstant(), ZoneId.systemDefault());   
            notif.setNotificationDate(ZonedDateTime.of(ldt,ZoneId.systemDefault()));
            notif.setNotificationType(NotificationType.INFO);
            notif.setProject(measureInstance.getProject());
            notificationService.save(notif);
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        this.shedulingService.removeMeasure(id);
        this.measureInstanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("measureInstance", id.toString())).build();
    }

}
