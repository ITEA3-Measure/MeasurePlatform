(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'Phase', 'MeasureInstance', 'User'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, Phase, MeasureInstance, User) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('measurePlatformApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
