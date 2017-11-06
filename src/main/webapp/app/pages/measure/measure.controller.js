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
				for (var j = 0; j < vm.measures.length; j++) {
					vm.measures[j].showUnites  = false;
					vm.measures[j].showDependencies = false;
					vm.measures[j].showProperties = false;
				}
			});
		}
		
		
		
		vm.showProperties = showProperties
		function showProperties(id, show) {
			for (var j = 0; j < vm.measures.length; j++) {
				if (vm.measures[j].id == id) {
					vm.measures[j].showProperties = show;
				}
			}
		}
		
		vm.showUnites = showUnites
		function showUnites(id, show) {
			for (var j = 0; j < vm.measures.length; j++) {
				if (vm.measures[j].id == id) {
					vm.measures[j].showUnites = show;
				}
			}
		}
		
		vm.showDependencies = showDependencies
		function showDependencies(id, show) {
			for (var j = 0; j < vm.measures.length; j++) {
				if (vm.measures[j].id == id) {
					vm.measures[j].showDependencies = show;
				}
			}
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
