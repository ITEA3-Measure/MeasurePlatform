package org.measure.platform.service.application.impl;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.catalogue.api.IApplicationCatalogueService;
import org.measure.platform.core.catalogue.api.IMeasureCatalogueService;
import org.measure.platform.core.catalogue.api.IMeasureVisaulisationManagement;
import org.measure.platform.core.data.api.IApplicationService;
import org.measure.platform.core.data.api.IDashboardService;
import org.measure.platform.core.data.api.IMeasureInstanceService;
import org.measure.platform.core.data.api.IMeasureViewService;
import org.measure.platform.core.data.entity.Application;
import org.measure.platform.core.data.entity.Dashboard;
import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.core.data.entity.MeasureView;
import org.measure.platform.service.application.api.IApplicationScheduling;
import org.measure.platform.service.smmengine.api.ISchedulingService;
import org.measure.smm.application.model.SMMApplication;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.measure.model.View;
import org.springframework.stereotype.Service;

@Service
public class ApplicationSchedulingService implements IApplicationScheduling{
	@Inject
	private IMeasureInstanceService measureInstanceService;

	@Inject
	private IMeasureCatalogueService measureCatalogue;
	
	@Inject
	private IDashboardService dashboardService;
	
	
	@Inject
	private IMeasureViewService measureViewService;
	
	@Inject
	private ISchedulingService schedulingService;
	
	@Inject
	private IApplicationService applicationInstanceService;
	
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
		for (MeasureInstance measureInstance : applicationInstance.getInstances()) {
			measureInstance.setIsShedule(true);
			schedulingService.scheduleMeasure(measureInstance);
		}
		
		applicationInstance.setEnable(true);
		return applicationInstanceService.save(applicationInstance);	
	}

	@Override
	public Boolean stopApplication(Long id) {

		// update application and measure instances data model
		org.measure.platform.core.data.entity.Application applicationInstance = applicationInstanceService.findOne(id);
		if (applicationInstance == null || !applicationInstance.isEnable())
			return null;

		applicationInstance.setEnable(false);
		for (MeasureInstance measureInstance :  applicationInstance.getInstances()) {
			measureInstance.setIsShedule(false);
			this.schedulingService.removeMeasure(measureInstance.getId());
		}
		
		applicationInstance = applicationInstanceService.save(applicationInstance);

		// delete views and dashboards
		List<org.measure.platform.core.data.entity.Dashboard> dashboards = dashboardService.findByApplication(id);
		for (org.measure.platform.core.data.entity.Dashboard dashboard : dashboards) {
			dashboardService.delete(dashboard.getId());
		}

		return false;
	}

}
