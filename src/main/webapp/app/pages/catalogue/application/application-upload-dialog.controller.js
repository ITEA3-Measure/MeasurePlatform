(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('ApplicationUploadController',
			ApplicationUploadController);

	ApplicationUploadController.$inject = ['$location', '$http','$scope', '$uibModalInstance' ];

	function ApplicationUploadController($location,$http,$scope, $uibModalInstance) {
		var vm = this;
		vm.isUpload = false;
		vm.uploadFile = uploadFile;
		vm.uploadStatus;


		
		vm.uploadFile = uploadFile;
		function uploadFile() {		
			var absUrl = $location.absUrl();
			var url = $location.url();
			var basURL = absUrl.replace("/#" + url, "/api/application/upload");
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
