(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('BreedController', BreedController);

    BreedController.$inject = ['$scope', '$state', 'Breed'];

    function BreedController ($scope, $state, Breed) {
        var vm = this;
        
        vm.breeds = [];

        loadAll();

        function loadAll() {
            Breed.query(function(result) {
                vm.breeds = result;
            });
        }
    }
})();
