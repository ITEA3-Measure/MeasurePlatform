(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('MeasureController',
			MeasureController);

	MeasureController.$inject = [ '$scope', 'Principal', 'LoginService',
			'$state', 'Measure' ];

	function MeasureController($scope, Principal, LoginService, $state, Measure) {
		var vm = this;

		vm.measures = [];

		loadAll();

		function loadAll() {
			Measure.allmeasures(function(result) {
				vm.measures = result;
			});
		}

		vm.isUpload = false;
		vm.uploadFile = uploadFile;

		function uploadFile() {
			Measure.upload($scope.fileread, onUploadSuccess, onUploadError);
		}

		function onUploadSuccess(result) {
			vm.isUpload = true;
		}

		function onUploadError() {
			vm.isUpload = false;
		}
	}
})();
