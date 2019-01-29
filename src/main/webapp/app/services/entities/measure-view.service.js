(function() {
    'use strict';
    angular
        .module('measurePlatformApp')
        .factory('MeasureView', MeasureView);

    MeasureView.$inject = ['$resource'];

    function MeasureView ($resource) {
        var resourceUrl =  'api/measure-views/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'byproject': {url:'api/measureview/byproject/:id',
            	method: 'GET',
            	isArray: true
            },
            'byprojectoverview': {url:'api/measureview/byprojectoverview/:id',
            	method: 'GET',
            	isArray: true
            },
            'byphase': {url:'api/measureview/byphase/:id',
            	method: 'GET',
            	isArray: true
            },
            'byphaseoverview': {url:'api/measureview/byphaseoverview/:id',
            	method: 'GET',
            	isArray: true
            },
            'bydashboard': {url:'api/measureview/bydashboard/:id',
            	method: 'GET',
            	isArray: true
            },
            'allkibanavisualisations': {url:'api/measureview/kibana-visualisations',
            	method: 'GET',
            	isArray: true
            },
            'allkibanadashboards': {url:'api/measureview/kibana-dashboards',
            	method: 'GET',
            	isArray: true
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
