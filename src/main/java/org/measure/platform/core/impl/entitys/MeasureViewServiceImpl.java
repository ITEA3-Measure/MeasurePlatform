package org.measure.platform.core.impl.entitys;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.api.entitys.PhaseService;
import org.measure.platform.core.api.entitys.ProjectService;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Phase;
import org.measure.platform.core.entity.Project;
import org.measure.platform.core.impl.repository.MeasureViewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MeasureView.
 */
@Service
@Transactional
public class MeasureViewServiceImpl implements MeasureViewService{

    private final Logger log = LoggerFactory.getLogger(MeasureViewServiceImpl.class);
    
    @Inject
    private MeasureViewRepository measureViewRepository;
    
    @Inject
    private ProjectService projectService;
    
    @Inject
    private PhaseService phaseService;
    
    @Inject
    private DashboardService dashboardService;
    
    

    @Autowired
    private MessageSource messageSource;
    
	@Value("${measure.kibana.adress}")
	private String kibanaAdress;

    /**
     * Save a measureView.
     *
     * @param measureView the entity to save
     * @return the persisted entity
     */
    public MeasureView save(MeasureView measureView) {
        log.debug("Request to save MeasureView : {}", measureView);
        
        if(!measureView.isCustom()){   	
        	updateViewData(measureView);
        }
             
        MeasureView result = measureViewRepository.save(measureView);
        return result;
    }

	private void updateViewData(MeasureView measureView) {
	
		String type = "line";
		
		if(measureView.getType().equals("Last Value")){

			String refresh = measureView.isAuto() ? "t":"f";
			String periode = "from:now-1y,mode:quick,to:now";
			String measure = measureView.getMeasureinstance().getInstanceName().replaceAll(" ", "+");
			
			String width = "300";
			String height = "200";
			String font = "50";
			if(measureView.getSize().equals("Small")){
				font = "20";
			} else if(measureView.getSize().equals("Medium")){
				font = "50";
			} else if(measureView.getSize().equals("Large")){
				font = "80";
			} else if(measureView.getSize().equals("Very Large")){
				font = "120";
			}
			
			String value =   messageSource.getMessage("viewtype.view2", new Object[]{"metric",refresh,periode,measure,font,height,width,kibanaAdress}, Locale.ENGLISH);    	
			measureView.setViewData(value);		
		}else{
			if(measureView.getType().equals("Line chart")){
				type = "line";
			}else if(measureView.getType().equals("Area chart")){
				type = "area";
			}else if(measureView.getType().equals("Bar chart")){
				type = "histogram";
			}
			
			String refresh = measureView.isAuto() ? "t":"f";
			
			String periode = "from:now-24h,mode:quick,to:now";
			String interval = "30s";
			if(measureView.getInterval().equals("Last 15 minutes")){
				periode = "from:now-15m,mode:quick,to:now";
				interval = "5s";
			}else if(measureView.getInterval().equals("Last Hour")){
				periode = "from:now-1h,mode:quick,to:now";
				interval = "30s";
			} else if(measureView.getInterval().equals("Last Day")){
				periode = "from:now-24h,mode:quick,to:now";
				interval="10m";
			} else if(measureView.getInterval().equals("Last Week")){
				periode = "rom:now-7d,mode:quick,to:now";
				interval = "1h";
			} else if(measureView.getInterval().equals("Last Month")){
				periode = "from:now-30d,mode:quick,to:now";
				interval = "3h";
			} else if(measureView.getInterval().equals("Last Year")){
				periode = "from:now-1y,mode:quick,to:now";
				interval = "1d";
			} 
			String measure = measureView.getMeasureinstance().getInstanceName().replaceAll(" ", "+");
			
			String color = "%235195CE";
			
			String width = "800";
			String height = "400";
			if(measureView.getSize().equals("Small")){
				width = "300";
				height = "200";
			} else if(measureView.getSize().equals("Medium")){
				width = "400";
				height = "300";
			} else if(measureView.getSize().equals("Large")){
				width = "600";
				height = "400";
			} else if(measureView.getSize().equals("Very Large")){
				width = "800";
				height = "600";
			}
			
			String value =   messageSource.getMessage("viewtype.view1", new Object[]{type,refresh,periode,measure,color,interval,height,width,kibanaAdress}, Locale.ENGLISH);    	
			measureView.setViewData(value);		
		}
		
		

		

	}

    /**
     *  Get all the measureViews.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<MeasureView> findAll() {
        log.debug("Request to get all MeasureViews");
        List<MeasureView> result = measureViewRepository.findAll();

        return result;
    }

    /**
     *  Get one measureView by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MeasureView findOne(Long id) {
        log.debug("Request to get MeasureView : {}", id);
        MeasureView measureView = measureViewRepository.findOne(id);
        return measureView;
    }

    /**
     *  Delete the  measureView by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MeasureView : {}", id);
        measureViewRepository.delete(id);
    }
    
    public List<MeasureView> findByProject(Long id){
		Project project = projectService.findOne(id);
		return measureViewRepository.findByProject(project);
	}
	
    public List<MeasureView> findByProjectOverview(Long id){
		Project project = projectService.findOne(id);
		return measureViewRepository.findByProjectOverview(project);
	}

    public List<MeasureView> findByPhase(Long id){
		Phase phase = phaseService.findOne(id);
		return measureViewRepository.findByPhase(phase);
	}
	
    public List<MeasureView> findByPhaseOverview(Long id){
		Phase phase = phaseService.findOne(id);
		return measureViewRepository.findByPhaseOverview(phase);
	}
	
    public List<MeasureView> findByDashboard(Long id){
		Dashboard dashboard = dashboardService.findOne(id);
		return measureViewRepository.findByDashboard(dashboard);
	}
}
