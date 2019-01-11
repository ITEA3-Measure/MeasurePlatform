(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('Application', Application);

	Application.$inject = [ '$resource' ];

	function Application($resource) {
		var resourceUrl = 'api/applications/:id';

		return $resource(resourceUrl, {}, {
			'allapplications' : {
				url : 'api/applications/findall',
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
