package org.measure.platform.restapi.measure.dto;

import org.measure.platform.core.entity.MeasureView;

public class MeasurementViewDataQuery {
	private MeasureView  view;
	private MeasurementQuery query;
	public MeasureView getView() {
		return view;
	}
	public void setView(MeasureView view) {
		this.view = view;
	}
	public MeasurementQuery getQuery() {
		return query;
	}
	public void setQuery(MeasurementQuery query) {
		this.query = query;
	}
}
