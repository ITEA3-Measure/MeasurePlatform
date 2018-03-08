(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'AddAnalysisToolDialogController', AddAnalysisToolDialogController);

	AddAnalysisToolDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'Project',
			'AnalysisServicesService' ,'ProjectAnalysis'];

	function AddAnalysisToolDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, Project, AnalysisServicesService,ProjectAnalysis) {
		var vm = this;
	
		vm.project = entity;
		vm.services = [];


		loadAllServices();
		function loadAllServices() {
			AnalysisServicesService.allServices(function(result) {
				vm.services = result;
				for (var i = 0; i < vm.services.length; i++) {	
					vm.services[i].isselected = false;
				}
				
			});
		}
		
		vm.addService = addService;
		function addService(service) {
			vm.isSaving = true;
			var analysis = new Object();
			analysis.analysisToolId = service.name;
			analysis.project = vm.project;
			analysis.analysisToolDescription = service.description;		
			ProjectAnalysis.save(analysis, onSaveSuccess, onSaveError);	
		}

		function onSaveSuccess(result) {
			$uibModalInstance.close(result);
			vm.isSaving = false;		
		}

		function onSaveError() {
			vm.isSaving = false;
		}

		vm.clear = clear;
		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		

	}
})();
