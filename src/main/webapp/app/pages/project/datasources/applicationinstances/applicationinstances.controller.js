(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ApplicationInstancesController', ApplicationInstancesController);

	ApplicationInstancesController.$inject = [ '$location', '$scope',
			'Principal', '$state', 'entity',
			'ApplicationInstances', 'Notification' ];

	function ApplicationInstancesController($location, $scope, Principal,
			 $state, entity, ApplicationInstances, Notification) {
		var vm = this;

		vm.project = entity;

		vm.applicationInstances = [];
		loadInstances(vm.project.id);

		function loadInstances(id) {
			ApplicationInstances.instances({
				id : id
			}, function(result) {
				vm.applicationInstances = result;
			});
		}

		vm.startScheduling = startScheduling
		vm.stopScheduling = stopScheduling

		function startScheduling(index) {
			ApplicationInstances
					.startScheduling(
							{
								id : vm.applicationInstances[index].id
							},
							function(result) {
								vm.applicationInstances[index].enable = (result.data == 'true');
							});
		}

		function stopScheduling(index) {
			ApplicationInstances
					.stopScheduling(
							{
								id : vm.applicationInstances[index].id
							},
							function(result) {
								vm.applicationInstances[index].enable = (result.data == 'true');
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
