(function() {
	'use strict';

	angular.module('measurePlatformApp').controller('MeasureAgent',
			MeasureAgent);

	MeasureAgent.$inject = [ '$scope', 'Principal', 'MeasureAgentService',
			'$state' ];

	function MeasureAgent($scope, Principal, MeasureAgentService, $state) {
		var vm = this;

		vm.agents = [];

		loadAll();
			
		function loadAll() {
			MeasureAgentService.allAgents(function(result) {
				vm.agents = result;
			});
		}
	}
})();
