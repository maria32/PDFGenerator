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

    .run(function ($rootScope, $location) {
        $rootScope.filesNo;

    })

    .config(function($routeProvider) {
        $routeProvider.
        when('/', {
            templateUrl: 'template/upload.html',
            controller: 'UploadCtrl'
        }).
        when('/pdf-settings', {
            templateUrl: 'template/pdf-settings.html',
            controller: 'PDFSettingsCtrl'
        }).
        when('/settings', {
            templateUrl: 'template/files-settings.html',
            controller: 'FilesSettingsCtrl'
        }).
        when('image-settings', {
            templateUrl: 'template/settings/image.html'
        }).
        when('document-settings', {
            templateUrl: 'template/settings/document.html'
        }).
        when('text-settings', {
            templateUrl: 'template/settings/text.html'
        }).
        otherwise({
            redirectTo: '/'
        });
    });