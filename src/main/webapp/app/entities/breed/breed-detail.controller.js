(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('BreedDetailController', BreedDetailController);

    BreedDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Breed', 'Dog'];

    function BreedDetailController($scope, $rootScope, $stateParams, previousState, entity, Breed, Dog) {
        var vm = this;

        vm.breed = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('apiChallengeApp:breedUpdate', function(event, result) {
            vm.breed = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
