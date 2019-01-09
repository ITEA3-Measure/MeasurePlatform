(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectDataSourceDialogController', ProjectDataSourceDialogController);

	ProjectDataSourceDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'project',
			'ProjectDataSources', 'Measure', 'MeasureProperty' ];

	function ProjectDataSourceDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, project, ProjectDataSources, Measure,
			MeasureProperty) {
		var vm = this;
		var measureupdate = false;

		vm.measureInstance = entity;
		vm.measureInstance.project = project;
		vm.measureInstance.createDefaultView = true ;

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
			ProjectDataSources
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
			ProjectDataSources
					.properties(
							{
								id : id
							},
							function(result) {
								vm.measureInstance.properties = [];
								for (var i = 0; i < result.length; i++) {		
									vm.currentProperties.push(result[i]);	
								}
							});
		}
	
		$scope
				.$watch(
						"vm.selectedmeasure",
						function(newValue, oldValue) {
							vm.references =[];
							vm.properties = [];
							
							if (newValue != null && oldValue != null && oldValue != newValue) {	
								measureupdate = true;
							}
							if (newValue != null) {			
								for (var i = 0; i < newValue.scope.property.length; i++) {
									var o = newProperty();
									o.propertyName = newValue.scope.property[i].name;
									o.propertyType = newValue.scope.property[i].type;
									
									if(newValue.scope.property[i].type == 'ENUM'){
										for (var j = 0; j < newValue.scope.property[i].enumType.enumvalue.length; j++) {
											var eva = newEnumValue();
											eva.label = newValue.scope.property[i].enumType.enumvalue[j].label;
											eva.value = newValue.scope.property[i].enumType.enumvalue[j].value;
											
											o.enumvalues[j] = eva;
										}
									}
									
									var propval = null;
									if(vm.measureInstance.id != null){												
										for(var j = 0;j < vm.currentProperties.length; j++){
											if(vm.currentProperties[j].propertyName == o.propertyName){
												propval = vm.currentProperties[j].propertyValue;
												o.id = vm.currentProperties[j].id;
											}			
										}
										
									}else {
										propval = newValue.scope.property[i].defaultValue;		
									}
									
									
									if(newValue.scope.property[i].type == 'INTEGER' || newValue.scope.property[i].type == 'FLOAT' ){
										o.propertyValue = Number(propval);		
									}else if(newValue.scope.property[i].type == 'ENUM'){
										o.propertyValue = propval;	
									}else if(newValue.scope.property[i].type == 'DATE'){
										o.propertyValue = new Date(propval);	
									}else{
										o.propertyValue = propval;
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
								
								if(newValue.agentId == null){
									vm.measureInstance.isRemote = false;
								}else{
									vm.measureInstance.isRemote = true;	
									vm.measureInstance.remoteLabel = newValue.agentId;
								} 
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
			ProjectDataSources
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

		vm.errorMessage = "";
		function save() {
			vm.isSaving = true;
			if(vm.measureInstance.id != null){
				ProjectDataSources.update(vm.measureInstance,onSaveSuccess, onSaveError);					
			}else{
				ProjectDataSources.checkname(
						{
							name : vm.measureInstance.instanceName
						},
						function(result) {
							if(result.id == null){
								ProjectDataSources.save(vm.measureInstance,onSaveSuccess, onSaveError);			
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
			
			
			if(measureupdate){
				for(var i = 0;i<vm.currentProperties.length; i++) {
					MeasureProperty.delete(vm.currentProperties[i]);
				}

				for (var i = 0; i < vm.properties.length; i++) {
					vm.properties[i].measureInstance = result;
					MeasureProperty.save(vm.properties[i]);
				}
			}else{
				for (var i = 0; i < vm.properties.length; i++) {
					vm.properties[i].measureInstance = result;
					MeasureProperty.update(vm.properties[i]);
				}				
			}
			
			
			
			for(var i = 0;i<vm.currentReferences.length; i++) {
				ProjectDataSources.deletereference(vm.currentReferences[i]);
			}
			
			for (var i = 0; i < vm.references.length; i++) {
				vm.references[i].ownerInstance = result;
				ProjectDataSources.savereference(vm.references[i]);
			}
			
			ProjectDataSources.createDefaultVisualisation({id : result.id},
					function(result) {
						
					});
		}

		function onSavePropertySuccess(result) {
			vm.isSaving = false;
		}

		function onSaveError() {
			vm.isSaving = false;
		}
		
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }

	}
})();
