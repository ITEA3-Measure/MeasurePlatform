package org.measure.platform.core.impl.entitys;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.api.entitys.PhaseService;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Phase;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.impl.repository.PhaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Phase.
 */
@Service
@Transactional
public class PhaseServiceImpl implements PhaseService{

    private final Logger log = LoggerFactory.getLogger(PhaseServiceImpl.class);
    
    @Inject
    private PhaseRepository phaseRepository;
    
    @Inject
    private DashboardService dashboardService;
    
	@Inject
	private MeasureViewService viewService;

    /**
     * Save a phase.
     *
     * @param phase the entity to save
     * @return the persisted entity
     */
    public Phase save(Phase phase) {
        log.debug("Request to save Phase : {}", phase);
        Phase result = phaseRepository.save(phase);
        return result;
    }

    /**
     *  Get all the phases.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Phase> findAll() {
        List<Phase> result = phaseRepository.findAll();
        return result;
    }
    
	@Override
	public List<Phase> findByProject(Project project) {
        List<Phase> result = phaseRepository.findByProject(project);
        return result;
	}

    /**
     *  Get one phase by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Phase findOne(Long id) {
        log.debug("Request to get Phase : {}", id);
        Phase phase = phaseRepository.findOne(id);
        return phase;
    }

    /**
     *  Delete the  phase by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {

    	for(Dashboard dash : dashboardService.findByPhase(id)){
    		dashboardService.delete(dash.getId());
    	}
    	
    	for(MeasureView view : viewService.findByPhaseOverview(id)){
    		viewService.delete(view.getId());
    	}
    	
    	for(MeasureView view : viewService.findByPhase(id)){
    		viewService.delete(view.getId());
    	}
    	
        phaseRepository.delete(id);
    }


}
