(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ApplicationInstancesController', ApplicationInstancesController);

	ApplicationInstancesController.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity', 'Home',
			'ApplicationInstances', 'MeasureAgentService', 'Notification' ,'ProjectAnalysis'];

	function ApplicationInstancesController($location, $scope, Principal,
			LoginService, $state, entity, Home, ApplicationInstances,
			MeasureAgentService, Notification,ProjectAnalysis) {
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
