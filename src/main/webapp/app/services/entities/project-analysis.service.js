(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('ProjectAnalysis', ProjectAnalysis);

    ProjectAnalysis.$inject = ['$resource', 'DateUtils'];

    function ProjectAnalysis ($resource, DateUtils) {
        var resourceUrl =  'api/projectanalysis/:id';

		return $resource(resourceUrl, {}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'byprojects' : {
				url : 'api/projectanalysis/byproject/:id',
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
