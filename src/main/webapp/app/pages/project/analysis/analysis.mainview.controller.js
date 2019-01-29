(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'AnalysisMainView', AnalysisMainView);

	AnalysisMainView.$inject = [ '$location', '$scope','$cookies',
			'Principal', 'LoginService', '$state','project','role', 'Notification','ProjectAnalysis','AnalysisServicesService','UsersRightAccessService'];

	function AnalysisMainView($location, $scope,$cookies, Principal,
			LoginService, $state,project,role, Notification,ProjectAnalysis,AnalysisServicesService,UsersRightAccessService) {
		var vm = this;
		vm.currentanalyse = null;
		vm.project = project;		
		vm.hasServices = false;
		vm.hasManagerRole = role.data;
		vm.selectedTab = 0;
		
		vm.analysis = [];	
		loadAnalysisByProject(vm.project.id);
		function loadAnalysisByProject(id) {
			ProjectAnalysis.byprojects({
				id : id
			}, function(result) {
				vm.analysis = result;
				if(vm.analysis.length > 0){
					vm.hasServices = true;	
					
					let savedSelection = $cookies.get("selectedAnalysis");
					
					if(savedSelection != null){
						vm.selectedTab = savedSelection;
					}
				}

				AnalysisServicesService.allServices(function(result) {
					for(var i = 0 ; i< vm.analysis.length;i++){
						for(var j = 0;j < result.length;j++){					
							if(result[j].name == vm.analysis[i].analysisToolId){
								vm.analysis[i].isserviceavailable = true;
								vm.analysis[i].frame = "<iframe src=\""+vm.analysis[i].viewUrl+"\" style=\"border:none;margin-right:10px;\" width=\"100%\" height=\"600\"></iframe>";
							}		
						}
						if(vm.analysis[i].isserviceavailable == null){
							vm.analysis[i].isserviceavailable = false;
						}
					}
				});		
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
		
		// Tab Management
		vm.isActive = isActive;
		function isActive(idx) {
			if (idx == vm.selectedTab) {
				return 'active';
			}
			return '';
		}

		vm.setActive = setActive;		
		function setActive(idx) {
			vm.selectedTab = idx;
			$cookies.put("selectedAnalysis",idx);
		}


	}
})();
