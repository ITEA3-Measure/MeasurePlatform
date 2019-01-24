(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('MeasureUploadController',
			MeasureUploadController);

	MeasureUploadController.$inject = ['$location', '$http','$scope', '$uibModalInstance',
			'Measure' ];

	function MeasureUploadController($location,$http,$scope, $uibModalInstance, Measure) {
		var vm = this;
		vm.isUpload = false;
		vm.uploadFile = uploadFile;
		vm.uploadStatus;

		function uploadFile() {
			vm.isUpload = true;
			Measure.upload($scope.fileread, onUploadSuccess, onUploadError).$promise;
		}
		
		vm.uploadFile2 = uploadFile2;
		function uploadFile2() {
			
			var absUrl = $location.absUrl();
			var url = $location.url();
			var basURL = absUrl.replace("/#" + url, "/api/measure/upload");
			uploadFileToUrl($scope.myFile, basURL);
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
		
		

		function uploadFileToUrl(file, uploadUrl){
	        var fd = new FormData();
	        fd.append('file', file);
	        $http.post(uploadUrl, fd, {
	            transformRequest: angular.identity,
	            headers: {'Content-Type': undefined}
	        })
	        .success(function(){
	        	vm.isUpload = false;
				$uibModalInstance.close(true);
	        })
	        .error(function(){
	        	vm.isUpload = false;
				$uibModalInstance.close(true);
	        });
		}
	}
})();
