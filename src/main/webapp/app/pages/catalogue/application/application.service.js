(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('Application', Application);

	Application.$inject = [ '$resource' ];

	function Application($resource) {
		var resourceUrl = 'api/application/:id';

		return $resource(resourceUrl, {}, {
			'allapplications' : {
				url : 'api/application/findall',
				method : 'GET',
				isArray : true
			},
			'upload' : {
				url : 'api/applications/upload',
				method : 'PUT'
			}
		});

	}
})();
