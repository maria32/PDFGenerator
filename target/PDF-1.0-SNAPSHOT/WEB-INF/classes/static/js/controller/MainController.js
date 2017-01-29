'use strict';

angular.module('PDF')
    .controller('MainController', function ($scope, $http, $routeParams, $location) {

        $scope.upload = function() {
            console.log("Main HAHAHA This is the file:" + $scope.file);
            $http({
                method: 'POST',
                url: '/upload',
                headers: {'Content-Type': 'application/json'},
                data: {
                    file: $scope.file
                }

            }).then(function successCallback(response) {

                if (response.data != "") {

                    //$location.path('/')

                } else {
                    console.log("AUTHENTICATION ERROR!");
                }
            });
        };
    });