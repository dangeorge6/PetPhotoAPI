(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogPhotoDialogController', DogPhotoDialogController);

    DogPhotoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DogPhoto', 'Dog', 'Vote'];

    function DogPhotoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DogPhoto, Dog, Vote) {
        var vm = this;

        vm.dogPhoto = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dogs = Dog.query();
        vm.votes = Vote.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dogPhoto.id !== null) {
                DogPhoto.update(vm.dogPhoto, onSaveSuccess, onSaveError);
            } else {
                DogPhoto.save(vm.dogPhoto, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('apiChallengeApp:dogPhotoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
