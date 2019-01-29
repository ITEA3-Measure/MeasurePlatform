(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('MeasureController',
			MeasureController);

	MeasureController.$inject = [ '$scope','$cookies', 'Principal', 'LoginService',
			'$state', 'Measure' ];

	function MeasureController($scope,$cookies, Principal, LoginService, $state, Measure) {
		var vm = this;

		vm.measures = [];
		vm.category = [];
		vm.measuresInCategory = [];

		
		
		// Tab Management
		vm.selectedCategory = $cookies.get("selectedCategory");	
		if(vm.selectedCategory == null){
			vm.selectedCategory = "0";
		}
		
		loadAll();
	
		function loadAll() {
			Measure.allmeasures(function(result) {
				vm.measures = result;
				for (var j = 0; j < vm.measures.length; j++) {
					vm.measures[j].showUnites  = false;
					vm.measures[j].showDependencies = false;
					vm.measures[j].showProperties = false;
					
					if(vm.measures[j].category != null && vm.measures[j].category != ""){
						var find = false;
						for (var i = 0; i < vm.category.length; i++) {
							if(vm.category[i] == vm.measures[j].category){
								find = true;
							}
						}
						
						if(!find){
							vm.category.push(vm.measures[j].category);
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
				getMeasureInCategory(vm.category[vm.selectedCategory]);
			});
		}
		
		
		vm.showProperties = showProperties
		function showProperties(name, show) {
			for (var j = 0; j < vm.measures.length; j++) {
				if (vm.measures[j].name == name) {
					vm.measures[j].showProperties = show;
				}
			}
		}
		
		vm.showUnites = showUnites
		function showUnites(name, show) {
			for (var j = 0; j < vm.measures.length; j++) {
				if (vm.measures[j].name == name) {
					vm.measures[j].showUnites = show;
				}
			}
		}
		
		vm.showDependencies = showDependencies
		function showDependencies(name, show) {
			for (var j = 0; j < vm.measures.length; j++) {
				if (vm.measures[j].name == name) {
					vm.measures[j].showDependencies = show;
				}
			}
		}

		vm.isUpload = false;
		vm.uploadFile = uploadFile;

		function uploadFile() {
			Measure.upload($scope.fileread, onUploadSuccess, onUploadError);
		}

		function onUploadSuccess(result) {
			vm.isUpload = true;
		}

		function onUploadError() {
			vm.isUpload = false;
		}
		
		
		function getMeasureInCategory(category){
			vm.measuresInCategory = [];	
			for (var i = 0; i < vm.measures.length; i++) {
				if(vm.measures[i].category == category){
					vm.measuresInCategory.push(vm.measures[i]);
				}
				
				if((vm.measures[i].category == null || vm.measures[i].category == "")   && category == "General"){
					vm.measuresInCategory.push(vm.measures[i]);
				}
			}
		}
		
		
		vm.isActive = isActive;
		function isActive(idx) {
			if (idx == vm.selectedCategory) {
				return 'active';
			}
			return '';
		}

		vm.setActive = setActive;
		function setActive(idx) {
			vm.selectedCategory = idx;			
			$cookies.put("selectedCategory",idx);
			getMeasureInCategory(vm.category[vm.selectedCategory]);
		}
	}
})();
