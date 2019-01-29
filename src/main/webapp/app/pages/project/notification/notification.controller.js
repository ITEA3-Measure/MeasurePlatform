(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('NotificationController',
			NotificationController);

	NotificationController.$inject = [ '$scope','$cookies', 'Principal', 'LoginService',
			'$state', 'entity','role','Notification', 'MeasureView','ProjectAnalysis', 'AnalysisServicesService', 'UsersRightAccessService'];

	function NotificationController($scope,$cookies, Principal, LoginService, $state,
			entity,role,Notification, MeasureView,ProjectAnalysis, AnalysisServicesService, UsersRightAccessService) {
		var vm = this;
		vm.project = entity;
		vm.hasManagerRole = role.data;
			
		vm.notifications = [];
		loadNotificationByProject(vm.project.id);

		function loadNotificationByProject(id) {
			Notification.notifications({
				id : id
			}, function(result) {
				vm.notifications = result;
			});
		}	
		
		vm.validateNotification = validateNotification;
		function validateNotification(id){
			
			var newList = [];
			var j = 0;
			for (var i = 0; i < vm.notifications.length; i++) {
				if(vm.notifications[i].id == id){
					vm.notifications[i].isValidated = true;
					Notification.update(vm.notifications[i]);
				}else{
					newList[j] = vm.notifications[i];
					j = j + 1;
				}	
			}
			vm.notifications = newList;
		}
		

		vm.showErrorNotification = true;
		vm.showWarningNotification = true;
		vm.showInfoNotification = true;
		vm.showSuccessNotification = true;
		
		vm.filterNotification = filterNotification;
	
		function filterNotification(){
			
			Notification.notifications({
				id : vm.project.id
			}, function(result) {
				var allNotif = result;
				
				var newList = [];
				var filtred = 0;
			
				for (var i = 0; i <allNotif.length; i++) {
					if(vm.showErrorNotification && allNotif[i].notificationType== 'ERROR'){
						newList[filtred] = allNotif[i];
						filtred = filtred + 1;
					}else if(vm.showInfoNotification && allNotif[i].notificationType== 'INFO'){
						newList[filtred] = allNotif[i];
						filtred = filtred + 1;
					}else if(vm.showWarningNotification && allNotif[i].notificationType== 'WARNING'){
						newList[filtred] = allNotif[i];
						filtred = filtred + 1;
					}else if(vm.showErrorNotification && allNotif[i].notificationType== 'ERROR'){
						newList[filtred] = allNotif[i];
						filtred = filtred + 1;
					}
				}
				
				vm.notifications = newList;
			});		
		}
	}
})();
