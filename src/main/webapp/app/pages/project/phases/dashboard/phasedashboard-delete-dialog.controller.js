(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('PhaseDashboardDeleteController',PhaseDashboardDeleteController);

    PhaseDashboardDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dashboard'];

    function PhaseDashboardDeleteController($uibModalInstance, entity, Dashboard) {
        var vm = this;

        vm.dashboard = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dashboard.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
