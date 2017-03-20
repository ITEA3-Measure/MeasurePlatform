(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasurePropertyDetailController', MeasurePropertyDetailController);

    MeasurePropertyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MeasureProperty', 'MeasureInstance'];

    function MeasurePropertyDetailController($scope, $rootScope, $stateParams, previousState, entity, MeasureProperty, MeasureInstance) {
        var vm = this;

        vm.measureProperty = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('measurePlatformApp:measurePropertyUpdate', function(event, result) {
            vm.measureProperty = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
