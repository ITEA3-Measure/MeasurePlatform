package org.measure.platform.restapi.application;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.application.api.IApplicationConfigurationService;
import org.measure.platform.service.application.impl.dto.ApplicationConfiguration;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/")
public class ApplicationConfigurationResource {

	private final Logger log = LoggerFactory.getLogger(ApplicationConfigurationResource.class);

	@Inject
	private IApplicationConfigurationService configurationService;


	@GetMapping("application-configuration/{id}")
	@Timed
	public ResponseEntity<ApplicationConfiguration> getApplicationConfiguration(@PathVariable Long id) {	

		log.debug("REST request to get ApplicationInstanceConfiguration");      
		ApplicationConfiguration applicationInstanceConfiguration = configurationService.getConfiguration(id);

        return Optional.ofNullable(applicationInstanceConfiguration)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	
	
	@GetMapping("application-configuration/by-application-type")
	@Timed
	public ResponseEntity<ApplicationConfiguration> getApplicationConfiguration(@RequestParam("applicationType") String applicationType) {	

		log.debug("REST request to get ApplicationInstanceConfiguration");      
		ApplicationConfiguration applicationInstanceConfiguration = configurationService.getConfigurationByType(applicationType);

        return Optional.ofNullable(applicationInstanceConfiguration)
                .map(result -> new ResponseEntity<>(
                    result,
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@PostMapping("application-configuration")
	@Timed
	public ResponseEntity<ApplicationConfiguration> createApplicationConfiguration(@Valid @RequestBody ApplicationConfiguration applicationInstance) throws URISyntaxException {
		log.debug("REST request createApplicationInstanceConfiguration");      

		ApplicationConfiguration applicationInstanceConfigurationResult = configurationService.createApplication(applicationInstance);


		return ResponseEntity.created(new URI("/api/application-instance-configuration/" + applicationInstanceConfigurationResult.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("applicationInstance", applicationInstanceConfigurationResult.getId().toString()))
				.body(applicationInstanceConfigurationResult);    	

	}

	@PutMapping("/application-configuration")
	@Timed
	public ResponseEntity<ApplicationConfiguration> updateApplicationConfiguration(@Valid @RequestBody ApplicationConfiguration applicationInstance) throws URISyntaxException {
		
		log.debug("REST request updateApplicationInstanceConfiguration");      

        if (applicationInstance.getId() == null) {
            return createApplicationConfiguration(applicationInstance);
        }
		ApplicationConfiguration applicationInstanceConfigurationResult = configurationService.updateApplication(applicationInstance);

        return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("applicationInstance", applicationInstance.getId().toString()))
                    .body(applicationInstanceConfigurationResult);

	}

	@DeleteMapping("application-configuration/{id}")
	@Timed
	public ResponseEntity<Void> deleteApplicationConfiguration(@PathVariable Long id) {
        log.debug("REST request to delete application instance : {}", id);     

        configurationService.deleteApplicaionInstance(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("applicationInstance", id.toString())).build();

	}

}
