(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogDetailController', DogDetailController);

    DogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dog', 'Breed', 'DogPhoto'];

    function DogDetailController($scope, $rootScope, $stateParams, previousState, entity, Dog, Breed, DogPhoto) {
        var vm = this;

        vm.dog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('apiChallengeApp:dogUpdate', function(event, result) {
            vm.dog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
