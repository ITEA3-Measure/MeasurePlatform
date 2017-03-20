(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('DashboardDeleteController',DashboardDeleteController);

    DashboardDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dashboard'];

    function DashboardDeleteController($uibModalInstance, entity, Dashboard) {
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
