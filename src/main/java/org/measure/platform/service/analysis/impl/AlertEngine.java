package org.measure.platform.service.analysis.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.measure.platform.service.analysis.api.IAlertEngineService;
import org.measure.platform.service.analysis.data.alert.AlertData;
import org.measure.platform.service.analysis.data.alert.AlertReport;
import org.measure.platform.service.analysis.data.alert.AlertSubscription;
import org.measure.platform.service.analysis.data.alert.AlertType;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(value = "singleton")
public class AlertEngine implements IAlertEngineService {

	private Map<String, AlertReport> alertMap = new HashMap<>();
	private Map<String, List<String>> alertSuscriptors = new HashMap<>();

	@Override
	public void subscribe(AlertSubscription suscribtion) {
		// Create ALert Report
		
		String analysisTool = suscribtion.getAnalysisTool();
		AlertReport report =alertMap.get(analysisTool);
		if(report == null){
			report = new AlertReport();
			report.setFrom(new Date());
			alertMap.put(analysisTool, report);
		}
		
		 String alertKey = calculateAlertKey(suscribtion);
		 List<String> analysisToolList = alertSuscriptors.get(alertKey);
		 if(analysisToolList == null){
			 analysisToolList = new ArrayList<>();
			 alertSuscriptors.put(alertKey,analysisToolList);
			 
		 }
		 if(!analysisToolList.contains(analysisTool)){
			 analysisToolList.add(analysisTool);	 
		 }
	}
	
	
	
	@Override
	public void alert(AlertData alert) {
		if(alert.getAlertTool() != null){
			AlertReport toolReport = alertMap.get(alert.getAlertTool());
			if(toolReport != null){
				toolReport.getAlerts().add(alert);
			}
		}else{
			String alertKey = calculateAlertKey(alert);
			List<String> tools = alertSuscriptors.get(alertKey);
			if(tools != null){
				for (String analysisTool : 	tools) {
					AlertReport toolReport = alertMap.get(analysisTool);
					toolReport.getAlerts().add(alert);
				}
			}
		}
		
	}

	private String calculateAlertKey(AlertData alert) {
		AlertType type = AlertType.fromString(alert.getAlertType());
		String key =  type.name();
		for(String idProperties :  type.getRequestProperties()){
			key = key + "[" + idProperties+":" + alert.getPropertieValue(idProperties) + "]";
		}
		return key;
	}

	private String calculateAlertKey(AlertSubscription suscribtion) {
		String key = suscribtion.getEventType().name();
		for(String idProperties : suscribtion.getEventType().getRequestProperties()){
			key = key + "[" + idProperties+":" + suscribtion.getPropertieValue(idProperties) + "]";
		}
		return key;
	}

	@Override
	public AlertReport getAlerts(String analysisTool) {
		AlertReport currentReport =  alertMap.get(analysisTool);
		AlertReport newReport = new AlertReport();
		newReport.setFrom(new Date());
		this.alertMap.put(analysisTool, newReport);
		return currentReport;	
	}

	@Override
	public void unsubscribe(AlertSubscription suscribtion) {
		String alertKey = calculateAlertKey(suscribtion);
		List<String> analysisToolList = alertSuscriptors.get(alertKey);
		if(analysisToolList != null){
			analysisToolList.remove(suscribtion.getAnalysisTool());
		}	
	}	
}
