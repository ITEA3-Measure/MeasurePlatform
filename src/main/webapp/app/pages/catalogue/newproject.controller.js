(function() {
    'use strict';

    angular
        .module('measurePlatformApp')
        .controller('NewProjectDialogController', NewProjectDialogController);

    NewProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Project', 'User','Principal'];

    function NewProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Project,User,Principal) {
        var vm = this;

        vm.project = entity;
        vm.clear = clear;
        vm.save = save;       
        vm.account = null;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.project.id !== null) {	
            
            	Project.update(vm.project, onSaveSuccess, onSaveError);
            } else {
            	for (var i = 0; i < vm.users.length; i++) {
            		if(vm.account.login == vm.users[i].login){
            			vm.project.owner = vm.users[i];
            		}    	
            	}
            	Project.save(vm.project, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('measurePlatformApp:projectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        
        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }


    }
})();
