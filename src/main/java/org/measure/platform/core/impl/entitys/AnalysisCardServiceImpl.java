package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.AnalysisCardService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.entity.AnalysisCard;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.entity.ProjectAnalysis;
import org.measure.platform.core.impl.repository.AnalysisCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing AnalysisCard.
 */
@Service
@Transactional
public class AnalysisCardServiceImpl implements AnalysisCardService {
	private final Logger log = LoggerFactory.getLogger(AnalysisCardServiceImpl.class);

	@Inject
	private AnalysisCardRepository analysisRepository;

	@Inject
	private MeasureViewService measureViewService;

	@Override
	public AnalysisCard save(AnalysisCard card) {
		log.debug("Request to save AnalysisCard : {}", card);
		AnalysisCard result = analysisRepository.save(card);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnalysisCard> findAll() {
		List<AnalysisCard> result = analysisRepository.findAll();
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnalysisCard> findAllByProjectAnalysis(ProjectAnalysis projectAnalysis) {
		
		List<AnalysisCard> result = analysisRepository.findByProjectAnalysis(projectAnalysis);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public AnalysisCard findOne(Long id) {
		log.debug("Request to get Phase : {}", id);
		AnalysisCard card = analysisRepository.findOne(id);
		return card;
	}

	@Override
	public void delete(Long id) {
		for (MeasureView view : measureViewService.findByAnalysisCard(id)) {
			measureViewService.delete(view.getId());
		}

		analysisRepository.delete(id);
	}

	@Override
	public List<AnalysisCard> findAllByProject(Project project) {
		return analysisRepository.findByProject(project);
	}

}
