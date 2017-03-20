(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('Catalogue', Catalogue);

    Catalogue.$inject = ['$resource'];

    function Catalogue ($resource) {	
        var resourceUrl =  'api/catalogue';

        return $resource(resourceUrl, {}, {
            'projects': {
            	 url:'api/ownerProjects',
            	 method: 'GET',
             	 isArray: true
             }
        });
    }
})();
