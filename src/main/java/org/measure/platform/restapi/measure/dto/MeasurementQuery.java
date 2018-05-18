package org.measure.platform.restapi.measure.dto;

import java.io.Serializable;

public class MeasurementQuery implements Serializable {
	private static final long serialVersionUID = -5698280856709636347L;
	
	private String measureInstance;
	private int page;

	private int pageSyze;

	private String query;

	public String getMeasureInstance() {
		return measureInstance;
	}

	public void setMeasureInstance(String measureInstance) {
		this.measureInstance = measureInstance;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSyze() {
		return pageSyze;
	}

	public void setPageSyze(int pageSyze) {
		this.pageSyze = pageSyze;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
