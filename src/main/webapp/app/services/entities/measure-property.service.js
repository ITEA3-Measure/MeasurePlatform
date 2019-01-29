(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('MeasureProperty', MeasureProperty);

    MeasureProperty.$inject = ['$resource'];

    function MeasureProperty ($resource) {
        var resourceUrl =  'api/measure-properties/:id';

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
            'update': { method:'PUT' },
            'newprop' :{
            	url:"api/measure-properties/new",
            	method: 'PUT'
            }
        });
    }
})();