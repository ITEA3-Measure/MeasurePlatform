package org.measure.platform.service.analysis.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.measure.platform.core.data.api.IProjectService;
import org.measure.platform.core.data.entity.Project;
import org.measure.platform.service.analysis.api.IAlertSubscriptionManager;
import org.measure.platform.service.analysis.api.IAnalysisCatalogueService;
import org.measure.platform.service.analysis.data.alert.AlertSubscription;
import org.measure.platform.service.analysis.data.alert.AlertType;
import org.measure.platform.service.analysis.data.analysis.AnalysisService;
import org.measure.platform.service.analysis.data.analysis.RegistredAnalysisService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope(value = "singleton")
public class AnalysisCatalogue implements IAnalysisCatalogueService {
	
	private Map<String, RegistredAnalysisService> analysisToolMap = new HashMap<>();
	
	@Inject
	private IProjectService projectService;

	@Inject
	private IAlertSubscriptionManager suscribtionManager;
	
	
	@Override
	public void registerAnalysisService(AnalysisService service) {
		if(!this.analysisToolMap.containsKey(service.getName())){
			RegistredAnalysisService rs = new RegistredAnalysisService(service,new Date());
			this.analysisToolMap.put(service.getName(), rs);	
		}else{
			this.analysisToolMap.get(service.getName()).setLiveSign(new Date());
		}	
		
		for(Project project : projectService.findAll()){
			AlertSubscription suscribtion = new AlertSubscription();
			suscribtion.setAnalysisTool(service.getName());
			suscribtion.setProjectId(project.getId());
			suscribtion.setEventType(AlertType.ANALYSIS_ENABLE);
			suscribtionManager.subscribe(suscribtion);
			
			suscribtion = new AlertSubscription();
			suscribtion.setAnalysisTool(service.getName());
			suscribtion.setProjectId(project.getId());
			suscribtion.setEventType(AlertType.ANALYSIS_DESABLE);
			suscribtionManager.subscribe(suscribtion);
		}
	}
	
	@Override
	public void updateLiveSign(String analysisTool){
		if(this.analysisToolMap.containsKey(analysisTool)){
			this.analysisToolMap.get(analysisTool).setLiveSign(new Date());
		}
	}

	@Override
	public void unregisterAnalysisService(AnalysisService service) {
		this.analysisToolMap.remove(service.getName());	
		
		for(Project project : projectService.findAll()){
			AlertSubscription suscribtion = new AlertSubscription();
			suscribtion.setAnalysisTool(service.getName());
			suscribtion.setProjectId(project.getId());
			suscribtion.setEventType(AlertType.ANALYSIS_ENABLE);
			suscribtionManager.unsubscribe(suscribtion);
				
			suscribtion = new AlertSubscription();
			suscribtion.setAnalysisTool(service.getName());
			suscribtion.setProjectId(project.getId());
			suscribtion.setEventType(AlertType.ANALYSIS_DESABLE);
			suscribtionManager.unsubscribe(suscribtion);
		}	
	}

	@Override
	public AnalysisService getAnalysisServiceByName(String serviceName) {
		return this.analysisToolMap.get(serviceName);
	}

	@Override
	public List<RegistredAnalysisService> getAllAnalysisService() {
		return new ArrayList<RegistredAnalysisService>(this.analysisToolMap.values());
	}
}
