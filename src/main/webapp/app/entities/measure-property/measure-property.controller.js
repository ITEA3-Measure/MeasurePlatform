(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasurePropertyController', MeasurePropertyController);

    MeasurePropertyController.$inject = ['$scope', '$state', 'MeasureProperty'];

    function MeasurePropertyController ($scope, $state, MeasureProperty) {
        var vm = this;
        
        vm.measureProperties = [];

        loadAll();

        function loadAll() {
            MeasureProperty.query(function(result) {
                vm.measureProperties = result;
            });
        }
    }
})();
