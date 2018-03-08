package org.measure.platform.service.analysis.data.analysis;

import java.util.Date;

/**
 * Analysis Service registre into Measure Platform
 *
 */
public class RegistredAnalysisService extends AnalysisService {

	private Date liveSign;
	public RegistredAnalysisService(AnalysisService service,Date liveSign) {
		this.setName(service.getName());
		this.setDescription(service.getDescription());
		this.setConfigurationURL(service.getConfigurationURL());
		this.setLiveSign(liveSign);
	}
	public Date getLiveSign() {
		return liveSign;
	}
	public void setLiveSign(Date liveSign) {
		this.liveSign = liveSign;
	}

}
