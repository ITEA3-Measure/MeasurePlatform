(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('Measure', Measure);

	Measure.$inject = [ '$resource' ];

	function Measure($resource) {
		var resourceUrl = 'api/measure/:id';

		return $resource(resourceUrl, {}, {
			'allmeasures' : {
				url : 'api/measure/findall',
				method : 'GET',
				isArray : true
			},
			'upload' : {
				url : 'api/measure/upload',
				method : 'PUT'
			}
		});

	}
})();
