(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasurePropertyDeleteController',MeasurePropertyDeleteController);

    MeasurePropertyDeleteController.$inject = ['$uibModalInstance', 'entity', 'MeasureProperty'];

    function MeasurePropertyDeleteController($uibModalInstance, entity, MeasureProperty) {
        var vm = this;

        vm.measureProperty = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MeasureProperty.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
