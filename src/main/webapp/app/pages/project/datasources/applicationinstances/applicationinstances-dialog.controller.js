(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectApplicationInstancesDialogController', ProjectApplicationInstancesDialogController);

	ProjectApplicationInstancesDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'project',
			'ApplicationInstances', 'Measure', 'MeasureProperty' ];

	function ProjectApplicationInstancesDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, project, ApplicationInstances, Measure,
			MeasureProperty) {
		var vm = this;

		

		vm.applicationInstance = entity;
		vm.applicationInstance.project = project;




		
		vm.clear = clear;
		vm.save = save;





	



		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		vm.errorMessage = "";
		
		function save() {
			vm.isSaving = true;
			
			ApplicationInstances.save(vm.applicationInstance,onSaveSuccess, onSaveError);			


		}

		function onSaveSuccess(result) {
			$uibModalInstance.close(result);
			vm.isSaving = false;
		}


		function onSaveError() {
			vm.isSaving = false;
		}

	}
})();
