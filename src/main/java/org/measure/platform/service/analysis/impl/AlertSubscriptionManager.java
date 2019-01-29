package org.measure.platform.service.analysis.impl;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.data.api.IAlertEventPropertyService;
import org.measure.platform.core.data.api.IAlertEventService;
import org.measure.platform.core.data.entity.AlertEvent;
import org.measure.platform.core.data.entity.AlertEventProperty;
import org.measure.platform.core.data.entity.Project;
import org.measure.platform.core.data.querys.ProjectRepository;
import org.measure.platform.service.analysis.api.IAlertEngineService;
import org.measure.platform.service.analysis.api.IAlertSubscriptionManager;
import org.measure.platform.service.analysis.data.alert.AlertProperty;
import org.measure.platform.service.analysis.data.alert.AlertSubscription;
import org.measure.platform.service.analysis.data.alert.AlertType;
import org.springframework.stereotype.Service;

@Service
public class AlertSubscriptionManager implements IAlertSubscriptionManager{

	@Inject
	private IAlertEngineService alertEngineService;
	
	@Inject
	private IAlertEventService alertEventService;
	
	@Inject
	private IAlertEventPropertyService alertEventPropertyService;
	

	@Inject
	private ProjectRepository projectRepository;
	
	@Override
	public void subscribe(AlertSubscription suscribtion) {	
		if(findAlertEvent(suscribtion) == null){
			AlertEvent event = new AlertEvent();
			Project project = projectRepository.getOne(suscribtion.getProjectId());
			event.setProject(project);
			event.setEventType(suscribtion.getEventType().name());
			event.setAnalysisTool(suscribtion.getAnalysisTool());
			event = alertEventService.save(event);
			
			for(AlertProperty prop : suscribtion.getProperties()){
				AlertEventProperty property = new AlertEventProperty();
				property.setName(prop.getProperty());
				property.setValue(prop.getValue());
				property.setAlertevent(event);
				alertEventPropertyService.save(property);
			}
					
			alertEngineService.subscribe(suscribtion);
		}		
	}
	
	@Override
	public void unsubscribe(AlertSubscription suscribtion) {
	    alertEngineService.unsubscribe(suscribtion);	
		AlertEvent event = findAlertEvent(suscribtion);
		if(event != null){
			alertEventService.delete(event.getId());
		}
	}
	
	@Override
	public void init(){
		for(AlertEvent alerts : alertEventService.findAll()){
			AlertSubscription suscribtion = new AlertSubscription();
			
			suscribtion.setAnalysisTool(alerts.getAnalysisTool());
			suscribtion.setEventType(AlertType.fromString(alerts.getEventType()));
			suscribtion.setProjectId(alerts.getProject().getId());
			
			for(AlertEventProperty prop  : alerts.getAlerteventpropertys()){
				suscribtion.getProperties().add(new AlertProperty(prop.getName(),prop.getValue()));
			}
			
		}
		
	}

	private AlertEvent findAlertEvent(AlertSubscription suscribtion){
		List<AlertEvent> result = null;
		Project project = projectRepository.getOne(suscribtion.getProjectId());
		if(suscribtion.getEventType().getResponsProperties().isEmpty()){
			result = alertEventService.findByProjectAndEventType(project,suscribtion.getAnalysisTool(),suscribtion.getEventType().name());
		}else if(!suscribtion.getProperties().isEmpty())  {
			result = alertEventService.findByProjectEventTypeAndProp(project, suscribtion.getAnalysisTool(),suscribtion.getEventType().name(), suscribtion.getProperties().get(0).getProperty(), suscribtion.getProperties().get(0).getValue());		
		}
		
		if(result != null && !result.isEmpty()){
			return result.get(0);
		}
		return null;
	}
	
	
	

}
