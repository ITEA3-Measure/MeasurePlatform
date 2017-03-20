(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('ProjectPhasesController',
			ProjectPhasesController);

	ProjectPhasesController.$inject = [ '$scope', 'Principal', 'LoginService',
			'$state', 'entity', 'Phase', 'Dashboard','MeasureView' ];

	function ProjectPhasesController($scope, Principal, LoginService, $state,
			entity, Phase, Dashboard,MeasureView) {
		var vm = this;
		vm.phase = entity;
		vm.dashboards = [];
		vm.selectedDashboard = 0;

		loadAllDashBoard(vm.phase.id);
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

		function setActive(idx) {
			vm.selectedDashboard = idx;
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
			 $state.go('appproject', null, { reload: 'appproject' });
		}
	}
	


	angular.module('measurePlatformApp').filter('to_trusted',
			[ '$sce', function($sce) {
				return function(text) {
					return $sce.trustAsHtml(text);
				};
			} ]);

})();
