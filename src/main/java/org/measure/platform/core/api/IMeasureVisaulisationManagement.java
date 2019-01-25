package org.measure.platform.core.api;

import java.util.List;

import org.measure.platform.core.entity.Dashboard;
import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.smm.measure.model.SMMMeasure;
import org.measure.smm.measure.model.View;

public interface IMeasureVisaulisationManagement {

	String formatViewDataAsAnalysisCard(MeasureView measureView);

	String formatViewDataAsKibanaDashboardReference(MeasureView measureView);

	String formatViewDataAsKibanaViewReference(MeasureView measureView);

	String formatViewDataAsKibanaURL(MeasureView measureView);

	List<MeasureView> createDefaultMeasureView(SMMMeasure measure, Long measureInstanceId);

	MeasureView createDefaultMeasureView(SMMMeasure measure, Long measureInstanceId, String viewName);

	MeasureView createMeasureView(View mView, Dashboard dashboard, MeasureInstance measure);

}
