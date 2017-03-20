(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'PhaseDashboardDialogController', PhaseDashboardDialogController);

	PhaseDashboardDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity', 'phase', 'Dashboard' ];

	function PhaseDashboardDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity, phase, Dashboard) {
		var vm = this;
		vm.dashboard = entity;
		vm.phase = phase;
		vm.isSaving = false;
		vm.save=save;
		function save() {
			vm.isSaving = true;
			if (vm.dashboard.id != null) {
				Dashboard.update(vm.dashboard, onSaveSuccess, onSaveError);
			} else {
				vm.dashboard.phase = vm.phase;
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
