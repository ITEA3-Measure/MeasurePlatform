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
				url : 'api/dashboardDTOs/byproject/:id',
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
