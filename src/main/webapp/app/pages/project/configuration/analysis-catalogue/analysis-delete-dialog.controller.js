(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('AnalysisDeleteController',AnalysisDeleteController);

    AnalysisDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectAnalysis'];

    function AnalysisDeleteController($uibModalInstance, entity, ProjectAnalysis) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
        	ProjectAnalysis.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
            });
        }
    }
})();
