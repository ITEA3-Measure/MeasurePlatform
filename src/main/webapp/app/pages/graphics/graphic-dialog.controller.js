(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('GraphicDialogController', GraphicDialogController);

    GraphicDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance','entity','project', 'phase', 'dashboard','data', 'MeasureView','ProjectInstances'];

    function GraphicDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity,project, phase,dashboard,data,MeasureView,ProjectInstances) {
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

		function changemode() {
			if (vm.measureView.custom) {
				vm.measureView.custom = false;
			} else {
				vm.measureView.custom = true;
			}
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
		}

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        vm.clear = clear;       
        function clear () {
            $uibModalInstance.dismiss('cancel');
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
