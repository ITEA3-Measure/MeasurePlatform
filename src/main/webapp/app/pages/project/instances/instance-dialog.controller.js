(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectInstanceDialogController', ProjectInstanceDialogController);

	ProjectInstanceDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'project',
			'ProjectInstances', 'Measure', 'MeasureProperty' ];

	function ProjectInstanceDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, project, ProjectInstances, Measure,
			MeasureProperty) {
		var vm = this;

		vm.measureInstance = entity;
		vm.measureInstance.project = project;

		vm.clear = clear;
		vm.save = save;

		vm.selectedmeasure = null;

		vm.currentProperties = [];
		vm.properties = [];
		
		vm.references = [];
		vm.currentReferences = [];
		
		vm.allmeasures = [];
		
		if (vm.measureInstance.id != null) {
			loadProperties(vm.measureInstance.id);
			loadReferences(vm.measureInstance.id);
		}

		loadMeasures();

		function loadMeasures() {
			Measure
					.allmeasures(function(result) {
						vm.allmeasures = result;
						for (var i = 0; i < vm.allmeasures.length; i++) {
							if (vm.measureInstance.measureName == vm.allmeasures[i].name) {
								vm.selectedmeasure = vm.allmeasures[i];
							}
						}
					});
		}
		
		function loadReferences(id) {
			ProjectInstances
					.references(
							{
								id : id
							},
							function(result) {
								vm.measureInstance.references = [];
								for (var i = 0; i < result.length; i++) {
									vm.currentReferences.push(result[i]);
								}
							});
		}
		
		
		function loadProperties(id) {
			ProjectInstances
					.properties(
							{
								id : id
							},
							function(result) {
								vm.measureInstance.properties = [];
								for (var i = 0; i < result.length; i++) {
									vm.currentProperties.push(result[i]);
									if (vm.properties[i] != null && vm.properties[i].propertyName == result[i].propertyName) {
										vm.properties[i].propertyValue = result[i].propertyValue;
									}
								}
							});
		}



		$scope
				.$watch(
						"vm.selectedmeasure",
						function(newValue, oldValue) {
							vm.references =[];
							vm.properties = [];
							if (newValue != null) {									
								for (var i = 0; i < newValue.scopeProperties.length; i++) {
									var o = newProperty();
									o.propertyName = newValue.scopeProperties[i].name;
									if (vm.currentProperties[i] != null && vm.currentProperties[i].propertyName == o.propertyName) {
										o.propertyValue = vm.currentProperties[i].propertyValue;
									} else {
										o.propertyValue = newValue.scopeProperties[i].defaultValue;
									}
									vm.properties[i] = o;
								}
								
								
								for (var i = 0; i < newValue.references.length; i++) {
									var o = newReference();
									o.role = newValue.references[i].role;
									o.numberRef = newValue.references[i].number;											
									vm.references[i] = o;
									
									loadInstancesByRole(i,o.role,newValue.references[i].measureRef);				
								}
								
								vm.measureInstance.measureName = newValue.name;
								vm.measureInstance.measureVersion = "1.0.0";
								vm.measureInstance.measureType = newValue.type;
								
								if(newValue.callbackAdress == null){
									vm.measureInstance.isRemote = false;
								}else{
									vm.measureInstance.isRemote = true;	
									vm.measureInstance.remoteLabel = newValue.callbackLable;
									vm.measureInstance.remoteAdress = newValue.callbackAdress;
								} 
							}
						});

		function newProperty() {
			return {
				propertyName : null,
				propertyValue : null,
				id : null
			}
		};
		
		
		function newReference() {
			return {
				id : null,
				numberRef : null,
				filterExpression : null,
				role : null,
				candidateInstances: [],
				referencedInstance : null
			}
		};
		
		
		function loadInstancesByRole(refindex,role,measureRef) {
			ProjectInstances
					.instancesofmeasure(
							{
								measureRef : measureRef
							},
							function(result) {
								vm.references[refindex].candidateInstances = result;
														
								for (var i = 0 ; i < vm.currentReferences.length;i++){
									if (vm.currentReferences[i] != null && vm.currentReferences[i].role == role) {
										for(var j = 0 ; j<vm.references[refindex].candidateInstances.length;j++){
											if(vm.references[refindex].candidateInstances[j].id == vm.currentReferences[i].referencedInstance.id){
												vm.references[refindex].referencedInstance = vm.references[refindex].candidateInstances[j];
											}
										}						
									}
								}	
							});
		}

		$timeout(function() {
			angular.element('.form-group:eq(1)>input').focus();
		});

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function save() {
			vm.isSaving = true;
			if(vm.measureInstance == null){
				ProjectInstances.save(vm.measureInstance,
						onSaveSuccess, onSaveError);
			}else{
				ProjectInstances.update(vm.measureInstance,
						onSaveSuccess, onSaveError);
			}
			
		}

		function onSaveSuccess(result) {
			$uibModalInstance.close(result);
			vm.isSaving = false;
			
			for(var i = 0;i<vm.currentProperties.length; i++) {
				MeasureProperty.delete(vm.currentProperties[i]);
			}

			for (var i = 0; i < vm.properties.length; i++) {
				vm.properties[i].measureInstance = result;
				MeasureProperty.save(vm.properties[i]);
			}
			
			
			for(var i = 0;i<vm.currentReferences.length; i++) {
				ProjectInstances.deletereference(vm.currentReferences[i]);
			}
			
			for (var i = 0; i < vm.references.length; i++) {
				vm.references[i].ownerInstance = result;
				ProjectInstances.savereference(vm.references[i]);
			}
		}

		function onSavePropertySuccess(result) {
			vm.isSaving = false;
		}

		function onSaveError() {
			vm.isSaving = false;
		}

	}
})();
