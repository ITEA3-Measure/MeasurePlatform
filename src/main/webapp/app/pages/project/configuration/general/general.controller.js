(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectGeneralConfigurationController', ProjectGeneralConfigurationController);

	ProjectGeneralConfigurationController.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity', 'Home','MeasureAgentService', 'Notification','ProjectAnalysis','AnalysisServicesService' ];

	function ProjectGeneralConfigurationController($location, $scope, Principal,
			LoginService, $state, entity, Home,	MeasureAgentService, Notification,ProjectAnalysis,AnalysisServicesService) {
		var vm = this;
		vm.project = entity;
		
		vm.notifications = [];
		loadNotificationByProject(vm.project.id);

		function loadNotificationByProject(id) {
			Notification.notifications({
				id : id
			}, function(result) {
				vm.notifications = result;
			});
		}
		
		vm.analysis = [];
		loadAnalysisByProject(vm.project.id);
		function loadAnalysisByProject(id) {
			ProjectAnalysis.byprojects({
				id : id
			}, function(result) {
				vm.analysis = result;
			});
			
			UsersRightAccessService.currentUserHasManagerRole({
				projectId : id
			}, function(result) {
				vm.hasManagerRole = result.data;
			});
		}
		
		vm.services = [];
		loadAllAnalysisServices();		
		function loadAllAnalysisServices() {
			AnalysisServicesService.allServices(function(result) {
				vm.services = result;
			});
		}
		
		vm.isserviceavailable = isserviceavailable;
		function isserviceavailable(id) {
			for(var i = 0; i < vm.services.length;i++){
				if(vm.services[i].name == id){
					return true;
				}		
			}
			return false;
		}
	}
})();
