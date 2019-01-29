(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectGeneralConfigurationController', ProjectGeneralConfigurationController);

	ProjectGeneralConfigurationController.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity','MeasureAgentService', 'Notification','ProjectAnalysis','AnalysisServicesService','UsersRightAccessService' ];

	function ProjectGeneralConfigurationController($location, $scope, Principal,
			LoginService, $state, entity,	MeasureAgentService, Notification,ProjectAnalysis,AnalysisServicesService,UsersRightAccessService) {
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
		}
		
	}
})();
