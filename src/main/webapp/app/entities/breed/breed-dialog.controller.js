(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('BreedDialogController', BreedDialogController);

    BreedDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Breed', 'Dog'];

    function BreedDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Breed, Dog) {
        var vm = this;

        vm.breed = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dogs = Dog.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.breed.id !== null) {
                Breed.update(vm.breed, onSaveSuccess, onSaveError);
            } else {
                Breed.save(vm.breed, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('apiChallengeApp:breedUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
