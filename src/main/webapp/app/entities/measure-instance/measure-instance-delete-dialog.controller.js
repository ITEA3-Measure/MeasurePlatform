(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureInstanceDeleteController',MeasureInstanceDeleteController);

    MeasureInstanceDeleteController.$inject = ['$uibModalInstance', 'entity', 'MeasureInstance'];

    function MeasureInstanceDeleteController($uibModalInstance, entity, MeasureInstance) {
        var vm = this;

        vm.measureInstance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MeasureInstance.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
