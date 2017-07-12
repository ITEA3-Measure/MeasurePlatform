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

		
		// Phase Management
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
		
		
		
		// Notification
		
		vm.notifications = [];
		loadNotificationByProject(vm.project.id);

		function loadNotificationByProject(id) {
			Notification.notifications({
				id : id
			}, function(result) {
				vm.notifications = result;
			});
		}
		
		vm.deleteview = deleteview;
		function deleteview(id) {
			 MeasureView.delete({id: id});
			 $state.go('appproject', null, { reload: 'appproject' });
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
		
		
		// Tab Management
		vm.selectedTab ="1";
		vm.selectedTab = $cookies.get("selectedTab");
		vm.selectTab = selectTab;
	
		function selectTab(tab){
			$cookies.put("selectedTab",tab);
		}
		
		vm.isActive = isActive;
		function isActive(tab){
			if($cookies.get("selectedTab") == tab){
				return "active";
			}
			return "";
		}
		
	

	}
})();
