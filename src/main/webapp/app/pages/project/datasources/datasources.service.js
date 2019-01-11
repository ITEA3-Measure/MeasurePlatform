(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('ProjectDataSources',
			ProjectDataSources);

	ProjectDataSources.$inject = [ '$resource' ];

	function ProjectDataSources($resource) {
		var resourceUrl = 'api/measure-instances/:id';

		return $resource(resourceUrl, {}, {
			'instances' : {
				url : 'api/project-measure-instances/:id',
				method : 'GET',
				isArray : true
			},
			'properties' : {
				url : 'api/measure-properties/byinstance/:id',
				method : 'GET',
				isArray : true
			},'instancesofmeasure' : {
				url : 'api/measure-instances/bymeasure/:measureRef',
				method : 'GET',
				isArray : true
			},'references' : {
				url : 'api/measure-references/byinstance/:id',
				method : 'GET',
				isArray : true
			},'checkname' : {
				url : 'api/existing-measure/:name',
				method : 'GET',
				isArray : false
			},
			'update' : {
				url : 'api/measure-instances/:id',
				method : 'PUT'
			},'savereference' : {
				url : 'api/measure-references/',
				method : 'POST'
			},'updatereference' : {
				url : 'api/measure-references/',
				method : 'PUT'
			},'deletereference' : {
				url : 'api/measure-references/:id',
				method : 'DELETE'
			},
			'startSheduling' : {
				url : 'api/measure-instance/sheduling/start',
				method : 'GET',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			},
			'stopSheduling' : {
				url : 'api/measure-instance/sheduling/stop',
				method : 'GET',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			},
			'isShedule' : {
				url : 'api/measure-instance/sheduling/isshedule',
				method : 'GET',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			},
			'testMeasure' : {
				url : 'api/measure-instance/sheduling/test',
				method : 'GET'
			},
			'executeMeasure' : {
				url : 'api/measure-instance/sheduling/execute',
				method : 'GET'
			},
			'createDefaultVisualisation' : {
				url : 'api/measure-visualisation/create-default',
				method : 'GET',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			},
			'deleteDefaultVisualisation' : {
				url : 'api/measure-visualisation/delete-default',
				method : 'DELETE',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			},
			'getDefaultVisualisation' : {
				url : 'api/measure-visualisation/get-default',
				method : 'GET',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			}
		});
	}
})();
