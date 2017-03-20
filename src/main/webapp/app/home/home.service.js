(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('Home', Home);

    Home.$inject = ['$resource'];

    function Home ($resource) {	
        var resourceUrl =  'api/home';

        return $resource(resourceUrl, {}, {
            'projects': {
            	 url:'api/ownerProjects',
            	 method: 'GET',
             	 isArray: true
             }
        });
    }
})();
