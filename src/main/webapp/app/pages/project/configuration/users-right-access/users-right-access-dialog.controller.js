(function() {
	'use strict';

	angular.module('measurePlatformApp').controller(
			'UsersRightAccessDialogController', UsersRightAccessDialogController);

	UsersRightAccessDialogController.$inject = [ '$timeout', '$scope',
			'$stateParams', '$uibModalInstance', 'project','UsersRightAccessService'];

	function UsersRightAccessDialogController($timeout, $scope, $stateParams,$uibModalInstance, project, UsersRightAccessService) {
		var vm = this;
		vm.candidates = [];
		vm.save = save;
		vm.selectCand = selectCand;
		vm.selectRole = selectRole;
		
		loadCandidates($stateParams.id)
		function loadCandidates(projectId) {
			UsersRightAccessService.candidatesToProject({
				projectId : projectId
			}, function(result){
				vm.candidates = result;
			})
		}
		
		function save() {
			vm.isSaving = true;
			UsersRightAccessService.inviteToProject({
    			projectId: $stateParams.id,
    			userId: $scope.candId,
    			role: $scope.role
    		},
            function () {
                $uibModalInstance.close(true);
            });
		}
		
		function selectCand(candId){
			$scope.candId = candId;
		}
		
		function selectRole(role) {
			$scope.role = role;
		}
		
		function onSaveSuccess(result) {
			$uibModalInstance.close(result);
			vm.isSaving = false;		
		}

		function onSaveError() {
			vm.isSaving = false;
		}

		vm.clear = clear;
		function clear() {
			$uibModalInstance.dismiss('cancel');
		}
		

	}
})();