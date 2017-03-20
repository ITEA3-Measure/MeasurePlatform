(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureInstanceController', MeasureInstanceController);

    MeasureInstanceController.$inject = ['$scope', '$state', 'MeasureInstance'];

    function MeasureInstanceController ($scope, $state, MeasureInstance) {
        var vm = this;
        
        vm.measureInstances = [];

        loadAll();

        function loadAll() {
            MeasureInstance.query(function(result) {
                vm.measureInstances = result;
            });
        }
    }
})();
