'use strict';

angular
    .module('PDF', [
        //external dependencies
        'ngFileUpload',
        'ngSanitize', //for using of ng-bind-html
        'ngRoute',
        'ui.bootstrap',
        'ui.sortable', //for changing order of files
        'nya.bootstrap.select', //for <select> multiple nice loop with ng-repeat
        'ngStorage'
    ])

    .run(function ($rootScope, $location, Upload,$http, NotificationService, $sessionStorage) {

        $rootScope.user = $sessionStorage.user;
        if ($sessionStorage.user == undefined) {
            console.log("go to login page.");
            console.log($sessionStorage.user);
            $location.path("/login");
        }

        $rootScope.filesNo = $sessionStorage.filesNo;

        var headers = $rootScope.credentials ? {authorization : "Basic " + btoa($rootScope.credentials.username + ":" + $rootScope.credentials.password)} : {};

        $rootScope.login = function () {
            if ($rootScope.credentials.username != undefined && $rootScope.credentials.password != undefined) {
                $http({
                    method: 'POST',
                    url: '/login/credentials',
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify($rootScope.credentials)
                }).then(function successCallback(response) {
                    if (response.data != null && response.data != "") {
                        $rootScope.user = response.data;
                        $sessionStorage.user = response.data;
                        $location.path("/upload");
                        NotificationService.success("Successful login");
                    } else {
                        console.log("Login failed.");
                        $rootScope.user = undefined;
                        $location.path("/login");
                    }
                });
            } else {
                NotificationService.warn("Empty credentials.");
            }

            $('#signInModal').modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();

        };

        $rootScope.signup = function () {
            console.log("Sending credentials:");
            console.log($rootScope.credentials);
            if ($rootScope.credentials.username != undefined && $rootScope.credentials.password != undefined && $rootScope.credentials.email != undefined) {
                $http({
                    method: 'PUT',
                    url: '/login/sing-up',
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify($rootScope.credentials)
                }).then(function successCallback(response) {
                    console.log("Result from server:");
                    console.log(headers()['Content-Range']);
                    if (response.data != null && response.data != "") {
                        $rootScope.user = response.data;
                        $sessionStorage.user = response.data;
                        $location.path("/upload");
                        NotificationService.success("Successful login");
                    } else {
                        NotificationService.error("Username or email already exists.");
                        $rootScope.user = undefined;
                        $location.path("/login");
                    }
                });
            } else {
                NotificationService.warn("Empty credentials.");
            }

            $('#signUpModal').modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();

        };

        $rootScope.logout = function () {
            $sessionStorage.user = undefined;
            $rootScope.user = undefined;
            $sessionStorage.filesNo = undefined;
            $rootScope.filesNo = undefined;
            $location.path('/login');
            console.log("User logged out.");
        };


        $rootScope.showProgressCircle = false;
        $rootScope.progress = 0;

//        $rootScope.username = null;

        // function sleep(miliseconds) {
        //     var currentTime = new Date().getTime();
        //
        //     while (currentTime + miliseconds >= new Date().getTime()) {
        //     }
        //
        // }

        function getProgressBarStatus(){
            $http({
                method: 'GET',
                url: '/convert/generatePDF/progress-bar'
            }).then(function successCallback(response) {
                $rootScope.progress = parseInt((100 / $rootScope.filesNo) * response.data);
                console.log(response.data + " " + $rootScope.progress);
                $('#my-progress')
                    .css('width', $rootScope.progress+'%')
                    .attr('aria-valuenow', $rootScope.progress);
                if(response.data == $rootScope.filesNo) {
                    clearTimeout($rootScope.progressBarStatus);
                    $rootScope.showProgressCircle = false;
                }
                return response.data;
            });
        }

        $rootScope.generatePDF = function () {
            if ($rootScope.filesNo > 0) {
                $rootScope.showProgressCircle = true;
                Upload.upload({
                    method: 'GET',
                    // headers: {'Content-Type': 'application/json'},
                    url: '/convert/' + $sessionStorage.user.id + '/generatePDF'
                }).then(function (response) {
                    console.log("xxx" + response);
                    if (response.data != '') {

                    } else {
                        console.log('GeneratePDF ERROR');
                    }
                });
                $rootScope.progressBarStatus = setInterval(getProgressBarStatus, 500);
            }else {
                alert("No files to convert! Make sure you clicked 'Step 3' before converting.");
            }
        };


        $rootScope.positions = ['TOP_LEFT', 'TOP_CENTER', 'TOP_RIGHT', 'MIDDLE_LEFT', 'CENTER', 'MIDDLE_RIGHT', 'BOTTOM_LEFT', 'BOTTOM_CENTER', 'BOTTOM_RIGHT'];
        $rootScope.positionsWithinText = ["None", "Left", "Middle", "Right", "Underlying"];
    })

    .config(function ($routeProvider, $httpProvider) {

        $routeProvider.when('/upload', {
            templateUrl: 'template/upload.html',
            controller: 'UploadCtrl'
        }).when('/login', {
            templateUrl: 'login.html'

        }).when('/pdf-settings', {
            templateUrl: 'template/pdf-settings.html',
            controller: 'PDFSettingsCtrl'
        }).when('/settings', {
            templateUrl: 'template/files-settings.html',
            controller: 'FilesSettingsCtrl'

        }).when('/image-settings', {
            templateUrl: 'template/settings/image.html'
        }).when('/document-settings', {
            templateUrl: 'template/settings/document.html'
        }).when('/text-settings', {
            templateUrl: 'template/settings/text.html'
        }).when('/extensions', {
            templateUrl: 'template/extensions.html'
        }).when('/demo-instructions', {
            templateUrl: 'template/demo-instructions.html'
        }).otherwise({
            redirectTo: '/upload'
        });

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';


    });