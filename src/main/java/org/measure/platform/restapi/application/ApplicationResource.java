package org.measure.platform.restapi.application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.measure.platform.core.catalogue.api.IApplicationCatalogueService;
import org.measure.platform.core.measurement.impl.ElasticMeasurementStorage;
import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.smm.application.model.SMMApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/application")
public class ApplicationResource {
	private final Logger log = LoggerFactory.getLogger(ElasticMeasurementStorage.class);

	@Inject
	private IApplicationCatalogueService applicationsCatalogue;

	
	/**
	 * GET /applications : get all applications.
	 * 
	 * @return the ResponseEntity with status 200 (OK) and the list of applications
	 *         in body
	 */
	@GetMapping("/findall")
	@Timed
	public List<SMMApplication> getAllApplications() {
		List<SMMApplication> result = new ArrayList<>();
		result.addAll(this.applicationsCatalogue.getAllApplications());
		return result;
	}
	
	/**
	 * PUT /upload : Upload a packaged application into the platform application storage
	 * @param data Packaged application
	 */
	@PostMapping("/upload")
	public void handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			byte[] fileData = file.getBytes();
			File tempFile = File.createTempFile(file.getOriginalFilename(), ".application");
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tempFile));
			stream.write(fileData);
			stream.close();
			applicationsCatalogue.storeApplication(tempFile.toPath());
			tempFile.delete();
		} catch (IOException e) {
			log.error("File Upload Error", e);
		}
	}
	
	/**
	 * GET /application/:id : get the "id" application.
	 * 
	 * @param id
	 *            the id of the application to retrieve
	 * @return the ResponseEntity with status 200 (OK) and with body the
	 *         measure, or with status 404 (Not Found)
	 */
	@GetMapping("/{id}")
	@Timed
	public ResponseEntity<SMMApplication> getApplication(@PathVariable String id) {
		if (id != null) {
			SMMApplication application = applicationsCatalogue.getApplication(id);
			return Optional.ofNullable(application).map(result -> new ResponseEntity<>(result, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
		}
		
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}


	/**
	 * DELETE /application/:id : delete the "id" application.
	 * 
	 * @param id
	 *            the id of the measure to delete
	 * @return the ResponseEntity with status 200 (OK)
	 */
	@DeleteMapping("/{id}")
	@Timed
	public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
		applicationsCatalogue.deleteApplication(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("application", id)).build();
	}
	

}
