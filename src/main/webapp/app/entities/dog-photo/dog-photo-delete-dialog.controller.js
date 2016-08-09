(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogPhotoDeleteController',DogPhotoDeleteController);

    DogPhotoDeleteController.$inject = ['$uibModalInstance', 'entity', 'DogPhoto'];

    function DogPhotoDeleteController($uibModalInstance, entity, DogPhoto) {
        var vm = this;

        vm.dogPhoto = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DogPhoto.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
