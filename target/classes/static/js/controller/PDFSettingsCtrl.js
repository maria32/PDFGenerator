angular.module('PDF')
    .controller('PDFSettingsCtrl', function ($scope, $http, Upload, $sce) {

        function setPassword(ok) {
            console.log(ok.check());
        }

        console.log("Output Settings");

        //get PDFSettings
        $scope.pdfSettings = null;
        $http({
            method: 'GET',
            url: '/pdf-settings/'
        }).then(function successCallback(response) {
            if (response.data != "") {
                $scope.pdfSettings = response.data;
                $scope.fileName = response.data.fileName;
                console.log($scope.pdfSettings);
            } else {
                console.log("PDFSettings GET error");
            }
        });

        // show/hide password
        $("#button-show-password").mousedown(function() {
            timer = setTimeout(function(){
                $("#password").prop('type', 'text');
                console.log($scope.pdfSettings);
            },5);
        }).bind('mouseup', function() {
            clearTimeout(timer);
            $("#password").prop('type', 'password');
        });

        $scope.deleteFile = function($event){

            $event.stopPropagation();
            $scope.file = null;
            $scope.fileName = null;
            $scope.pdfSettings.watermarkPic = null;

        };

        $scope.submitPDFSettings = function(){
                console.log($scope.pdfSettings);

                Upload.upload({
                    method: 'POST',
                    //headers: {'Content-Type': 'application/json'},
                    url: '/pdf-settings/add',
                    data: {file: $scope.file}
                }).then(function (response) {
                    console.log(response.data);
                    if(response.data != ''){
                        console.log("Saved file");
                        $scope.pdfSettings.watermarkPic = response.data;
                        $scope.fileName = "";
                        $http({
                            method: 'POST',
                            url: '/pdf-settings/',
                            data: $scope.pdfSettings
                        }).then(function successCallback(response) {
                            if (response.data != "") {
                                console.log("like magic");
                                console.log(response.data);
                            } else {
                                console.log("PDFSettings GET error2");
                            }
                        });

                    }else{
                        console.log('Save PDFSettings ERROR');
                    }
                });
        }

    });