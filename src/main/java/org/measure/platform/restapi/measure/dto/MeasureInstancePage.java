package org.measure.platform.restapi.measure.dto;

import java.util.ArrayList;
import java.util.List;

import org.measure.platform.core.entity.MeasureInstance;
import org.measure.platform.core.entity.MeasureView;
import org.measure.smm.measure.api.IMeasurement;

public class MeasureInstancePage {
	private MeasureView view;
	private List<IMeasurement> measurements;
	private int page;
	private int pageSize;
	private int totalMeasurements;
	
	
	public MeasureInstancePage() {
		this.measurements = new ArrayList<IMeasurement>();
	}
	
	public List<IMeasurement> getMeasurements() {
		return measurements;
	}
	public void setMeasurements(List<IMeasurement> measurements) {
		this.measurements = measurements;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalMeasurements() {
		return totalMeasurements;
	}
	public void setTotalMeasurements(int totalMeasurements) {
		this.totalMeasurements = totalMeasurements;
	}

	public MeasureView getView() {
		return view;
	}

	public void setView(MeasureView view) {
		this.view = view;
	}
}
