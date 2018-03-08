(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('AnalysisCard', AnalysisCard);

    AnalysisCard.$inject = ['$resource', 'DateUtils'];

    function AnalysisCard ($resource, DateUtils) {
        var resourceUrl =  'api//analysiscard/:id';

		return $resource(resourceUrl, {}, {
			'query' : {
				method : 'GET',
				isArray : true
			},
			'byprojects' : {
				url : 'api/analysiscard/byproject/:id',
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
