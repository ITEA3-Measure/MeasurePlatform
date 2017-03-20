(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasurePropertyDialogController', MeasurePropertyDialogController);

    MeasurePropertyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MeasureProperty', 'MeasureInstance'];

    function MeasurePropertyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MeasureProperty, MeasureInstance) {
        var vm = this;

        vm.measureProperty = entity;
        vm.clear = clear;
        vm.save = save;
        vm.measureinstances = MeasureInstance.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.measureProperty.id !== null) {
                MeasureProperty.update(vm.measureProperty, onSaveSuccess, onSaveError);
            } else {
                MeasureProperty.save(vm.measureProperty, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('measurePlatformApp:measurePropertyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
