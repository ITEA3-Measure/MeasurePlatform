package org.measure.platform.restapi.analysis;

import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.measure.platform.restapi.framework.rest.util.HeaderUtil;
import org.measure.platform.service.analysis.api.IAnalysisCatalogueService;
import org.measure.platform.service.analysis.data.AnalysisService;
import org.measure.platform.service.analysis.data.RegistredAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/analysis")
public class AnalysisRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(AnalysisRegistrationResource.class);
    
    
    @Inject
    IAnalysisCatalogueService analysisRepository;

    /**
     * PUT /register : Register an external analysis tool
     * @param analysisTool the AnalysisTool to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated
     * AnalysisTool, or with status 400 (Bad Request) if the project is not
     * valid, or with status 500 (Internal Server Error) if the project
     * couldnt be updated
     * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/register")
    @Timed
    public ResponseEntity<AnalysisService> registerAnalysisTool(@Valid @RequestBody AnalysisService analysisTool) throws URISyntaxException {
        log.debug("REST request to update RegisterAnalysisTool : {}", analysisTool);
        analysisRepository.registerAnalysisService(analysisTool);       
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("AnalysisTool Registred", analysisTool.getName())).body(analysisTool);
    }
    
    
    /**
     * GET  /list : get all the registred analysis services.
     * @return the ResponseEntity with status 200 (OK) and the list of analysis service in body
     */
    @GetMapping("/list")
    @Timed
    public List<RegistredAnalysisService> getAllAnalysisService() {
        log.debug("REST request to get all AnalysisService");
        return this.analysisRepository.getAllAnalysisService();
    }

    /**
     *  GET /list/{id} : get an analysis service by is name
     * @param id id of the required analysis service
     * @return  the ResponseEntity with status 200 (OK) and the requested analysis service in body
     */
    @GetMapping("/list/{id}")
    @Timed
    public AnalysisService getAnalysisByName(@PathVariable String id) {
        return this.analysisRepository.getAnalysisServiceByName(id);
    }

}
