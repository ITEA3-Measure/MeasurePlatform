(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('ProjectApplicationDeleteController',ProjectApplicationDeleteController);

    ProjectApplicationDeleteController.$inject = ['$uibModalInstance', 'entity', 'ApplicationInstances'];

    function ProjectApplicationDeleteController($uibModalInstance, entity, ApplicationInstances) {
        var vm = this;

        vm.aplicationinstance = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete () {       	
        	ApplicationInstances.delete({id: vm.aplicationinstance.id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
