(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('AnalysisServicesConfigurationDialogController', AnalysisServicesConfigurationDialogController);

    AnalysisServicesConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity'];

    function AnalysisServicesConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity) {
        var vm = this;
        vm.analysis = entity;
        vm.frame = "<iframe src=\""+vm.analysis.configurationURL+"\" style=\"border:none;margin-right:10px;\" width=\"100%\" height=\"600\"></iframe>"   
        
        vm.clear = clear;
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
    }
})();
