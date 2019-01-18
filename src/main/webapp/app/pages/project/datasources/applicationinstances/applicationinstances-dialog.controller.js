(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectApplicationInstancesDialogController', ProjectApplicationInstancesDialogController);

	ProjectApplicationInstancesDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'project',
			'ApplicationInstances', 'Measure', 'MeasureProperty', 'Application', 'ProjectDataSources' ];

	function ProjectApplicationInstancesDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, project, ApplicationInstances, Measure,
			MeasureProperty, Application, ProjectDataSources) {
		var vm = this;
		

		vm.applicationInstance = entity;
		vm.applicationInstance.project = project;

		vm.clear = clear;
		vm.save = save;

		vm.allapplications = [];

		vm.selectedapplication = null;

		loadApplicationsFromCatalogue();

		/*
		 * watch if an application was selected: when selector of application type is changed
		 */
		$scope
		.$watch(
				"vm.selectedapplication",
				function(newValue, oldValue) {

					vm.applicationInstance.properties = [];
					// vm.applicationMeasuresInstances = [];

					if (newValue != null) {	

						/*
						 * update vm.applicationInstance
						 */
						vm.applicationInstance.applicationType = newValue.name;


						// Get application Instance configuration by applicationType
						ApplicationInstances.getApplicationConfigurationByApplicationType({id: vm.applicationInstance.applicationType},
							function(result){
								
								result.properties.forEach((vProperty) => {
									console.log(vProperty);
									vm.applicationInstance.properties.push(vProperty);
								});

							});
						



					}
				});
		

		
		function newProperty() {
			return {
				propertyName : null,
				propertyValue : null,
				propertyType : null,
				enumvalues : [],
				id : null
			}
		};		
		
		
		function newEnumValue() {
			return {
				label : null,
				value : null
			}
		};
		
		
		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		vm.errorMessage = "";
		
		function save() {
			vm.isSaving = true;
			
			/*
			 * Check if this is an edit or new
			 */
			if(vm.applicationInstance.id != null){
				ApplicationInstances.update(vm.applicationInstance,onSaveSuccess, onSaveError);
			}else{
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
			}
			

			

		}

		function onSaveSuccess(result) {
			$uibModalInstance.close(result);
			vm.isSaving = false;
			
			/*
			 * save measures instances associated to the application instance
			 */
			// vm.applicationMeasuresInstances.forEach(function(elt){
			// 	elt.measureInstance.application = result;
			// 	elt.measureInstance.instanceName = 
			// 		vm.applicationInstance.name + elt.measureInstance.instanceName;
				 
			// 	ProjectDataSources.save(elt.measureInstance, 
			// 			onSaveMeasureInstanceSuccess, onSaveMeasureInstanceError);
			// 	//elt.propertiesNames
			// });
			
		}


		function onSaveError() {
			vm.isSaving = false;
		}

		
		function newMeasureInstance() {
			return {
				instanceName : null,
				instanceDescription : null,
				measureName : null,
				measureVersion : null,
				isShedule : null,
				shedulingExpression : null,
				schedulingUnit : null,
				measureType : null,
				remoteAdress : null,
				remoteLabel : null,
				isRemote : null,
				project : null,
				application : null,
				id : null
			}
		};
		
		function onSaveMeasureInstanceSuccess() {

		}
		
		function onSaveMeasureInstanceError() {
			vm.isSaving = false;

		}



		function loadApplicationsFromCatalogue() {
			Application
					.allapplications(function(result) {
						vm.allapplications = result;
						for (var i = 0; i < vm.allapplications.length; i++) {
							if (vm.applicationInstance.applicationType == vm.allapplications[i].name) {
								vm.selectedapplication = vm.allapplications[i];
							}
						}
					});
		}	
		
	}
})();
