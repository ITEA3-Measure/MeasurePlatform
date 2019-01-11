(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'PhaseDashboardDialogController', PhaseDashboardDialogController);

	PhaseDashboardDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'project', 'Dashboard','MeasureView','ConfigurationService'];

	function PhaseDashboardDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, project, Dashboard,MeasureView,ConfigurationService) {
		var vm = this;
		vm.dashboard = entity;
		vm.project = project;
		vm.isSaving = false;
		vm.save=save;
		
		loadConfiguration();
		
		function loadConfiguration() {
			ConfigurationService.kibanaadress(function(result) {
				vm.kibanaURL = "http://" + result.kibanaAdress + "/app/kibana";			
			});		
		}
		
		vm.active = isactive;
		
		function isactive(mode){
			if(vm.dashboard.mode == mode){
				return "active";
			}
			return "";
		}
		
		vm.kibanadashboards = [];
        loadKibanaDashboards();
        
        function loadKibanaDashboards() {
        	MeasureView.allkibanadashboards(function(result) {
				vm.kibanadashboards = result;
			});
		}
        
        vm.reloadDashboards = loadKibanaDashboards;
		
        
        if(vm.dashboard.timePeriode == null){
        	vm.dashboard.timePeriode = "from:now-1y,mode:quick,to:now"
        }       
        
        if(vm.dashboard.size == null){
        	vm.dashboard.size = "600"
        }
        
        vm.timePeriodeValue = vm.dashboard.timePeriode.substring( vm.dashboard.timePeriode.indexOf(",mode:quick,to:now")-1,vm.dashboard.timePeriode.indexOf(",mode:quick,to:now"));
        vm.timePeriodeIndex = vm.dashboard.timePeriode.substring(9, vm.dashboard.timePeriode.indexOf(",mode:quick,to:now")-1);

        
        function updateTimePeriode (){
        	if(vm.timePeriodeValue =='other'){
        		vm.dashboard.timePeriode = vm.timePeriodeIndex;
        	}else{
        		vm.dashboard.timePeriode = "from:now-"+vm.timePeriodeIndex +vm.timePeriodeValue+",mode:quick,to:now";
        	}	        	
        }
        
		function save() {
			vm.isSaving = true;
			updateTimePeriode ();
			if (vm.dashboard.id != null) {
				Dashboard.update(vm.dashboard, onSaveSuccess, onSaveError);
			} else {
				vm.dashboard.project = vm.project;
				vm.dashboard.editable = true;
				Dashboard.save(vm.dashboard, onSaveSuccess, onSaveError);
			}
		}

		function onSaveSuccess(result) {
			$uibModalInstance.close(result);
			vm.isSaving = false;
		}

		function onSaveError() {
			vm.isSaving = false;
		}
		vm.clear=clear;
	    function clear () {
            $uibModalInstance.dismiss('cancel');
        }
	}
})();
