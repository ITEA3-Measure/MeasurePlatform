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
			},'getApplicationConfigurationByApplicationType' : {
				url : 'api/application/configuration/:id',
				method : 'GET'
			},
			'checkname' : {
				url : 'api/existing-application/:name',
				method : 'GET',
				isArray : false
			},
			'save' : {
				url : 'api/application-instance-configuration',
				method : 'POST'
			},
			'update' : {
				url : 'api/application-instance-configuration',
				method : 'PUT'
			}

//			'startSheduling' : {
//				url : 'api/measure-instance/sheduling/start',
//				method : 'GET',
//		        transformResponse: function(data, headers) {
//		            return {data:data};
//		        }
//			},
//			'stopSheduling' : {
//				url : 'api/measure-instance/sheduling/stop',
//				method : 'GET',
//		        transformResponse: function(data, headers) {
//		            return {data:data};
//		        }
//			},
//			'isShedule' : {
//				url : 'api/measure-instance/sheduling/isshedule',
//				method : 'GET',
//		        transformResponse: function(data, headers) {
//		            return {data:data};
//		        }
//			},
//			'testMeasure' : {
//				url : 'api/measure-instance/sheduling/test',
//				method : 'GET'
//			},
//			'executeMeasure' : {
//				url : 'api/measure-instance/sheduling/execute',
//				method : 'GET'
//			},
//			'createDefaultVisualisation' : {
//				url : 'api/measure-visualisation/create-default',
//				method : 'GET',
//		        transformResponse: function(data, headers) {
//		            return {data:data};
//		        }
//			},
//			'deleteDefaultVisualisation' : {
//				url : 'api/measure-visualisation/delete-default',
//				method : 'DELETE',
//		        transformResponse: function(data, headers) {
//		            return {data:data};
//		        }
//			},
//			'getDefaultVisualisation' : {
//				url : 'api/measure-visualisation/get-default',
//				method : 'GET',
//		        transformResponse: function(data, headers) {
//		            return {data:data};
//		        }
//			}
		});
	}
})();
