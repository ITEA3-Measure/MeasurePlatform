(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'TestDataSourceDialogController', TestDataSourceDialogController);

	TestDataSourceDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'entity','ProjectDataSources','isTest'];

	function TestDataSourceDialogController($timeout, $scope, $stateParams,
			$uibModalInstance, entity,ProjectDataSources,isTest) {
		var vm = this;

		vm.close = close;
		
		vm.measureInstance = entity;
		vm.test = isTest;

		if(isTest){
			testMeasure(vm.measureInstance.id);
		}else {
			executeMeasure(vm.measureInstance.id);
		}

		vm.testResult = null;

		function testMeasure(id) {
			ProjectDataSources.testMeasure({
				id : id
			}, function(result) {
				vm.testResult = result;
			});
		}
		
		function executeMeasure(id) {
			ProjectDataSources.executeMeasure({
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
