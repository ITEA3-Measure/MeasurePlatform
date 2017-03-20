(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureInstanceDetailController', MeasureInstanceDetailController);

    MeasureInstanceDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MeasureInstance', 'Project', 'MeasureProperty'];

    function MeasureInstanceDetailController($scope, $rootScope, $stateParams, previousState, entity, MeasureInstance, Project, MeasureProperty) {
        var vm = this;

        vm.measureInstance = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('measurePlatformApp:measureInstanceUpdate', function(event, result) {
            vm.measureInstance = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
