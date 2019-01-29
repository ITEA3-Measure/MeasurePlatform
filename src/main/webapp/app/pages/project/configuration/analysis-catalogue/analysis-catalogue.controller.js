(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectAnalysisConfigurationController', ProjectAnalysisConfigurationController);

	ProjectAnalysisConfigurationController.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity','MeasureAgentService', 'Notification','ProjectAnalysis','AnalysisServicesService', 'UsersRightAccessService' ];

	function ProjectAnalysisConfigurationController($location, $scope, Principal,
			LoginService, $state, entity,	MeasureAgentService, Notification,ProjectAnalysis,AnalysisServicesService, UsersRightAccessService) {
		var vm = this;
		vm.project = entity;
		vm.hasManagerRole;
		vm.notifications = [];
		loadNotificationByProject(vm.project.id);

		function loadNotificationByProject(id) {
			Notification.notifications({
				id : id
			}, function(result) {
				vm.notifications = result;
			});
		}
		
		vm.services = [];
		vm.analysis = [];
		vm.desabledAnalyse = [];
		loadAllAnalysisServices();		
		function loadAllAnalysisServices() {
			AnalysisServicesService.allServices(function(result) {
				vm.services = result;			
				ProjectAnalysis.byprojects({
					id : vm.project.id
				}, function(result) {
					vm.analysis = result;
					for(var i = 0 ; i < vm.services.length;i++){
						vm.services[i].status = false;
						for(var j = 0 ; j < result.length;j++){
							if(vm.services[i].name == result[j].analysisToolId){
								vm.services[i].status = true;
								vm.services[i].instance = result[j];
							}
						}
					}	
				});
			});
		}
				
		vm.startAnalyse = startAnalyse;
		function startAnalyse(analysisTool) {
			var analysis = new Object();
			analysis.analysisToolId = analysisTool.name;
			analysis.project = vm.project;
			analysis.analysisToolDescription = analysisTool.description;		
			ProjectAnalysis.save(analysis, onSaveSuccess, onSaveError);
		}
		

		function onSaveSuccess(result) {
			loadAllAnalysisServices();	
		}

		function onSaveError() {
			
		}
		
		vm.stopAnalyse = stopAnalyse;
		function stopAnalyse(analysisTool) {
			ProjectAnalysis.delete({id: analysisTool.instance.id},
	                function () {
				loadAllAnalysisServices();	
	            });
		}

	}
})();
