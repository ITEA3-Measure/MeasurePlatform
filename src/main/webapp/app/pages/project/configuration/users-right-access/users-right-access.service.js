(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('UsersRightAccessService', UsersRightAccessService);

	UsersRightAccessService.$inject = [ '$resource'];

	function UsersRightAccessService($resource) {
		var resourceUrl = 'api/projets/:id';

		return $resource(resourceUrl, {}, {
			'inviteToProject' : {
				url : 'api/projects/invite-to-project',
				method : 'PUT'
			},'transformRole' : {
				url : 'api/projects/transform-user-role',
				method : 'PUT'
			},'usersByProject' : {
				url : 'api/projects/:projectId/users',
				method : 'GET',
				isArray : true
			},'candidatesToProject' : {
				url : 'api/projects/:projectId/candidate-users',
				method : 'GET',
				isArray : true
			},'deleteFromProject' : {
				url : 'api/projects/remove-from-project',
				method : 'PUT'
			},'currentUserHasManagerRole' : {
				url : 'api/projects/:projectId/current-user-manager-role',
				method : 'GET',
				transformResponse: function(data, headers) {
		            return {data:data};
		        }
			}
		});
	}
})();
