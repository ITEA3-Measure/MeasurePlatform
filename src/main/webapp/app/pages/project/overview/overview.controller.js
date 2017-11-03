(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('AppProjectController',
			AppProjectController);

	AppProjectController.$inject = [ '$scope','$cookies', 'Principal', 'LoginService',
			'$state', 'entity', 'Project', 'Phase','Notification', 'MeasureView' ];

	function AppProjectController($scope,$cookies, Principal, LoginService, $state,
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

		function edition() {
			if (vm.edit) {
				vm.edit = false;
			} else {
				vm.edit = true;
			}
		}
		
		vm.deleteview = deleteview;
		function deleteview(id) {
			 MeasureView.delete({id: id});
			 $state.go('projectoverview', null, { reload: 'projectoverview' });
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
