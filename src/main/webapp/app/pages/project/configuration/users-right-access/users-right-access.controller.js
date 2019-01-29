(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'UsersRightAccessController', UsersRightAccessController);

	UsersRightAccessController.$inject = [ '$location', '$scope', 'Principal', '$state', 'entity', 'ProjectAnalysis', 'Notification', 'UsersRightAccessService' ];

	function UsersRightAccessController($location, $scope, Principal, $state, entity, ProjectAnalysis, Notification, UsersRightAccessService) {
		var vm = this;
		vm.project = entity;
		vm.transformRole = transformRole;
		vm.hasManagerRole;
		vm.users = [];
		
		vm.notifications = [];
		loadNotificationByProject(vm.project.id);
		function loadNotificationByProject(id) {
			Notification.notifications({
				id : id
			}, function(result) {
				vm.notifications = result;
			});
		}
		
		loadUsersByProject(vm.project.id);
		function loadUsersByProject(projectId) {
			Principal.identity().then(function(account) {
	            $scope.currentUser = account.login;
	        });
			
			UsersRightAccessService.usersByProject({
				projectId : projectId
			}, function(result) {
				vm.users = result;
				vm.users.sort((a,b) => (a.id > b.id) ? 1 : ((b.id > a.id) ? -1 : 0));
			});
			
			UsersRightAccessService.currentUserHasManagerRole({
				projectId : projectId
			}, function(result) {
				vm.hasManagerRole = result.data;
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
		
		vm.analysis = [];
		loadAnalysisByProject(vm.project.id);
		function loadAnalysisByProject(id) {
			ProjectAnalysis.byprojects({
				id : id
			}, function(result) {
				vm.analysis = result;
			});
		}
	}
})();
