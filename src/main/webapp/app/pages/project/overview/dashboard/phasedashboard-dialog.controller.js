(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'PhaseDashboardDialogController', PhaseDashboardDialogController);

	PhaseDashboardDialogController.$inject = [ '$timeout', '$scope', '$state',
			'$stateParams', '$uibModalInstance', 'entity', 'project', 'Principal', 'Dashboard', 'MeasureView', 'ConfigurationService', 'UsersRightAccessService'];

	function PhaseDashboardDialogController($timeout, $scope, $state, $stateParams,
			$uibModalInstance, entity, project, Principal, Dashboard, MeasureView, ConfigurationService, UsersRightAccessService) {
		var vm = this;
		vm.dashboard = entity;
		vm.dashboard.sharedDashboard ="unchecked";
		vm.project = project;
		vm.dashboard.inviters = [];
		vm.candidates = [];
		vm.selected = [];
		vm.isSaving = false;
		vm.save = save;
		vm.toggleSelection = toggleSelection;
		vm.hasManagerRole;
		
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
        
        loadCandidates();
        
		function loadCandidates() {
			var projectId = $stateParams.id;
			
			Principal.identity().then(function(account) {
	            $scope.currentUser = account.login;
	        });
			
			UsersRightAccessService.usersByProject({
				projectId : projectId
			}, function(result){
				vm.candidates = result;
				for (var i = 0; i< vm.candidates.length; i++) {
					if (vm.candidates[i].login == $scope.currentUser) {
						vm.candidates.splice(i, 1);
					}
				}
			});
			
			UsersRightAccessService.currentUserHasManagerRole({
				projectId : projectId
			}, function(result) {
				vm.hasManagerRole = result.data;
			});
		}
		
		$scope.stateName = $state.current.name;
		if ($scope.stateName == "projectoverview.editdashboard") {
			loadInviters();
			console.log('inviters: ' + vm.dashboard.inviters);
		}
		
		function loadInviters() {
			for (var i = 0; i< vm.dashboard.users.length; i++) {
				vm.dashboard.inviters.push(String(vm.dashboard.users[i].id));
			}
			if (vm.dashboard.inviters.length == 0) {
				vm.dashboard.sharedDashboard = 'unchecked';
			} else {
				vm.dashboard.sharedDashboard = 'checked';
			}
		}
		
		function toggleSelection(inviter) {
		    var idx = vm.dashboard.inviters.indexOf(inviter);
		    // is currently selected
		    if (idx > -1) {
		    	vm.dashboard.inviters.splice(idx, 1);
		    }
		    // is newly selected
		    else {
		    	vm.dashboard.inviters.push(inviter);
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
