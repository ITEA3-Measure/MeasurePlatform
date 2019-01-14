(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('ApplicationController',
			ApplicationController);

	ApplicationController.$inject = [ '$scope', 'Principal', 'LoginService',
			'$state', 'Application' ];

	function ApplicationController($scope, Principal, LoginService, $state, Application) {
		var vm = this;

		vm.applications = [];



		loadAll();
		


		function loadAll() {
			Application.allapplications(function(result) {
				vm.applications = result;

			});
		}
		

//		vm.isUpload = false;
//		vm.uploadFile = uploadFile;
//
//		function uploadFile() {
//			Application.upload($scope.fileread, onUploadSuccess, onUploadError);
//		}
//
//		function onUploadSuccess(result) {
//			vm.isUpload = true;
//		}
//
//		function onUploadError() {
//			vm.isUpload = false;
//		}
		

	}
})();
