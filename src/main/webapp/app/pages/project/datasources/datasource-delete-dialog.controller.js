(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('ProjectDataSourceDeleteController',ProjectDataSourceDeleteController);

    ProjectDataSourceDeleteController.$inject = ['$uibModalInstance', 'entity', 'MeasureInstance', 'MeasureProperty',];

    function ProjectDataSourceDeleteController($uibModalInstance, entity, MeasureInstance, MeasureProperty) {
        var vm = this;

        vm.measureInstance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (measureInstance) {       	
            MeasureInstance.delete({id: measureInstance.id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
