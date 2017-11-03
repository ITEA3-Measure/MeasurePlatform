(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectPhaseNewDialogController', ProjectPhaseNewDialogController);

	ProjectPhaseNewDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'project', 'Phase' ];

	function ProjectPhaseNewDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, project, Phase) {
		var vm = this;
		vm.project = project;
		vm.phase = entity;
		vm.clear = clear;
		vm.save = save;

		$timeout(function() {
			angular.element('.form-group:eq(1)>input').focus();
		});

		function clear() {
			$uibModalInstance.dismiss('cancel');
		}

		function save() {
			vm.isSaving = true;
			vm.phase.project = vm.project;
			Phase.save(vm.phase, onSaveSuccess, onSaveError);
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
