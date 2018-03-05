package org.measure.platform.service.analysis.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.measure.platform.service.analysis.api.IAnalysisCatalogueService;
import org.measure.platform.service.analysis.data.analysis.AnalysisService;
import org.measure.platform.service.analysis.data.analysis.RegistredAnalysisService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


@Service
@Scope(value = "singleton")
public class AnalysisCatalogue implements IAnalysisCatalogueService {
	
	private Map<String, RegistredAnalysisService> analysisToolMap = new HashMap<>();

	@Override
	public void registerAnalysisService(AnalysisService service) {
		if(!this.analysisToolMap.containsKey(service.getName())){
			RegistredAnalysisService rs = new RegistredAnalysisService(service,new Date());
			this.analysisToolMap.put(service.getName(), rs);	
		}else{
			this.analysisToolMap.get(service.getName()).setLiveSign(new Date());
		}		
	}

	@Override
	public void unregisterAnalysisService(AnalysisService service) {
		this.analysisToolMap.remove(service.getName());	
		
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
