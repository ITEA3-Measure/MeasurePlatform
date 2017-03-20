(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('MeasureAgentService', Measure);

	Measure.$inject = [ '$resource' ];

	function Measure($resource) {
		var resourceUrl = 'api/remote-measure';

		return $resource(resourceUrl, {}, {
			'allAgents' : {
				url : 'api/remote-measure/agent-list',
				method : 'GET',
				isArray : true
			}
		});

	}
})();
