(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('AppProjectDeleteController',AppProjectDeleteController);

    AppProjectDeleteController.$inject = ['$uibModalInstance', 'entity', 'Project'];

    function AppProjectDeleteController($uibModalInstance, entity, Project) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Project.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
            });
        }
    }
})();
