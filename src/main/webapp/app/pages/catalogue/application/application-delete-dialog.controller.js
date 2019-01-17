(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('ApplicationDeleteController',ApplicationDeleteController);

    ApplicationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Application'];

    function ApplicationDeleteController($uibModalInstance, entity, Application) {
        var vm = this;

        vm.application = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	Application.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
            });
        }
    }
})();
