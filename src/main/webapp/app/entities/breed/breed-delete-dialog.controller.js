(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('BreedDeleteController',BreedDeleteController);

    BreedDeleteController.$inject = ['$uibModalInstance', 'entity', 'Breed'];

    function BreedDeleteController($uibModalInstance, entity, Breed) {
        var vm = this;

        vm.breed = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Breed.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
