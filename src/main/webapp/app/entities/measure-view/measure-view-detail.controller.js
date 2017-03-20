(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureViewDetailController', MeasureViewDetailController);

    MeasureViewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MeasureView', 'Phase', 'Dashboard'];

    function MeasureViewDetailController($scope, $rootScope, $stateParams, previousState, entity, MeasureView, Phase, Dashboard) {
        var vm = this;

        vm.measureView = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('measurePlatformApp:measureViewUpdate', function(event, result) {
            vm.measureView = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
