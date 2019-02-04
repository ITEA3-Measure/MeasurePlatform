(function() {
	'use strict';
	
	angular
		.module('measurePlatformApp')
		.factory('MeasurementService',MeasurementService);

	MeasurementService.$inject = [ '$resource' ];

	function MeasurementService($resource) {
		var resourceUrl = 'api/measurement';

		return $resource(resourceUrl, {}, {
				'lastvalue' : {
					url : 'api/measurement/last-value/:id',
					method : 'GET',
					isArray : false
				},'find' : {
					url : 'api/measurement/find',
					method : 'POST',
					isArray : true
				}
			});
	}
})();
