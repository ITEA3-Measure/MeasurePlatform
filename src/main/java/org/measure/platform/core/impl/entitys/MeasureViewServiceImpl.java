package org.measure.platform.core.impl.entitys;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.measure.platform.core.api.IMeasureVisaulisationManagement;
import org.measure.platform.core.api.entitys.AnalysisCardService;
import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.api.entitys.PhaseService;
import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.AnalysisCard;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Phase;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.impl.repository.MeasureViewRepository;
import org.measure.platform.service.measurement.api.IElasticsearchIndexManager;
import org.measure.platform.service.measurement.impl.IndexFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MeasureView.
 */
@Service
@Transactional
public class MeasureViewServiceImpl implements MeasureViewService {
    private final Logger log = LoggerFactory.getLogger(MeasureViewServiceImpl.class);



    @Inject
    private MeasureViewRepository measureViewRepository;

    @Inject
    private ProjectService projectService;
    
    @Inject
    private MeasureInstanceService measureInstanceService;
    
    @Inject
    private AnalysisCardService analysisCardService;

    @Inject
    private PhaseService phaseService;

    @Inject
    private DashboardService dashboardService;
    
    @Inject
    private IMeasureVisaulisationManagement visualisationManager;


    /**
     * Save a measureView.
     * @param measureView the entity to save
     * @return the persisted entity
     */
    public MeasureView save(MeasureView measureView) {
        log.debug("Request to save MeasureView : {}", measureView);
        
        String mode = measureView.getMode();
        if ("AUTO".equals(mode)) {
            measureView.setViewData(visualisationManager.formatViewDataAsKibanaURL(measureView));
        } else if ("KVIS".equals(mode)) {
            measureView.setViewData(visualisationManager.formatViewDataAsKibanaViewReference(measureView));
        } else if ("KDASH".equals(mode)) {
            measureView.setViewData(visualisationManager.formatViewDataAsKibanaDashboardReference(measureView));
        } else if ("CARD".equals(mode)) {
            measureView.setViewData(visualisationManager.formatViewDataAsAnalysisCard(measureView));
        }
        
        MeasureView result = measureViewRepository.save(measureView);
        return result;
    }

    /**
     * Get all the measureViews.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MeasureView> findAll() {
        log.debug("Request to get all MeasureViews");
        List<MeasureView> result = measureViewRepository.findAll();
        return result;
    }

    /**
     * Get one measureView by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MeasureView findOne(Long id) {
        log.debug("Request to get MeasureView : {}", id);
        MeasureView measureView = measureViewRepository.findOne(id);
        return measureView;
    }

    /**
     * Delete the measureView by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeasureView : {}", id);
        measureViewRepository.delete(id);
    }

    public List<MeasureView> findByProject(Long id) {
        Project project = projectService.findOne(id);
        return measureViewRepository.findByProject(project);
    }
    
    public List<MeasureView> findDefaulsByMeasureInstance(Long id) {
        MeasureInstance instance = measureInstanceService.findOne(id);
        return measureViewRepository.findDefaultViewByMeasureInstance(instance);
    }

    public List<MeasureView> findByProjectOverview(Long id) {
        Project project = projectService.findOne(id);
        return measureViewRepository.findByProjectOverview(project);
    }

    public List<MeasureView> findByPhase(Long id) {
        Phase phase = phaseService.findOne(id);
        return measureViewRepository.findByPhase(phase);
    }

    public List<MeasureView> findByPhaseOverview(Long id) {
        Phase phase = phaseService.findOne(id);
        return measureViewRepository.findByPhaseOverview(phase);
    }

    public List<MeasureView> findByDashboard(Long id) {
        Dashboard dashboard = dashboardService.findOne(id);
        return measureViewRepository.findByDashboard(dashboard);
    }
    
	@Override
	public List<MeasureView> findByAnalysisCard(Long id) {
		AnalysisCard analysisCard = analysisCardService.findOne(id);
		return measureViewRepository.findByAnalysisCard(analysisCard);
	}



}
