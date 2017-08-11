(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('ConfigurationService', ConfigurationService);

	ConfigurationService.$inject = [ '$resource' ];

	function ConfigurationService($resource) {
		var resourceUrl = 'api/platform/';

		return $resource(resourceUrl, {}, {
			'kibanaadress' : {
				url : 'api/platform/configuration',
				method : 'GET',
				isArray : false
			}
		});

	}
})();
