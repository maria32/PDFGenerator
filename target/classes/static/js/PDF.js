'use strict';

angular
    .module('PDF', [
        // 'WeatherTabs',

        //external dependencies
        'ngFileUpload',
        'ngSanitize', //for using of ng-bind-html
        'ngRoute',
        'ui.bootstrap',
        'ui.sortable' //for changing order of files
    ])

    .run(function ($rootScope, $location) {



    })

    .config(function($routeProvider) {
        $routeProvider.
        when('/', {
            templateUrl: 'template/upload.html',
            controller: 'UploadCtrl'
        }).
        when('/settings', {
            templateUrl: 'template/files-settings.html',
            controller: 'SettingsController'
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