(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('Notification', Notification);

    Notification.$inject = ['$resource'];

    function Notification ($resource) {
        var resourceUrl =  'api/notifications/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'notifications' : {
				url : 'api/notifications/byproject/:id',
				method : 'GET',
				isArray : true
			},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
