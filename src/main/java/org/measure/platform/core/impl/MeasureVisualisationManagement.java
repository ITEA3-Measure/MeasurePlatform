package org.measure.platform.core.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.measure.platform.core.api.IMeasureVisaulisationManagement;
import org.measure.platform.core.api.entitys.DashboardService;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.entity.AnalysisCard;
import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.platform.core.entity.Project;
import org.measure.platform.service.measurement.api.IElasticsearchIndexManager;
import org.measure.platform.service.measurement.impl.IndexFormat;
import org.measure.smm.measure.model.DataSource;
import org.measure.smm.measure.model.Layout;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.measure.model.View;
import org.measure.smm.measure.model.ViewTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MeasureVisualisationManagement implements IMeasureVisaulisationManagement {
	
	
    @Autowired
    private MessageSource messageSource;

    @Value("${measure.kibana.adress}")
    private String kibanaAdress;
            
    @Inject 
    private MeasureInstanceService measureInstanceService; 
    
    @Inject 
    private DashboardService dashboardService; 
    
 
    @Override
	public String formatViewDataAsKibanaURL(MeasureView measureView) {
        String type = "line";
        
        
        
        if (measureView.getType().equals(ViewTypeEnum.DATA.toString())) {
        
            String refresh = measureView.isAuto() ? "f" : "t";
            String periode = measureView.getTimePeriode();
            String measure = measureView.getMeasureinstance().getInstanceName().replaceAll(" ", "+");  
            String visualisedProperty = measureView.getVisualisedProperty();
            String color = measureView.getColor();
            String font = measureView.getFontSize();    
            String width = measureView.getWidth();
            String height = measureView.getHeight();
            
            String label = measureView.getDescription();
            if(label == null || "".equals(label)) {
            	label = visualisedProperty;
            }
   
            return messageSource.getMessage("viewtype.view2", new Object[] { refresh, periode,measure, font, height, width, kibanaAdress, visualisedProperty, color, IndexFormat.getMeasureInstanceIndex(measureView.getMeasureinstance().getInstanceName()),label }, Locale.ENGLISH);
        } else {
            if (measureView.getType().equals(ViewTypeEnum.LIGNE.toString())) {
                type = "line";
            } else if (measureView.getType().equals(ViewTypeEnum.AREA.toString())) {
                type = "area";
            } else if (measureView.getType().equals(ViewTypeEnum.BAR.toString())) {
                type = "histogram";
            }
        
            String refresh = measureView.isAuto() ? "f" : "t";        
            String periode = measureView.getTimePeriode();
            String interval = measureView.getTimeAgregation();    
            String measure = measureView.getMeasureinstance().getInstanceName().replaceAll(" ", "+");   
            String color = measureView.getColor();
            String width = measureView.getWidth();
            String height = measureView.getHeight();
            
            if(Integer.valueOf(height) < 440) {
            	height = "440";
            }
            
            String visualisedProperty = measureView.getVisualisedProperty();
            String dateIndex = measureView.getDateIndex();
            String label = measureView.getDescription();
            if(label == null || "".equals(label)) {
            	label = visualisedProperty +" / " + interval;
            }
        
            return messageSource.getMessage("viewtype.view1", new Object[] { type, refresh, periode, measure,color, interval, height, width, kibanaAdress, visualisedProperty, dateIndex,color, IndexFormat.getMeasureInstanceIndex(measureView.getMeasureinstance().getInstanceName()),label}, Locale.ENGLISH);
        }
    }

    @Override
    public String formatViewDataAsKibanaViewReference(MeasureView measureView) {
        String width = measureView.getWidth();
        String height = measureView.getHeight();        
        String periode = measureView.getTimePeriode();
        String refresh = measureView.isAuto() ? "f" : "t";
            
        return messageSource.getMessage("viewtype.view3",new Object[] { height, width, kibanaAdress, measureView.getKibanaName(),refresh ,periode}, Locale.ENGLISH);
    }
  
    @Override
    public String formatViewDataAsKibanaDashboardReference(MeasureView measureView) {
        String height = measureView.getHeight();               
        String periode = measureView.getTimePeriode();
        String refresh = measureView.isAuto() ? "f" : "t";       
        return messageSource.getMessage("viewtype.view4",new Object[] { height, kibanaAdress, measureView.getKibanaName(),refresh,periode }, Locale.ENGLISH);
    }
    

    @Override
    public String formatViewDataAsAnalysisCard(MeasureView measureView) {
       AnalysisCard card = measureView.getAnalysiscard();
       return messageSource.getMessage("viewtype.view5", new Object[] {card.getCardUrl(),card.getPreferedHeight(), card.getPreferedWidth()}, Locale.ENGLISH);
	}
 
    @Override
    public List<MeasureView> createDefaultMeasureView(SMMMeasure measure,Long measureInstanceId) {
    	List<MeasureView> result = new ArrayList<>();
    	if(measure.getViews() != null) {
    		for(View mView : measure.getViews().getView())	{
    			if(mView.isDefault()) {
    		    	MeasureInstance instance = measureInstanceService.findOne(measureInstanceId);
    		    	Project project = instance.getProject();
    		    
    		    	
    		     	for(Dashboard ds :  dashboardService.findByProject(project.getId())) {
    		    		if(ds.getMode().equals("OVERVIEW")) {
    		    			result.add(createMeasureView(mView,ds,instance));
    		    		}
    		    	}
    			}
    		}
    	}
    	return result;
    }
    
    @Override
    public MeasureView createDefaultMeasureView(SMMMeasure measure,Long measureInstanceId,String viewName) {
    	if(measure.getViews() != null) {
    		for(View mView : measure.getViews().getView())	{
    			if(mView.getName().equals(viewName)) {
    		    	MeasureInstance instance = measureInstanceService.findOne(measureInstanceId);
    		    	Project project = instance.getProject();
    		    	for(Dashboard ds : dashboardService.findByProject(project.getId())) {
    		    		if(ds.getMode().equals("OVERVIEW")) {
    		   		    	return createMeasureView(mView,ds,instance);
    		    		}
    		    	}		    
    			}
    		}
    	}
    	return null;
    }
    
    private MeasureView createMeasureView(View mView,Dashboard dashboard,MeasureInstance measure) {
    	MeasureView measureView = new MeasureView();
    	
    	if(mView.getCustomData() != null && ! "".equals(mView.getCustomData())){
    	   	measureView.setMode("MANUAL");
         	measureView.setName(mView.getName() + " : " + measure.getInstanceName());
        	measureView.setDescription(mView.getDescription());
        	
        	byte[] decodedBytes = Base64.getDecoder().decode(mView.getCustomData());
        	String decodedString = new String(decodedBytes);  
        	decodedString = decodedString.replace("{ELASTICSEARCH_INDEX}",  IndexFormat.getMeasureInstanceIndex(measure.getInstanceName()));
        	measureView.setViewData(decodedString);
        	
         	if(mView.getDatasource() != null) {
         		DataSource dsView = mView.getDatasource() ;
         		measureView.setTimePeriode("from:now-"+dsView.getTimePeriode()+",mode:relative,to:now");
         	}
        	if(mView.getLayout() != null) {
        		Layout layout = mView.getLayout();
        		measureView.setWidth(layout.getWidth());
        		measureView.setHeight(layout.getHeight());
        		measureView.setFontSize(layout.getFontSize());
        	} 
           	measureView.setDashboard(dashboard);
        	measureView.setMeasureinstance(measure); 
    	}else {
        	measureView.setMode("AUTO");
        	measureView.setAuto(mView.isAutoRefresh());
        	measureView.setType(mView.getType().toString());
        	measureView.setName(mView.getName() + " : " + measure.getInstanceName());
        	measureView.setDescription(mView.getDescription());
        	measureView.setDefaultView(true);
        	        	
        	if(mView.getDatasource() != null) {
        		DataSource dsView = mView.getDatasource() ;
        		measureView.setViewData(dsView.getDataIndex());
        		measureView.setDateIndex(dsView.getDateIndex());
        		measureView.setTimePeriode("from:now-"+dsView.getTimePeriode()+",mode:relative,to:now");
        		measureView.setTimeAgregation(dsView.getTimeAggregation());
        	}
        	if(mView.getLayout() != null) {
        		Layout layout = mView.getLayout();
        		measureView.setWidth(layout.getWidth());
        		measureView.setHeight(layout.getHeight());
        		measureView.setFontSize(layout.getFontSize());
        	}  	
        	measureView.setDashboard(dashboard);
        	measureView.setMeasureinstance(measure); 
    	}
    	
 
    	
    	return measureView;
    }
}
