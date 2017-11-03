(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('PhaseOverviewController',
			PhaseOverviewController);

	PhaseOverviewController.$inject = [ '$scope','$cookies', 'Principal', 'LoginService',
			'$state', 'entity', 'Project', 'Phase','Notification', 'MeasureView' ];

	function PhaseOverviewController($scope,$cookies, Principal, LoginService, $state,
			entity, Project, Phase,Notification, MeasureView) {
		var vm = this;
		vm.project = entity;
			
		// Overview Management
		MeasureView.byproject({
			id : vm.project.id
		}, function(result) {
			if (result.length > 0) {
				vm.project.measureview = result;
			}
		});

		vm.edit = false;

		vm.edition = edition;

		function edition(id) {
			if (id == null) {
				if (vm.edit) {
					vm.edit = false;
				} else {
					vm.edit = true;
				}
			} else {
				if (vm.editphases[id]) {
					vm.editphases[id] = false;
				} else {
					vm.editphases[id] = true;
				}
			}

		}

		
		vm.deleteview = deleteview;
		function deleteview(id) {
			 MeasureView.delete({id: id});
			 $state.go('phasesoverview', null, { reload: 'phasesoverview' });
		}
		
		
		vm.phases = [];
		vm.editphases = [];

		loadPhasesByProject(vm.project.id);
		
		function loadPhasesByProject(id) {
			Phase.phases({
				id : id
			}, function(result) {
				vm.phases = result;
				for (var i = 0; i < vm.phases.length; i++) {
					vm.editphases[vm.phases[i].id] = false;
				}
				
				for (var i = 0; i < vm.phases.length; i++) {
					MeasureView.byphaseoverview({
						id : vm.phases[i].id
					}, function(result) {
						if(result.length > 0){
							for (var i = 0; i < vm.phases.length; i++) {
								if(vm.phases[i].id == result[0].phaseoverview.id){
									vm.phases[i].measureview = result;
								}
							}
						}
					});
				}
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
