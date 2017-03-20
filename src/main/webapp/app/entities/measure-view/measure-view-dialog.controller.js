(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureViewDialogController', MeasureViewDialogController);

    MeasureViewDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MeasureView', 'Phase', 'Dashboard'];

    function MeasureViewDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MeasureView, Phase, Dashboard) {
        var vm = this;

        vm.measureView = entity;
        vm.clear = clear;
        vm.save = save;
        vm.phases = Phase.query();
        vm.dashboards = Dashboard.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.measureView.id !== null) {
                MeasureView.update(vm.measureView, onSaveSuccess, onSaveError);
            } else {
                MeasureView.save(vm.measureView, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('measurePlatformApp:measureViewUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
