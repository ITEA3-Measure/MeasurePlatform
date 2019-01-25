(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('MeasureDeleteController',MeasureDeleteController);

    MeasureDeleteController.$inject = ['$uibModalInstance', 'entity', 'Measure'];

    function MeasureDeleteController($uibModalInstance, entity, Measure) {
        var vm = this;

        vm.measure = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	Measure.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
            });
        }
    }
})();
