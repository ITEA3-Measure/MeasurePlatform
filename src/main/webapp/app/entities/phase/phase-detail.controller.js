(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('PhaseDetailController', PhaseDetailController);

    PhaseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Phase', 'Project', 'Dashboard', 'MeasureView'];

    function PhaseDetailController($scope, $rootScope, $stateParams, previousState, entity, Phase, Project, Dashboard, MeasureView) {
        var vm = this;

        vm.phase = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('measurePlatformApp:phaseUpdate', function(event, result) {
            vm.phase = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
