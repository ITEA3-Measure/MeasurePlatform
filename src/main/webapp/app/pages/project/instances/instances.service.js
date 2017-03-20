(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('ProjectInstances',
			ProjectInstances);

	ProjectInstances.$inject = [ '$resource' ];

	function ProjectInstances($resource) {
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
			}
		});
	}
})();
