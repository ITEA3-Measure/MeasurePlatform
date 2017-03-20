(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('DashboardDialogController', DashboardDialogController);

    DashboardDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dashboard', 'Phase', 'MeasureView'];

    function DashboardDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dashboard, Phase, MeasureView) {
        var vm = this;

        vm.dashboard = entity;
        vm.clear = clear;
        vm.save = save;
        vm.phases = Phase.query();
        vm.measureviews = MeasureView.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dashboard.id !== null) {
                Dashboard.update(vm.dashboard, onSaveSuccess, onSaveError);
            } else {
                Dashboard.save(vm.dashboard, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('measurePlatformApp:dashboardUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
