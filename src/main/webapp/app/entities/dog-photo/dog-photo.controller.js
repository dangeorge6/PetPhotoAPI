(function() {
    'use strict';

    angular
        .module('apiChallengeApp')
        .controller('DogPhotoController', DogPhotoController);

    DogPhotoController.$inject = ['$scope', '$state', 'DogPhoto'];

    function DogPhotoController ($scope, $state, DogPhoto) {
        var vm = this;
        
        vm.dogPhotos = [];

        loadAll();

        function loadAll() {
            DogPhoto.query(function(result) {
                vm.dogPhotos = result;
            });
        }
    }
})();
