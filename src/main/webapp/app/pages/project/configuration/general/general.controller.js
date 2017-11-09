(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectGeneralConfigurationController', ProjectGeneralConfigurationController);

	ProjectGeneralConfigurationController.$inject = [ '$location', '$scope',
			'Principal', 'LoginService', '$state', 'entity', 'Home','MeasureAgentService', 'Notification' ];

	function ProjectGeneralConfigurationController($location, $scope, Principal,
			LoginService, $state, entity, Home,	MeasureAgentService, Notification) {
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
	}
})();
