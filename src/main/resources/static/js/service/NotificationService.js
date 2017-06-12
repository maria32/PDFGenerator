'use strict';

angular.module('PDF')
    .service('NotificationService', function() {

        var self = this;
        var notify = function(message, type) {
            $.notify(message, type);
        };

        self.success = function(message) {
            notify(message, 'success');
        };

        self.info = function(message) {
            notify(message, 'info');
        };

        self.warn = function(message) {
            notify(message, 'warn');
        };

        self.error = function(message) {
            notify(message, 'error');
        };

    });
