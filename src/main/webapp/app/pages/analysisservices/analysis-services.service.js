(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('AnalysisServicesService', AnalysisServicesService);

	AnalysisServicesService.$inject = [ '$resource' ];

	function AnalysisServicesService($resource) {
		var resourceUrl = 'api/analysis';

		return $resource(resourceUrl, {}, {
			'allServices' : {
				url : 'api/analysis/list',
				method : 'GET',
				isArray : true
			},
			'findByName' : {
				url : 'api/analysis/list/:id',
				method : 'GET',
				isArray : false
			}
		});

	}
})();
