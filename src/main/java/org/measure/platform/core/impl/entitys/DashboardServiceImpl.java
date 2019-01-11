package org.measure.platform.core.impl.entitys;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.impl.repository.DashboardRepository;
import org.measure.platform.core.impl.repository.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Dashboard.
 */
@Service
@Transactional
public class DashboardServiceImpl implements DashboardService {
    private final Logger log = LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

    @Value("${measure.kibana.adress}")
    private String kibanaAdress;

    @Inject
    private DashboardRepository dashboardRepository;

    @Inject
    private ProjectRepository projectRepository;

    @Inject
    private MeasureViewService viewService;

    /**
     * Save a dashboard.
     * @param dashboard the entity to save
     * @return the persisted entity
     */
    public Dashboard save(Dashboard dashboard) {
        if(dashboard.getMode().equals("KIBANA")){
             updateViewDataFromKibanaDashboard( dashboard);
        }
        
        Dashboard result = dashboardRepository.save(dashboard);
        return result;
    }

    private void updateViewDataFromKibanaDashboard(Dashboard dashboard) {
        String height = dashboard.getSize();
        if(height == null){
            height = "600";
        }
        
        String periode = dashboard.getTimePeriode();
        String refresh = dashboard.isAuto() ? "f" : "t";
        
        String value = messageSource.getMessage("viewtype.view4",new Object[] { height, kibanaAdress, dashboard.geKibanaId(),refresh,periode }, Locale.ENGLISH);
        dashboard.setContent(value);
    }

    /**
     * Get all the dashboards.
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Dashboard> findAll() {
        log.debug("Request to get all Dashboards");
        List<Dashboard> result = dashboardRepository.findAll();
        return result;
    }

    /**
     * Get one dashboard by id.
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Dashboard findOne(Long id) {
        log.debug("Request to get Dashboard : {}", id);
        Dashboard dashboard = dashboardRepository.findOne(id);
        return dashboard;
    }

    /**
     * Delete the  dashboard by id.
     * @param id the id of the entity
     */
    public void delete(Long id) {
        for(MeasureView view : viewService.findByDashboard(id)){
            viewService.delete(view.getId());
        }     
        dashboardRepository.delete(id);
    }

    @Override
    public List<Dashboard> findByProject(Long projectId) {
        return dashboardRepository.findByProject(projectRepository.getOne(projectId));
    }

}
