(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'ProjectPhaseDialogController', ProjectPhaseDialogController);

	ProjectPhaseDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'Phase','Dashboard' ];

	function ProjectPhaseDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, Phase,Dashboard) {
		var vm = this;
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
			Phase.update(vm.phase, onSaveSuccess, onSaveError);
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
