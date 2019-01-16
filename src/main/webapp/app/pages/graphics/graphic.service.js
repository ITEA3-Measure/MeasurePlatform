(function() {
	'use strict';
	angular.module('measurePlatformApp').factory('GraphicService',
			GraphicService);

	GraphicService.$inject = [ '$resource' ];

	function GraphicService($resource) {

		var resourceUrl = 'api/measure-instances/:id';

		return $resource(resourceUrl, {}, {
			'instances' : {
				url : 'api/project-measure-instances/:id',
				method : 'GET',
				isArray : true
			}
		});
	}
})();
