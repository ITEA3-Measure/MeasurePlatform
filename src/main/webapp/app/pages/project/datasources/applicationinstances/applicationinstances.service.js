(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('ApplicationInstances',
			ApplicationInstances);

	ApplicationInstances.$inject = [ '$resource' ];

	function ApplicationInstances($resource) {
		var resourceUrl = 'api/application-instances/:id';

		return $resource(resourceUrl, {}, {
			'instances' : {
				url : 'api/project-application-instances/:id',
				method : 'GET',
				isArray : true
			},
			'update' : {
				url : 'api/application-instances/:id',
				method : 'PUT'
			}
		});
	}
})();
