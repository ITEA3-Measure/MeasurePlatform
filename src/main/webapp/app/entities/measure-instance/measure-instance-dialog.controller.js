(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureInstanceDialogController', MeasureInstanceDialogController);

    MeasureInstanceDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MeasureInstance', 'Project', 'MeasureProperty'];

    function MeasureInstanceDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MeasureInstance, Project, MeasureProperty) {
        var vm = this;

        vm.measureInstance = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projects = Project.query();
        vm.measureproperties = MeasureProperty.query();
        vm.measureinstances = MeasureInstance.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.measureInstance.id !== null) {
                MeasureInstance.update(vm.measureInstance, onSaveSuccess, onSaveError);
            } else {
                MeasureInstance.save(vm.measureInstance, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('measurePlatformApp:measureInstanceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
