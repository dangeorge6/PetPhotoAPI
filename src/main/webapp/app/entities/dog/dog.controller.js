(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogController', DogController);

    DogController.$inject = ['$scope', '$state', 'Dog'];

    function DogController ($scope, $state, Dog) {
        var vm = this;
        
        vm.dogs = [];

        loadAll();

        function loadAll() {
            Dog.query(function(result) {
                vm.dogs = result;
            });
        }
    }
})();
