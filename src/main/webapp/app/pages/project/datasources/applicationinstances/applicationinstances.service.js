(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('ApplicationInstances',
			ApplicationInstances);

	ApplicationInstances.$inject = [ '$resource' ];

	function ApplicationInstances($resource) {
		var resourceUrl = 'api/application-configuration/:id';

		return $resource(resourceUrl, {}, {
			'instances' : {
				url : 'api/project-application-instances/:id',
				method : 'GET',
				isArray : true
			},'getConfigurationByApplicationType' : {
				url : 'api/application-configuration/by-application-type',
				method : 'GET',
				isArray : false
			},
			'checkname' : {
				url : 'api/existing-application/:name',
				method : 'GET',
				isArray : false
			},
			'get' : {
				url : 'api/application-configuration/:id',
				method : 'GET',
				isArray : false
			},
			'save' : {
				url : 'api/application-configuration',
				method : 'POST'
			},
			'update' : {
				url : 'api/application-configuration',
				method : 'PUT'
			},
			'delete' : {
				url : 'api/application-configuration/:id',
				method : 'DELETE'
			},'getconfiguration' :{
				url : 'api/application-instance-configuration/:id',
				method : 'GET'
			},
			'startScheduling' : {
				url : 'api/application-execution/start',
				method : 'GET',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			},	
			'stopScheduling' : {
				url : 'api/application-execution/stop',
				method : 'GET',
		        transformResponse: function(data, headers) {
		            return {data:data};
		        }
			}
			
		});
	}
})();
