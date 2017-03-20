(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureViewController', MeasureViewController);

    MeasureViewController.$inject = ['$scope', '$state', 'MeasureView'];

    function MeasureViewController ($scope, $state, MeasureView) {
        var vm = this;
        
        vm.measureViews = [];

        loadAll();

        function loadAll() {
            MeasureView.query(function(result) {
                vm.measureViews = result;
            });
        }
    }
})();
