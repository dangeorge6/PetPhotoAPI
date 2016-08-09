(function() {
    'use strict';
    angular
        .module('apiChallengeApp')
        .factory('Dog', Dog);

    Dog.$inject = ['$resource'];

    function Dog ($resource) {
        var resourceUrl =  'api/dogs/:id';

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
