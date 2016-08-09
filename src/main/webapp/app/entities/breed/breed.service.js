(function() {
    'use strict';
    angular
        .module('apiChallengeApp')
        .factory('Breed', Breed);

    Breed.$inject = ['$resource'];

    function Breed ($resource) {
        var resourceUrl =  'api/breeds/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
