(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('AnalysisServices',
			AnalysisServices);

	AnalysisServices.$inject = [ '$scope', 'Principal', 'AnalysisServicesService',
			'$state' ];

	function AnalysisServices($scope, Principal, AnalysisServicesService, $state) {
		var vm = this;

		vm.agents = [];

		loadAll();
			
		function loadAll() {
			AnalysisServicesService.allServices(function(result) {
				vm.services = result;
			});
		}
	}
})();
