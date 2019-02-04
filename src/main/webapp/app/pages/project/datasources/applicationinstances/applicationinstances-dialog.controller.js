(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectApplicationInstancesDialogController', ProjectApplicationInstancesDialogController);

	ProjectApplicationInstancesDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'project','param',
			'ApplicationInstances', 'Application' ];

	function ProjectApplicationInstancesDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, project,param, ApplicationInstances, Application) {
		var vm = this;
		vm.allapplications = [];
		vm.selectedapplication = null;
		vm.errorMessage = "";	
		vm.param = param;
		vm.isSaving = false;
		
		
		if(entity != null){		
			vm.applicationInstance = entity;
			
			vm.selectedapplication = vm.applicationInstance;
			
		    for(var i = 0; i < vm.applicationInstance.properties.length; i++){
		    	if(vm.applicationInstance.properties[i].type == 'INTEGER' || vm.applicationInstance.properties[i].type == 'FLOAT' ){
		    		vm.applicationInstance.properties[i].value = Number(vm.applicationInstance.properties[i].value);		
				}else if(vm.applicationInstance.properties[i].type == 'DATE'){
					vm.applicationInstance.properties[i].value = new Date(vm.applicationInstance.properties[i].value);	
				}
		    }	
		}else{
			loadApplicationsFromCatalogue();
		}
		
		if(vm.applicationInstance == null){
			$scope.$watch("vm.selectedapplication", function(newValue, oldValue) {
				if (newValue != null) {	
					// Get application Instance configuration by applicationType
					ApplicationInstances.getConfigurationByApplicationType({applicationType: vm.selectedapplication.name},
						function(result){		
						    vm.applicationInstance = result;
						    
						    for(var i = 0; i < vm.applicationInstance.properties.length; i++){
						    	if(vm.applicationInstance.properties[i].type == 'INTEGER' || vm.applicationInstance.properties[i].type == 'FLOAT' ){
						    		vm.applicationInstance.properties[i].value = Number(vm.applicationInstance.properties[i].value);		
								}else if(vm.applicationInstance.properties[i].type == 'DATE'){
									vm.applicationInstance.properties[i].value = new Date(vm.applicationInstance.properties[i].value);	
								}
						    }
					
							
							vm.applicationInstance.project = project;
						});
				}else{
					vm.applicationInstance = null;
				}
			});
		}


		
		vm.clear = clear;
		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		vm.save = save;
		function save() {
			vm.isSaving = true;
			
			/*
			 * Check if this is an edit or new
			 */
			if(vm.applicationInstance.id != null){
				ApplicationInstances.update(vm.applicationInstance,onSaveSuccess, onSaveError);
			}else{
				if(vm.applicationInstance.name != null){
					ApplicationInstances.checkname(
							{
								name : vm.applicationInstance.name
							},
							function(result) {
								if(result.id == null){	
									ApplicationInstances.save(vm.applicationInstance,onSaveSuccess, onSaveError);			
								}else{
									vm.isSaving = false;
									vm.errorMessage = "An Instance with the same name already exist"
								}
							});
				}else{
					vm.isSaving = false;
				}				
			}
			
		}

		function onSaveSuccess(result) {
			$uibModalInstance.close(result);
			vm.isSaving = false;		
		}


		function onSaveError() {
			vm.isSaving = false;
		}

		

		
		function onSaveMeasureInstanceSuccess() {
			vm.isSaving = false;
		}
		
		function onSaveMeasureInstanceError() {
			vm.isSaving = false;
		}

		function loadApplicationsFromCatalogue() {
			Application
					.allapplications(function(result) {
						vm.allapplications = result;
					});
		}	
		
	}
})();
