(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('MeasureInstance', MeasureInstance);

    MeasureInstance.$inject = ['$resource'];

    function MeasureInstance ($resource) {
        var resourceUrl =  'api/measure-instances/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
