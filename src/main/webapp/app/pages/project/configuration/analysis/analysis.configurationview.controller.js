(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectAnalysisConfiguration', ProjectAnalysisConfiguration);

	ProjectAnalysisConfiguration.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity','project', 'Notification','ProjectAnalysis','AnalysisServicesService' ];

	function ProjectAnalysisConfiguration($location, $scope, Principal,
			LoginService, $state, entity,project, Notification,ProjectAnalysis,AnalysisServicesService) {
		var vm = this;
		vm.currentanalyse = entity;
		vm.project = project;
		
		vm.frame = frame;
		function frame() {
			return "<iframe src=\""+vm.currentanalyse.configurationUrl+"\" style=\"border:none;margin-right:10px;\" width=\"100%\" height=\"600\"></iframe>";
		}
		 
		AnalysisServicesService.allServices(function(result) {
			for(var j = 0;j < result.length;j++){					
				if(result[j].name == vm.currentanalyse.analysisToolId){
					vm.currentanalyse.isserviceavailable = true;
					vm.currentanalyse.frame = "<iframe src=\""+vm.currentanalyse.configurationUrl+"\" style=\"border:none;margin-right:10px;\" width=\"100%\" height=\"600\"></iframe>";
				}		
			}	
			if(vm.currentanalyse.isserviceavailable == null){
				vm.currentanalyse.isserviceavailable = false;
			}
		});	

		
		vm.analysis = [];
		loadAnalysisByProject(vm.project.id);
		function loadAnalysisByProject(id) {
			ProjectAnalysis.byprojects({
				id : id
			}, function(result) {
				vm.analysis = result;
			});
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
	
	}
})();
