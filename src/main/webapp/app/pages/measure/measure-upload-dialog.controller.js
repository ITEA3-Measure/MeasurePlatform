(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('MeasureUploadController',
			MeasureUploadController);

	MeasureUploadController.$inject = [ '$scope', '$uibModalInstance',
			'Measure' ];

	function MeasureUploadController($scope, $uibModalInstance, Measure) {
		var vm = this;
		vm.isUpload = false;
		vm.uploadFile = uploadFile;
		vm.uploadStatus;

		function uploadFile() {
			vm.isUpload = true;
			Measure.upload($scope.fileread, onUploadSuccess, onUploadError).$promise;
		}

		function onUploadSuccess(result) {
			vm.isUpload = false;
			$uibModalInstance.close(true);
		}

		function onUploadError() {
			vm.isUpload = false;
			$uibModalInstance.close(true);
		}

		vm.clear = clear;
		function clear() {
			$uibModalInstance.dismiss('cancel');
		}
	}
})();
