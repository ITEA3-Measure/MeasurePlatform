(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('ApplicationController',
			ApplicationController);

	ApplicationController.$inject = [ '$scope','$cookies', 'Principal', 'LoginService',
			'$state', 'Application' ];

	function ApplicationController($scope,$cookies, Principal, LoginService, $state, Application) {
		var vm = this;

		vm.applications = [];
		vm.category = [];
		vm.applicationsInCategory = [];

		
		
		// Tab Management
		vm.selectedCategory = $cookies.get("selectedCategory");	
		if(vm.selectedCategory == null){
			vm.selectedCategory = "0";
		}
		

		loadAll();
		

	

		function loadAll() {
			Application.allapplications(function(result) {
				vm.applications = result;
				for (var j = 0; j < vm.applications.length; j++) {
//					vm.applications[j].showUnites  = false;
//					vm.applications[j].showDependencies = false;
//					vm.applications[j].showProperties = false;
					
					
					if(vm.applications[j].category != null && vm.applications[j].category != ""){
						var find = false;
						for (var i = 0; i < vm.category.length; i++) {
							if(vm.category[i] == vm.applications[j].category){
								find = true;
							}
						}
						
						if(!find){
							vm.category.push(vm.applications[j].category);
						}
					}else{
						var find = false;
						for (var i = 0; i < vm.category.length; i++) {
							if(vm.category[i] == 'General'){
								find = true;
							}
						}
						if(!find){
							vm.category.push('General');
						}	
					}
				}
				getApplicationInCategory(vm.category[vm.selectedCategory]);
			});
		}
		
		
//		vm.showProperties = showProperties
//		function showProperties(name, show) {
//			for (var j = 0; j < vm.applications.length; j++) {
//				if (vm.applications[j].name == name) {
//					vm.applications[j].showProperties = show;
//				}
//			}
//		}
//		
//		vm.showUnites = showUnites
//		function showUnites(name, show) {
//			for (var j = 0; j < vm.measures.length; j++) {
//				if (vm.measures[j].name == name) {
//					vm.measures[j].showUnites = show;
//				}
//			}
//		}
//		
//		vm.showDependencies = showDependencies
//		function showDependencies(name, show) {
//			for (var j = 0; j < vm.measures.length; j++) {
//				if (vm.measures[j].name == name) {
//					vm.measures[j].showDependencies = show;
//				}
//			}
//		}

		vm.isUpload = false;
		vm.uploadFile = uploadFile;

		function uploadFile() {
			Application.upload($scope.fileread, onUploadSuccess, onUploadError);
		}

		function onUploadSuccess(result) {
			vm.isUpload = true;
		}

		function onUploadError() {
			vm.isUpload = false;
		}
		
		
		vm.isActive = isActive;

		function isActive(idx) {
			if (idx == vm.selectedCategory) {
				return 'active';
			}
			return '';
		}

		
	
		function getApplicationInCategory(category){
			vm.applicationsInCategory = [];
			
			for (var i = 0; i < vm.applications.length; i++) {
				if(vm.applications[i].category == category){
					vm.applicationsInCategory.push(vm.applications[i]);
				}
				
				if((vm.applications[i].category == null || vm.applications[i].category == "")   && category == "General"){
					vm.applicationsInCategory.push(vm.applications[i]);
				}
			}
		}
		
	
		
		
		vm.setActive = setActive;
		

		
		function setActive(idx) {
			vm.selectedCategory = idx;			
			$cookies.put("selectedCategory",idx);
			getApplicationInCategory(vm.category[vm.selectedCategory]);
		}
	}
})();
