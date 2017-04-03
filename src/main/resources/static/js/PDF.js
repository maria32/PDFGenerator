'use strict';

angular
    .module('PDF', [
        // 'WeatherTabs',

        //external dependencies
        'ngFileUpload',
        'ngSanitize', //for using of ng-bind-html
        'ngRoute',
        'ui.bootstrap',
        'ui.sortable', //for changing order of files
        'nya.bootstrap.select' //for <select> multiple nice loop with ng-repeat
    ])

    .run(function ($rootScope, $location, Upload,$http) {
        $rootScope.filesNo;
        $rootScope.showProgressCircle = false;

        function getProgressBarStatus(){
            $http({
                method: 'GET',
                url: '/convert/generatePDF/progress-bar'
            }).then(function successCallback(response) {
                console.log(response.data);
                if(response.data == $rootScope.filesNo) {
                    clearTimeout($rootScope.progressBarStatus);
                    $rootScope.showProgressCircle = false;
                    return response.data;
                }else{
                    return response.data;
                }
            })
        }

        $rootScope.generatePDF = function () {
            if ($rootScope.filesNo > 0) {
                $rootScope.showProgressCircle = true;
                Upload.upload({
                    method: 'GET',
                    // headers: {'Content-Type': 'application/json'},
                    url: '/convert/generatePDF'
                }).then(function (response) {
                    console.log(response.data);
                    if (response.data != '') {

                    } else {
                        console.log('GeneratePDF ERROR');
                    }
                });

                $rootScope.progressBarStatus = setInterval(getProgressBarStatus, 500);

            }else {
                alert("No files to convert!");
            }
        }


        $('#pdf-generation-progress-circle').each(function(){
            var percent = $(this).find('.circle').attr('data-percent');
            var percentage = parseInt(percent, 10) / parseInt(100, 10);
            var animate = $(this).data('animate');
            if (!animate) {
                $(this).data('animate', true);
                $(this).find('.circle').circleProgress({
                    startAngle: -Math.PI / 2,
                    value: percent / 100,
                    thickness: 14,
                    fill: {
                        color: '#1B58B8'
                    }
                }).on('circle-animation-progress', function (event, progress, stepValue) {
                    $(this).find('div').text((stepValue*100).toFixed(1) + "%");
                }).stop();
            }
        });

    })

    .config(function ($routeProvider) {
        $routeProvider.when('/', {
            templateUrl: 'template/upload.html',
            controller: 'UploadCtrl'
        }).when('/pdf-settings', {
            templateUrl: 'template/pdf-settings.html',
            controller: 'PDFSettingsCtrl'
        }).when('/settings', {
            templateUrl: 'template/files-settings.html',
            controller: 'FilesSettingsCtrl'
        }).when('image-settings', {
            templateUrl: 'template/settings/image.html'
        }).when('document-settings', {
            templateUrl: 'template/settings/document.html'
        }).when('text-settings', {
            templateUrl: 'template/settings/text.html'
        }).otherwise({
            redirectTo: '/'
        });
    });