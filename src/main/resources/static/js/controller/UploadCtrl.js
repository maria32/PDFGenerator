/**
 * Created by martanase on 12/11/2016.
 */

//inject angular file upload directives and services.
// var app = angular.module('fileUpload', ['ngFileUpload']);
angular.module('PDF')
    .controller('UploadCtrl', function ($scope, $rootScope, $sessionStorage, $location, $http, Upload, $timeout, NotificationService) {

        if (getCookie("remember-me") == "") {
            $rootScope.user = undefined;
            $location.path("/login");
        } else {
            $http({
                method: 'GET',
                url: '/user/' + getCookie("remember-me"),
                headers: {'Content-Type': 'application/json'}
            }).then(function successCallback(response) {
                if (response.data != null && response.data != "") {
                    $rootScope.user = response.data;
                    $sessionStorage.user = response.data;
                } else {
                    $rootScope.user = undefined;
                    $location.path("/login");
                }
            });
        }

        function getCookie(cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for(var i = 0; i < ca.length; i++) {
                var c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        };


        $scope.$watch('files', function () {
            $scope.upload($scope.files);
        });
        $scope.$watch('file', function () {
            if ($scope.file != null) {
                $scope.files = [$scope.file];
            }
        });
        $scope.log = '';

        $scope.upload = function (files) {
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    if(file.name.search(" ") != -1) {
                        file.name = file.name.replace(" ", "_");
                    }
                    if (!file.$error) {
                        if (file.size < 5242880) {
                            Upload.upload({
                                method: 'POST',
                                url: '/upload/',
                                data: {
                                    file: file
                                }
                            }).then(function (resp) {
                                $timeout(function () {
                                    $scope.log = 'Uploaded file: ' +
                                        resp.config.data.file.name +
                                        // ', Response: ' + JSON.stringify(resp.data) +
                                        '\n' + $scope.log;
                                });
                            }, null, function (evt) {
                                var progressPercentage = parseInt(100.0 *
                                    evt.loaded / evt.total);
                                $scope.log = '\tprogress: ' + progressPercentage +
                                    '% ' + evt.config.data.file.name + '\n' +
                                    $scope.log;
                            });
                        } else {
                            NotificationService.error("File '" + file.name + "' exceeded the maximum size allowed (5MB). File was not uploaded.");
                        }
                    }
                }
            }
        };

        $scope.romanianVersion = function () {
            $scope.primaryHeading = 'Incarcare fisiere';
        };

        $scope.englishVersion = function () {
            $scope.primaryHeading = 'Upload files';
        };
});