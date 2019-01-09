package org.measure.platform.core.api;

import org.measure.platform.core.entity.MeasureView;
import org.measure.smm.measure.model.SMMMeasure;

public interface IMeasureVisaulisationManagement {

	String formatViewDataAsAnalysisCard(MeasureView measureView);

	String formatViewDataAsKibanaDashboardReference(MeasureView measureView);

	String formatViewDataAsKibanaViewReference(MeasureView measureView);

	String formatViewDataAsKibanaURL(MeasureView measureView);

	MeasureView createDefaultMeasureView(SMMMeasure measure, Long measureInstanceId);

	MeasureView createDefaultMeasureView(SMMMeasure measure, Long measureInstanceId, String viewName);

}
