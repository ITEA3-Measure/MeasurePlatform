(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('DashboardController', DashboardController);

    DashboardController.$inject = ['$scope', '$state', 'Dashboard'];

    function DashboardController ($scope, $state, Dashboard) {
        var vm = this;
        
        vm.dashboards = [];

        loadAll();

        function loadAll() {
            Dashboard.query(function(result) {
                vm.dashboards = result;
            });
        }
    }
})();
