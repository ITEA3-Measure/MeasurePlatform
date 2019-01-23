package org.measure.platform.core.entity.dto;

import org.measure.platform.core.entity.Dashboard;

public class MappingDashboardDTO {
	
	private Dashboard dashboard;
	
	public MappingDashboardDTO() {
		dashboard = new Dashboard();
	}
	
	public Dashboard convertDashboardDtoToDashboard(DashboardDTO dashboardDTO) {
		dashboard.setId(dashboardDTO.getId());
		dashboard.setDashboardName(dashboardDTO.getDashboardName());
		dashboard.setDashboardDescription(dashboardDTO.getDashboardDescription());
		dashboard.setMode(dashboardDTO.getMode());
		dashboard.setKibanaId(dashboardDTO.getKibanaId());
		dashboard.setContent(dashboardDTO.getContent());
		dashboard.setAuto(dashboardDTO.getAuto());
		dashboard.setEditable(dashboardDTO.getEditable());
		dashboard.setSize(dashboardDTO.getSize());
		dashboard.setTimePeriode(dashboardDTO.getTimePeriode());
		dashboard.setProject(dashboardDTO.getProject());
		dashboard.setApplication(dashboardDTO.getApplication());
		dashboard.setViews(dashboardDTO.getViews());
		dashboard.setUsers(dashboardDTO.getUsers());
		return dashboard;
	}

}
