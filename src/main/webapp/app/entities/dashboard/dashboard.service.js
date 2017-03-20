(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('Dashboard', Dashboard);

	Dashboard.$inject = [ '$resource' ];

	function Dashboard($resource) {
		var resourceUrl = 'api/dashboards/:id';

		return $resource(resourceUrl, {}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'dashboards' : {
				url : 'api/dashboards/byphase/:id',
				method : 'GET',
				isArray : true
			},
			'get' : {
				method : 'GET',
				transformResponse : function(data) {
					if (data) {
						data = angular.fromJson(data);
					}
					return data;
				}
			},
			'update' : {
				method : 'PUT'
			}
		});
	}
})();
