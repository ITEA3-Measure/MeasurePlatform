package org.measure.platform.core.catalogue.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.measure.platform.core.catalogue.api.IMeasureVisaulisationManagement;
import org.measure.platform.core.data.api.IDashboardService;
import org.measure.platform.core.data.api.IMeasureInstanceService;
import org.measure.platform.core.data.entity.AnalysisCard;
import org.measure.platform.core.data.entity.Dashboard;
import org.measure.platform.core.data.entity.MeasureInstance;
import org.measure.platform.core.data.entity.MeasureView;
import org.measure.platform.core.data.entity.Project;
import org.measure.platform.core.measurement.impl.IndexFormat;
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

    @Value("${measureplatform.kibana.api.endpoints}")
    private String kibanaAdress;
            
    @Inject 
    private IMeasureInstanceService measureInstanceService; 
    
    @Inject 
    private IDashboardService dashboardService; 
    
 
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
    		    			MeasureView view = createMeasureView(mView,ds,instance);
    		    			view.setDefaultView(true);
    		    			result.add(view);
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
    		    			MeasureView view = createMeasureView(mView,ds,instance);
    		    			view.setDefaultView(true);
    		    			return view;
    		    		}
    		    	}		    
    			}
    		}
    	}
    	return null;
    }
    
    @Override
    public MeasureView createMeasureView(View mView,Dashboard dashboard,MeasureInstance measure) {
    	MeasureView measureView = new MeasureView();
    	
    	if(mView.getCustomData() != null && ! "".equals(mView.getCustomData())){
    	   	measureView.setMode("MANUAL");
         	measureView.setName(mView.getName());
        	measureView.setDescription(mView.getDescription());
        	     	
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
        	
        	byte[] decodedBytes = Base64.getDecoder().decode(mView.getCustomData());
        	String decodedString = new String(decodedBytes);  
        	decodedString = decodedString.replace("{PLATFORM_INDEX}",  IndexFormat.getMeasureInstanceIndex(measure.getInstanceName()));
        	decodedString = decodedString.replace("{PLATFORM_URL}",  kibanaAdress);
        	decodedString = decodedString.replace("{PLATFORM_TIMEPERIODE}",  measureView.getTimePeriode());   	
        	decodedString = decodedString.replace("{PLATFORM_WIDTH}", measureView.getWidth());
        	decodedString = decodedString.replace("{PLATFORM_HEIGHT}", measureView.getHeight());

        	measureView.setViewData(decodedString);
        	
           	measureView.setDashboard(dashboard);
        	measureView.setMeasureinstance(measure); 
    	}else {
    		if(mView.getType() != null && mView.getType().equals(mView.getType().TABLE)) {
    			measureView.setMode(mView.getType().toString());
    		}else if(mView.getType() != null && mView.getType().equals(mView.getType().VALUE)) {
    			measureView.setMode(mView.getType().toString());
    		}else {
    			measureView.setMode("AUTO");	
    		}
        
        	measureView.setAuto(mView.isAutoRefresh());
        	measureView.setType(mView.getType().toString());
        	measureView.setName(mView.getName());
        	measureView.setDescription(mView.getDescription());
        	measureView.setDefaultView(true);
        	        	
        	if(mView.getDatasource() != null) {
        		DataSource dsView = mView.getDatasource() ;
        		measureView.setVisualisedProperty(dsView.getDataIndex());
        		measureView.setDateIndex(dsView.getDateIndex());
        		measureView.setTimePeriode("from:now-"+dsView.getTimePeriode()+",mode:relative,to:now");
        		measureView.setTimeAgregation(dsView.getTimeAggregation());
        	}
        	if(mView.getLayout() != null) {
        		Layout layout = mView.getLayout();
        		measureView.setWidth(layout.getWidth());
        		measureView.setHeight(layout.getHeight());
        		measureView.setFontSize(layout.getFontSize());
        		measureView.setColor(layout.getColor());
        	}  	
        	measureView.setDashboard(dashboard);
        	measureView.setMeasureinstance(measure); 
    	}
    	
 
    	
    	return measureView;
    }
}
