package org.measure.platform.service.application.impl;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.IApplicationCatalogueService;
import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.core.api.IMeasureVisaulisationManagement;
import org.measure.platform.core.api.entitys.ApplicationService;
import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.entity.Application;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.service.application.api.IApplicationScheduling;
import org.measure.platform.service.smmengine.api.ISchedulingService;
import org.measure.smm.application.model.SMMApplication;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.measure.model.View;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSchedulingService implements IApplicationScheduling{
	@Inject
	private MeasureInstanceService measureInstanceService;

	@Inject
	private IMeasureCatalogueService measureCatalogue;
	
	@Inject
	private DashboardService dashboardService;
	
	
	@Inject
	private MeasureViewService measureViewService;
	
	@Inject
	private ISchedulingService schedulingService;
	
	@Inject
	private ApplicationService applicationInstanceService;
	
	@Inject
	private IApplicationCatalogueService applicationCatalogue;
	

	
	@Inject
	private IMeasureVisaulisationManagement measureVisualisationService;
	
	@Override
	public Boolean startApplication(Long id) {
		
		Application applicationInstance = applicationInstanceService.findOne(id);
		applicationInstance = executeApplication(applicationInstance);

		SMMApplication applicationResource = applicationCatalogue.getApplication(applicationInstance.getApplicationType());
		
		for (org.measure.smm.application.model.Dashboard dashboardConf : applicationResource.getDashboards().getDashboard()) {
			createDashboard(applicationInstance,dashboardConf);
		}

		return true;
	}

	private void createDashboard(Application applicationInstance, org.measure.smm.application.model.Dashboard dashboardConf) {
		Dashboard dashboard = new Dashboard();
		dashboard.setApplication(applicationInstance);
		dashboard.setDashboardName(dashboardConf.getLabel());
		dashboard.setEditable(false);
		dashboard.setProject(applicationInstance.getProject());
		dashboard.setAuto(true);
		dashboard.setMode("APPLICATION");
		
		dashboard = this.dashboardService.save(dashboard);
		
		for (org.measure.smm.application.model.View viewConf : dashboardConf.getView()) {
			createMeasureView(viewConf,applicationInstance,dashboard);
		}
	}
	

	private MeasureView createMeasureView(org.measure.smm.application.model.View appViewConf,Application applicationInstance,Dashboard dashboard) {
		
		MeasureInstance instanceOfView = measureInstanceService.findMeasureInstancesByApplicationInstance(applicationInstance.getId(),appViewConf.getMeasure());

		View viewConf = null;
		SMMMeasure measureConf = this.measureCatalogue.getMeasure(applicationInstance.getApplicationType(), appViewConf.getMeasure());
		for(View measureViewConf : measureConf.getViews().getView()) {
			if(measureViewConf.getName().equals(appViewConf.getView())) {
				viewConf = measureViewConf;
				break;
			}	
		}
		
		if(instanceOfView != null && viewConf != null ) {
			MeasureView view = 	measureVisualisationService.createMeasureView(viewConf, dashboard, instanceOfView);
			measureViewService.save(view);
			return view;
		}
		
		return null;
	}



	private Application executeApplication(Application applicationInstance) {
		
		List<MeasureInstance> measuresInstances = measureInstanceService.findMeasureInstancesByApplicationInstance(applicationInstance.getId());
		for (MeasureInstance measureInstance : measuresInstances) {
			measureInstance.setIsShedule(true);
			measureInstanceService.save(measureInstance);	
			schedulingService.scheduleMeasure(measureInstance);
		}
		
		applicationInstance.setEnable(true);
		return applicationInstanceService.save(applicationInstance);	
	}

	@Override
	public Boolean stopApplication(Long id) {

		// update application and measure instances data model
		org.measure.platform.core.entity.Application applicationInstance = applicationInstanceService.findOne(id);
		if (applicationInstance == null || !applicationInstance.isEnable())
			return null;

		applicationInstance.setEnable(false);

		applicationInstance = applicationInstanceService.save(applicationInstance);

		List<MeasureInstance> measuresInstances = measureInstanceService.findMeasureInstancesByApplicationInstance(applicationInstance.getId());
		for (MeasureInstance measureInstance : measuresInstances) {
			measureInstance.setIsShedule(false);
			measureInstanceService.save(measureInstance);
			this.schedulingService.removeMeasure(measureInstance.getId());
		}

		// delete views and dashboards
		List<org.measure.platform.core.entity.Dashboard> dashboards = dashboardService.findByApplication(id);
		for (org.measure.platform.core.entity.Dashboard dashboard : dashboards) {
			dashboardService.delete(dashboard.getId());
		}

		return false;
	}

}
