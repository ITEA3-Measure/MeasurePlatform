(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('DashboardDetailController', DashboardDetailController);

    DashboardDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dashboard', 'Phase', 'MeasureView'];

    function DashboardDetailController($scope, $rootScope, $stateParams, previousState, entity, Dashboard, Phase, MeasureView) {
        var vm = this;

        vm.dashboard = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('measurePlatformApp:dashboardUpdate', function(event, result) {
            vm.dashboard = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
