(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'UsersRightAccessController', UsersRightAccessController);

	UsersRightAccessController.$inject = [ '$location', '$scope', 'Principal', '$state', 'entity', 'Home', 'UsersRightAccessService' ];

	function UsersRightAccessController($location, $scope, Principal, $state, entity, Home, UsersRightAccessService) {
		var vm = this;
		vm.project = entity;
		vm.transformRole = transformRole;
		
		vm.users = [];
		
		loadUsersByProject(vm.project.id);
		function loadUsersByProject(projectId) {
			Principal.identity().then(function(account) {
	            $scope.currentUser = account.login;
	        });
			UsersRightAccessService.usersByProject({
				projectId : projectId
			}, function(result) {
				vm.users = result;
			});
		}
		
		function transformRole(projectId, userId) {
			UsersRightAccessService.transformRole({
				projectId: projectId,
				userId: userId
			}, function() {
				loadUsersByProject(projectId);
			});
		}
	}
})();
