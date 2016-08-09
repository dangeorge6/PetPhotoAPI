(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogPhotoDetailController', DogPhotoDetailController);

    DogPhotoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DogPhoto', 'Dog', 'Vote'];

    function DogPhotoDetailController($scope, $rootScope, $stateParams, previousState, entity, DogPhoto, Dog, Vote) {
        var vm = this;

        vm.dogPhoto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('apiChallengeApp:dogPhotoUpdate', function(event, result) {
            vm.dogPhoto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
