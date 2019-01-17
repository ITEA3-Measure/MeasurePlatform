package org.measure.platform.restapi.application;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.measure.platform.core.entity.Application;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.application.impl.dto.ApplicationInstanceConfiguration;
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

@RestController
@RequestMapping(value = "api/")
public class ApplicationInstanceConfigurationResource {
	

	@GetMapping("application-instance-configuration/{id}")
	@Timed
	public ResponseEntity<ApplicationInstanceConfiguration> getApplicationConfiguration(@PathVariable String id) {	
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
    @PostMapping("application-instance-configuration")
    @Timed
    public ResponseEntity<ApplicationInstanceConfiguration> createApplicationInstanceConfiguration(@Valid @RequestBody ApplicationInstanceConfiguration applicationInstance) throws URISyntaxException {
    	return null;
    }
    
    @PutMapping("/application-instance-configuration")
    @Timed
    public ResponseEntity<ApplicationInstanceConfiguration> updateApplicationInstanceConfiguration(@Valid @RequestBody ApplicationInstanceConfiguration applicationInstance) throws URISyntaxException {
    	return null;
    }

	@DeleteMapping("application-instance-configuration/{id}")
	@Timed
	public ResponseEntity<Void> deleteApplicationInstanceConfiguration(@PathVariable String id) {
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("application", id)).build();
	}

}
