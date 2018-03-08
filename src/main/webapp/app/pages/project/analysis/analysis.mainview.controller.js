(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'AnalysisMainView', AnalysisMainView);

	AnalysisMainView.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity','project', 'Home', 'Notification','ProjectAnalysis','AnalysisServicesService' ];

	function AnalysisMainView($location, $scope, Principal,
			LoginService, $state, entity,project, Home, Notification,ProjectAnalysis,AnalysisServicesService) {
		var vm = this;
		vm.currentanalyse = entity;
		vm.project = project;
		
		vm.frame = frame;
		function frame() {
			return "<iframe src=\""+vm.currentanalyse.viewUrl+"\" style=\"border:none;margin-right:10px;\" width=\"100%\" height=\"600\"></iframe>";
		}
		 

		vm.notifications = [];
		loadNotificationByProject(vm.project.id);

		function loadNotificationByProject(id) {
			Notification.notifications({
				id : id
			}, function(result) {
				vm.notifications = result;
			});
		}
		
		vm.isserviceavailable = false;
		vm.services = [];
		loadAllAnalysisServices();		
		function loadAllAnalysisServices() {
			AnalysisServicesService.allServices(function(result) {
				vm.services = result;			
				for(var i = 0; i < vm.services.length;i++){
					if(vm.services[i].name == vm.currentanalyse.analysisToolId){
						vm.isserviceavailable = true;
					}		
				}
			});
		}
	
		function loadAllAnalyseServices() {
			AnalysisServicesService.allServices(function(result) {
				vm.services = result;
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
