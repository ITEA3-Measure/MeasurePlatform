(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('ProjectPhaseDeleteController',ProjectPhaseDeleteController);

    ProjectPhaseDeleteController.$inject = ['$uibModalInstance', 'entity', 'Phase'];

    function ProjectPhaseDeleteController($uibModalInstance, entity, Phase) {
        var vm = this;

        vm.phase = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Phase.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
