package org.measure.platform.restapi.analysis;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.measure.platform.core.api.entitys.AnalysisCardService;
import org.measure.platform.core.api.entitys.ProjectAnalysisService;
import org.measure.platform.core.entity.AnalysisCard;
import org.measure.platform.core.entity.ProjectAnalysis;
import org.measure.platform.restapi.analysis.dto.AnalysisConfiguration;
import org.measure.platform.restapi.analysis.dto.CardConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

@RestController
@RequestMapping(value = "api/analysis")
public class AnalysisConfigurationResource {

	private final Logger log = LoggerFactory.getLogger(AnalysisConfigurationResource.class);

	private ProjectAnalysisService projectAnalysisService;
	
	private AnalysisCardService analysisCardService;

	/**
	 * PUT /configure : Configure a project analysis
	 * 
	 * @param analysisTool the AnalysisTool to update
	 * @return the ResponseEntity with status 200 (OK) and with body the updated AnalysisTool, or with status 400 (Bad Request) if the project is
	 *         not valid, or with status 500 (Internal Server Error) if the project couldnt be updated
	 * @throws java.net.URISyntaxException if the Location URI syntax is incorrect
	 */
	@PutMapping("/configure")
	@Timed
	public void configureAnalysisTool(@Valid @RequestBody AnalysisConfiguration analysisConfiguration) throws URISyntaxException {
		log.debug("REST request to configure the analysis servicee : {}", analysisConfiguration);
		ProjectAnalysis analysis = projectAnalysisService.findOne(analysisConfiguration.getProjectAnalysisId());
		analysis.setViewUrl(analysisConfiguration.getViewUrl());
		analysis.setConfigurationUrl(analysisConfiguration.getConfigurationUrl());
		projectAnalysisService.save(analysis);
		
		for(CardConfiguration card : analysisConfiguration.getCards()){
			AnalysisCard mcard = new AnalysisCard();
			mcard.setCardLabel(card.getLabel());
			mcard.setCardUrl(card.getCardUrl());
			mcard.setProjectanalysis(analysis);
			analysisCardService.save(mcard);
		}
	}

}
