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
		
		
		var applicationupdate = false;

		vm.applicationInstance = entity;
		vm.applicationInstance.project = project;

		vm.clear = clear;
		vm.save = save;

		vm.selectedapplication = null;

		vm.currentProperties = {};
		vm.properties = {};

		vm.isObjectEmpty = function(obj){
			   return Object.keys(obj).length === 0;
		}

		vm.applicationInstanceDashboards = [];
		
		vm.allapplications = [];
	
		/*
		 * Structure of the elements on this list
		 * 
		 * {
		 * 		measureInstance : {},
		 * 		propertiesNames : []
		 * }
		 * 
		 */
		vm.applicationMeasuresInstances = [];
		
		//vm.applicationMeasuresInstanceProperties = [];

		/*
		 * check if this dialogue is for an edit (vm.applicationInstance.id != null)
		 */
		if (vm.applicationInstance.id != null) {
			loadProperties(vm.applicationInstance.id);
		}

		/*
		 * Load application from the catalogue for the selector
		 */
		loadApplications();

		function loadApplications() {
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
		

		/*
		 * watch if an application was selected
		 */
		$scope
		.$watch(
				"vm.selectedapplication",
				function(newValue, oldValue) {

					vm.properties = {};
					vm.applicationMeasuresInstances = [];

					
					if (newValue != null && oldValue != null && oldValue != newValue) {	
						applicationupdate = true;
					}
					if (newValue != null) {	

						/*
						 * update vm.applicationInstance
						 */
						vm.applicationInstance.applicationType = newValue.name;

						
						/*
						 * Navigate Dashboards in application
						 */
						newValue.dashboards.dashboard.forEach((vDashboard, i) => {
							
							/*
							 * create new dashboard 
							 */
							//vm.applicationInstanceDashboards.push({dashboardName : vDashboard.label});

							/*
							 * Navigate measures in the current dashboard
							 */
							vDashboard.measures.measure.forEach((vMeasure, j) => {

								/*
								 * GET Measure corresponding to the name"vMeasure.name" from the Catalogue
								 */
								Measure.get({id: vMeasure.name}, function(result){


									/*
									 * Create new Measure instance
									 */
									var varMeasureInstance = newMeasureInstance();
									varMeasureInstance.measureName = result.name;
									varMeasureInstance.instanceName = '_Measure_' +  result.name + '_' + i + '_' + j;
									varMeasureInstance.measureVersion = "1.0.0";
									varMeasureInstance.measureType = result.type;
									varMeasureInstance.schedulingUnit = 's';
									varMeasureInstance.shedulingExpression = vMeasure.scheduling;
									varMeasureInstance.project = project;
									
									var varApplicationMeasureInstance = {
											measureInstance : varMeasureInstance,
											propertiesNames : []
											};
									
									
									
									/*
									 * update properties in the view model
									 */
									for (var k = 0; k < result.scopeProperties.length; k++) {
										
										var vProperty = createPropertyFromScopeProperty(result.scopeProperties[k]);

										var varPropertyName = result.scopeProperties[k].name;
										vm.properties[varPropertyName] = vProperty;
										varApplicationMeasureInstance.propertiesNames.push(varPropertyName);
										
									}

									
									vm.applicationMeasuresInstances.push(varApplicationMeasureInstance);

									
								});
							});
						

							
						});

					}
				});
		
		

		function createPropertyFromScopeProperty(scopeProperty){
			var o = newProperty();

			o.propertyName = scopeProperty.name;
			o.propertyType = scopeProperty.type;
			
			if(scopeProperty.type == 'ENUM'){
				for (var j = 0; j < scopeProperty.enumType.enumvalue.length; j++) {
					var eva = newEnumValue();
					eva.label = scopeProperty.enumType.enumvalue[j].label;
					eva.value = scopeProperty.enumType.enumvalue[j].value;
					
					o.enumvalues[j] = eva;
				}
			}
			
			var propval = null;
			if(vm.applicationInstance.id != null){												
//				for(var j = 0;j < vm.currentProperties.length; j++){
//					if(vm.currentProperties[j].propertyName == o.propertyName){
//						propval = vm.currentProperties[j].propertyValue;
//						o.id = vm.currentProperties[j].id;
//					}			
//				}
				
			}else {
				propval = scopeProperty.defaultValue;		
			}

			
			if(scopeProperty.type == 'INTEGER' || scopeProperty.type == 'FLOAT' ){
				o.propertyValue = Number(propval);		
			}else if(scopeProperty.type == 'ENUM'){
				o.propertyValue = propval;	
			}else if(scopeProperty.type == 'DATE'){
				o.propertyValue = new Date(propval);	
			}else{
				o.propertyValue = propval;
			}
			
			return o;
		}
		
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
			vm.applicationMeasuresInstances.forEach(function(elt){
				elt.measureInstance.application = result;
				elt.measureInstance.instanceName = 
					vm.applicationInstance.name + elt.measureInstance.instanceName;
				 
				ProjectDataSources.save(elt.measureInstance, 
						onSaveMeasureInstanceSuccess, onSaveMeasureInstanceError);
				//elt.propertiesNames
			});
			
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
		
	}
})();
