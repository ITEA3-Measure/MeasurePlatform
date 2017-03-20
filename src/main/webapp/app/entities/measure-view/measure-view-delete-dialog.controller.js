(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureViewDeleteController',MeasureViewDeleteController);

    MeasureViewDeleteController.$inject = ['$uibModalInstance', 'entity', 'MeasureView'];

    function MeasureViewDeleteController($uibModalInstance, entity, MeasureView) {
        var vm = this;

        vm.measureView = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MeasureView.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
