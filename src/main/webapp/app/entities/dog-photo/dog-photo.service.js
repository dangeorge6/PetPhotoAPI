(function() {
    'use strict';
    angular
        .module('apiChallengeApp')
        .factory('DogPhoto', DogPhoto);

    DogPhoto.$inject = ['$resource'];

    function DogPhoto ($resource) {
        var resourceUrl =  'api/dog-photos/:id';

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
