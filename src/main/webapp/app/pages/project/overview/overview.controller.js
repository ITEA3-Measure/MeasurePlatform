(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('AppProjectController',
			AppProjectController);

	AppProjectController.$inject = [ '$scope','$cookies', 'Principal', 'LoginService',
			'$state', 'entity', 'Project','Notification', 'MeasureView','Dashboard','ProjectAnalysis'];

	function AppProjectController($scope,$cookies, Principal, LoginService, $state,
			entity, Project,Notification, MeasureView,Dashboard,ProjectAnalysis) {
		var vm = this;
		vm.project = entity;
		
		
		vm.dashboards = [];
		vm.selectedDashboard = 1;

		loadAllDashBoard(vm.project.id);
		function loadAllDashBoard(id) {

			Dashboard.dashboards({
				id : id
			}, function(result) {
				vm.dashboards = result;
				
				for (var i = 0; i < vm.dashboards.length; i++) {
					MeasureView.bydashboard({
						id : vm.dashboards[i].id
					}, function(result) {
						if(result.length > 0){
							for (var i = 0; i < vm.dashboards.length; i++) {
								if(vm.dashboards[i].id == result[0].dashboard.id){
									vm.dashboards[i].measureview = result;
								}
							}
						}
					});
				}
				
			});
		}

		vm.isActive = isActive;

		function isActive(idx) {
			if (idx == vm.selectedDashboard) {
				return 'active';
			}
			return '';
		}

		vm.setActive = setActive;

		// Tab Management
		vm.selectedDashboard = $cookies.get("selectedDashboard");
		
		function setActive(idx) {
			vm.selectedDashboard = idx;
			$cookies.put("selectedDashboard",idx);
		}
		
		vm.editdashboard = false;
		vm.edition = edition;
		
		function edition(){
			if(vm.editdashboard){
				vm.editdashboard  = false;
			}else{
				vm.editdashboard  = true;
			}
		}
		
		vm.deletegraphic = deletegraphic;
		function deletegraphic(id) {
			 MeasureView.delete({id: id});
			 $state.go('projectoverview', null, { reload: 'projectoverview' });
		}
		
		vm.viewBlockStyle = viewBlockStyle;
		
		function viewBlockStyle(measureview){
			if(measureview.mode == 'AUTO' || measureview.mode == 'KVIS' || measureview.mode == 'CARD' ){
				return "display: inline-block;";
			}
			return "";			
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
	
	angular.module('measurePlatformApp').filter('to_trusted',
			[ '$sce', function($sce) {
				return function(text) {
					return $sce.trustAsHtml(text);
				};
			} ]);
	
})();
