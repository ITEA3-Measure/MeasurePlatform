(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('GraphicDialogController', GraphicDialogController);

    GraphicDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','entity','project', 'phase', 'dashboard','data', 'MeasureView','ProjectInstances','Measure','ConfigurationService'];

    function GraphicDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,project, phase,dashboard,data,MeasureView,ProjectInstances,Measure,ConfigurationService) {
        var vm = this;
        vm.measureView = entity;
        vm.project = project;
        vm.phase = phase;
        vm.dashboard = dashboard;   
        vm.data = data;
        
        if(vm.measureView.auto == null){
        	vm.measureView.auto = false;
        }
        
        vm.measureInstances = [];
        
        if(vm.measureView != null && vm.measureView.projectoverview != null){
        	loadAll(vm.measureView.projectoverview.id);
        }else if(vm.project != null){
        	loadAll(vm.project.id);
        }else if(vm.phase != null){
        	loadAll(vm.phase.project.id);
        }else if(vm.dashboard != null){
        	loadAll(vm.dashboard.phase.project.id);
        }
        
        
        vm.changemode = changemode;
        vm.measureView.mode = "AUTO";

		function changemode(mode) {
			vm.measureView.mode = mode;
		}
        
		function loadAll(id) {
			ProjectInstances.instances({
				id : id
			}, function(result) {
				vm.measureInstances = result;
				
				if(vm.measureView.measureinstance != null){
					for(var i = 0;i<vm.measureInstances.length;i++){
						if(vm.measureInstances[i].instanceName == vm.measureView.measureinstance.instanceName){
							vm.measureView.measureinstance = vm.measureInstances[i];
						}
					}
				}
			});

			ConfigurationService.kibanaadress(function(result) {
				vm.kibanaURL = "http://" + result.kibanaAdress + "/app/kibana";			
			});
			
		}

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });
        
        $scope
		.$watch(
				"vm.measureView.measureinstance",
				function(newValue, oldValue) {			
					Measure.get({
						id : vm.measureView.measureinstance.measureName
					}, function(result) {
						vm.selectedMeasureDefinition = result;
						 vm.numericProperties = [];
					     vm.dateIndexs = [];
					     
					     for(var i = 0; i < result.unit.fields.length;i++){
					    	 if(result.unit.fields[i].fieldType == 'u_double' ||
					    		result.unit.fields[i].fieldType == 'u_integer' ||
					    		result.unit.fields[i].fieldType == 'u_short' ||
					    		result.unit.fields[i].fieldType == 'u_float' ||
					    		result.unit.fields[i].fieldType == 'u_long' ||
					    		result.unit.fields[i].fieldType == 'u_half_float' ||
					    		result.unit.fields[i].fieldType == 'u_scaled_float'){
					    		 vm.numericProperties.push(result.unit.fields[i].fieldName);
					    	 }else if(result.unit.fields[i].fieldType == 'u_date'){
					    		 vm.dateIndexs.push(result.unit.fields[i].fieldName);
					    	 }
					     }
					        
					});							
				});
 
        vm.clear = clear;       
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        
        
        vm.kibanavisualisations = [];
        loadKibanaVisualisations();
        
        function loadKibanaVisualisations() {
        	MeasureView.allkibanavisualisations(function(result) {
				vm.kibanavisualisations = result;
			});
		}
        
        
        vm.kibanadashboards = [];
        loadKibanaDashboards();
        
        function loadKibanaDashboards() {
        	MeasureView.allkibanadashboards(function(result) {
				vm.kibanadashboards = result;
			});
		}

        
        vm.save = save;
        function save () {
            vm.isSaving = true;
            if (vm.measureView.id !== null) {
                MeasureView.update(vm.measureView, onSaveSuccess, onSaveError);
            } else {
            	if(vm.project != null){
            		if(vm.data.isOverview){
            			vm.measureView.projectoverview = vm.project;
            		}else{
            			vm.measureView.project= vm.project;
            		}
            	}else if(vm.phase != null){
            		if(vm.data.isOverview){
            			vm.measureView.phaseoverview = vm.phase;
            		}else{
            			vm.measureView.phase= vm.phase;
            		}
            	}else if(vm.dashboard != null){
            		vm.measureView.dashboard= vm.dashboard;
            	}
            	
                MeasureView.save(vm.measureView, onSaveSuccess, onSaveError);
            }
        }
        
        
        vm.reloadVisualisations = loadKibanaVisualisations;      
        vm.reloadDashboards = loadKibanaDashboards;
        
        function onSaveSuccess (result) {
            $scope.$emit('measurePlatformApp:measureViewUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();


