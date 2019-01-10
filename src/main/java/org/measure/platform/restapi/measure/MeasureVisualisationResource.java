package org.measure.platform.restapi.measure;

import java.util.List;

import javax.inject.Inject;

import org.measure.platform.core.api.IMeasureCatalogueService;
import org.measure.platform.core.api.IMeasureVisaulisationManagement;
import org.measure.platform.core.api.entitys.MeasureInstanceService;
import org.measure.platform.core.api.entitys.MeasureViewService;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.smm.measure.model.SMMMeasure;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/measure-visualisation")
public class MeasureVisualisationResource {

	@Value("${measure.kibana.adress}")
	private String kibanaAdress;

	@Inject
	private IMeasureVisaulisationManagement visualisationManagement;

	@Inject
	private MeasureInstanceService instanceService;

	@Inject
	private MeasureViewService measureViewService;

	@Inject
	private IMeasureCatalogueService measureCatalogueService;

	@RequestMapping(value = "/create-default", method = RequestMethod.GET)
	public MeasureView createDefaultVisualisation(@RequestParam("id") String id) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);
			MeasureInstance mInstance = instanceService.findOne(instanceId);
			
			List<MeasureView> defaultViews =  measureViewService.findDefaulsByMeasureInstance(mInstance.getId());
			
			if (defaultViews.size() == 0) {
				SMMMeasure measure = measureCatalogueService.getMeasure(mInstance.getMeasureName());
				MeasureView view = visualisationManagement.createDefaultMeasureView(measure, instanceId);

				measureViewService.save(view);
				return view;
			}
		}
		return null;
	}

	@RequestMapping(value = "/create-view", method = RequestMethod.GET)
	public MeasureView createVisualisation(@RequestParam("id") String id, @RequestParam("view") String viewName) {
		if (id.matches("\\d+")) {
			Long instanceId = Long.valueOf(id);
			MeasureInstance mInstance = instanceService.findOne(instanceId);
			List<MeasureView> defaultViews =  measureViewService.findDefaulsByMeasureInstance(mInstance.getId());

			if (defaultViews.size() == 0) {
				SMMMeasure measure = measureCatalogueService.getMeasure(mInstance.getMeasureName());
				MeasureView view = visualisationManagement.createDefaultMeasureView(measure, instanceId, viewName);
				measureViewService.save(view);
				return view;
			}
		}
		return null;
	}
}
