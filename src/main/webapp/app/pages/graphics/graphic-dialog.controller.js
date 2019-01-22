(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('GraphicDialogController', GraphicDialogController);

    GraphicDialogController.$inject = ['$timeout', '$scope', 
    		'$stateParams', '$uibModalInstance','entity','project', 'dashboard','data', 'MeasureView','GraphicService','Measure',
    		'ConfigurationService','AnalysisCard'];

    function GraphicDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,project,dashboard,data,MeasureView,GraphicService,Measure,ConfigurationService,AnalysisCard) {
        var vm = this;
        vm.measureView = entity;
        vm.project = project;
        vm.dashboard = dashboard;   
        vm.data = data;
        
        if(vm.measureView.timePeriode == null){
        	vm.measureView.timePeriode = "from:now-1y,mode:quick,to:now"
        }       
        vm.timePeriodeValue = vm.measureView.timePeriode.substring( vm.measureView.timePeriode.indexOf(",mode:quick,to:now")-1,vm.measureView.timePeriode.indexOf(",mode:quick,to:now"));
        vm.timePeriodeIndex = vm.measureView.timePeriode.substring(9, vm.measureView.timePeriode.indexOf(",mode:quick,to:now")-1);
        
        
        if(vm.measureView.timeAgregation == null){
        	vm.measureView.timeAgregation = "d"
        }          
         
        if(vm.measureView.auto == null){
        	vm.measureView.auto = false;
        }
        
        vm.measureInstances = [];
        
        if(vm.measureView != null && vm.measureView.projectoverview != null){
        	loadAll(vm.measureView.projectoverview.id);
        }else if(vm.project != null){
        	loadAll(vm.project.id);
        }else if(vm.dashboard != null){
        	loadAll(vm.dashboard.project.id);
        }
        
        vm.cards = [];
        loadAnalysisCard(vm.project.id);
        function loadAnalysisCard(id) {
       	 AnalysisCard.byprojects({
    			id : id
    		}, function(result) {
    			vm.cards = result;
    			for(var i = 0;i<vm.cards.length;i++){
    				if(vm.cards[i].id == vm.measureView.analysiscard.id){
    					vm.measureView.analysiscard = vm.cards[i];
					}
    			}
    		});
        }
        
        if(vm.measureView.mode == null){    
        	vm.measureView.mode = "AUTO";
        }

		function loadAll(id) {
			GraphicService.instances({
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
        
        vm.active = active;
        function active(mode){
        	if(vm.measureView.mode == mode){
        		return 'active';
        	}
        	
        	return '';
        }
        
        
        $scope
		.$watch(
				"vm.measureView.measureinstance",
				function(newValue, oldValue) {	
					if(vm.measureView.measureinstance != null){
						Measure.get({
							id : vm.measureView.measureinstance.measureName
						}, function(result) {
							 vm.selectedMeasureDefinition = result;
							 vm.numericProperties = [];
							 vm.allProperties = [];
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
						    	 var property = new Object();
						    	 property.label = result.unit.fields[i].fieldName;
						    	 property.selected = true;
						    	 vm.allProperties.push(property);
						     }
						        
						});	
					}										
				});
          
    	
        $scope
		.$watch(
				"vm.timePeriodeValue",
				function(newValue, oldValue) {
					if(vm.timePeriodeValue =='other'){
						vm.timePeriodeIndex = "from:now-1y,mode:quick,to:now";
					}else{
						if(isNaN(parseInt(vm.timePeriodeIndex))){
							vm.timePeriodeIndex = "1"
						}
					}					
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
        	updateTimePeriode();
        	updateValueList();
            if (vm.measureView.id !== null) {
                MeasureView.update(vm.measureView, onSaveSuccess, onSaveError);
            } else {
            	if(vm.project != null && vm.phase == null && vm.dashboard == null){
            		if(vm.data.isOverview){
            			vm.measureView.projectoverview = vm.project;
            		}else{
            			vm.measureView.project= vm.project;
            		}
            	}else if(vm.phase != null && vm.dashboard == null){
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
        
        function updateTimePeriode (){
        	if(vm.timePeriodeValue =='other'){
        		vm.measureView.timePeriode = vm.timePeriodeIndex;
        	}else{
        		vm.measureView.timePeriode = "from:now-"+vm.timePeriodeIndex +vm.timePeriodeValue+",mode:quick,to:now";
        	}	        	
        }
        
        function updateValueList(){
        	if(vm.measureView.mode == 'VALUE' || vm.measureView.mode == 'TABLE'){
        		var datas = '';
        		
        		for(var i = 0 ; i < vm.allProperties.length; i++){
        			if(vm.allProperties[i].selected == true){
        				datas = datas + "," + vm.allProperties[i].label;
        			}
        		}
        		vm.measureView.visualisedProperty = datas.substring(1);
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


