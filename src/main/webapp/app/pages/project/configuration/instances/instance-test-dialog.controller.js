(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'TestInstanceDialogController', TestInstanceDialogController);

	TestInstanceDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity','ProjectInstances','isTest'];

	function TestInstanceDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity,ProjectInstances,isTest) {
		var vm = this;

		vm.close = close;
		
		vm.measureInstance = entity;

		if(isTest){
			testMeasure(vm.measureInstance.id);
		}else {
			executeMeasure(vm.measureInstance.id);
		}

		vm.testResult = null;

		function testMeasure(id) {
			ProjectInstances.testMeasure({
				id : id
			}, function(result) {
				vm.testResult = result;
			});
		}
		
		function executeMeasure(id) {
			ProjectInstances.executeMeasure({
				id : id
			}, function(result) {
				vm.testResult = result;
			});
		}
		
		function close() {
			$uibModalInstance.dismiss('cancel');
		}

	}
})();
