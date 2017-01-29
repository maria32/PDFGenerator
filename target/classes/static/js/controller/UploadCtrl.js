/**
 * Created by martanase on 12/11/2016.
 */

//inject angular file upload directives and services.
// var app = angular.module('fileUpload', ['ngFileUpload']);
angular.module('PDF')
    .controller('UploadCtrl', ['$scope', 'Upload', '$timeout', function ($scope, Upload, $timeout) {
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
                console.log(file);
                if(file.name.search(" ") == -1) {
                    if (!file.$error) {
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
                    }
                } else {
                    $scope.log = 'Error: "' + file.name + '" name contains white spaces.\n' + $scope.log;
                }
            }
        }
    };
}]);