(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('UsersRightAccessDeleteDialogController', UsersRightAccessDeleteDialogController);

    UsersRightAccessDeleteDialogController.$inject = ['$uibModalInstance', '$stateParams', 'UsersRightAccessService'];

    function UsersRightAccessDeleteDialogController($uibModalInstance, $stateParams, UsersRightAccessService) {
        var vm = this;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete() {       	
        	UsersRightAccessService.deleteFromProject({
        			projectId: $stateParams.id,
        			userId: $stateParams.userId
        		},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
