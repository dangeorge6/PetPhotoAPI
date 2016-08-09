(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogDeleteController',DogDeleteController);

    DogDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dog'];

    function DogDeleteController($uibModalInstance, entity, Dog) {
        var vm = this;

        vm.dog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
