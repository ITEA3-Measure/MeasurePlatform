(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('PhaseDialogController', PhaseDialogController);

    PhaseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Phase', 'Project', 'Dashboard', 'MeasureView'];

    function PhaseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Phase, Project, Dashboard, MeasureView) {
        var vm = this;

        vm.phase = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projects = Project.query();
        vm.dashboards = Dashboard.query();
        vm.measureviews = MeasureView.query();
        vm.phases = Phase.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.phase.id !== null) {
                Phase.update(vm.phase, onSaveSuccess, onSaveError);
            } else {
                Phase.save(vm.phase, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('measurePlatformApp:phaseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
