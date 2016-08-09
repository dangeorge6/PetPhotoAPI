(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogDialogController', DogDialogController);

    DogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dog', 'Breed', 'DogPhoto'];

    function DogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dog, Breed, DogPhoto) {
        var vm = this;

        vm.dog = entity;
        vm.clear = clear;
        vm.save = save;
        vm.breeds = Breed.query();
        vm.dogphotos = DogPhoto.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dog.id !== null) {
                Dog.update(vm.dog, onSaveSuccess, onSaveError);
            } else {
                Dog.save(vm.dog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('apiChallengeApp:dogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
