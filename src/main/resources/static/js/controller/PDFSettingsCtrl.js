angular.module('PDF')
    .controller('PDFSettingsCtrl', function ($scope, $rootScope, $sessionStorage, $location, $http, Upload, NotificationService) {

        if($sessionStorage.user == undefined){
            $location.path("/login");
        }

        function setPassword(ok) {
            console.log(ok.check());
        }

        //get PDFSettings
        $scope.pdfSettings = null;
        $http({
            method: 'GET',
            url: '/pdf-settings/'
        }).then(function successCallback(response) {
            if (response.data != "") {
                $scope.pdfSettings = response.data;
                if(response.data.imageWatermark != null){
                    $scope.fileName = response.data.imageWatermark.fileName;
                }else{
                    $scope.fileName = null;
                }
                console.log($scope.pdfSettings);
            } else {
                NotificationService.error("PDF settings not retrieved.");
            }
        });

        $(function () {
            $('[data-toggle="popover"]').popover()
        })


        $("#image-position").imagepicker();

        // show/hide password
        $("#button-show-password").mousedown(function() {
            timer = setTimeout(function(){
                $("#password").prop('type', 'text');
            },5);
        }).bind('mouseup', function() {
            clearTimeout(timer);
            $("#password").prop('type', 'password');
        });

        $scope.deleteFile = function($event){

            $event.stopPropagation();
            $scope.file = null;
            $scope.fileName = null;
            $scope.pdfSettings.imageWatermark = null;

        };

        $scope.postPDFSettings = function(){
            $.ajax({
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                url: '/pdf-settings/save/',
                data: JSON.stringify($scope.pdfSettings),
                success: function(result){
                    NotificationService.success("PDF Settings saved.");
                    if(result != "") {
                        console.log(result.data);
                    }
                },
                error: function(e) {
                    NotificationService.error("PDF Settings not saved.");
                }
            });
        };

        $scope.submitPDFSettings = function(){
            if ($scope.file != null) {
                Upload.upload({
                    method: 'POST',
                    //headers: {'Content-Type': 'application/json'},
                    url: '/pdf-settings/upload-watermark-picture/',
                    data: {file: $scope.file}
                }).then(function (response) {
                    if (response.data != '') {
                        console.log("Saved file");
                        $scope.pdfSettings.imageWatermark.watermark = response.data.watermark;
                        $scope.pdfSettings.imageWatermark.fileName = response.data.fileName;
                        $scope.pdfSettings.imageWatermark.width = response.data.width;
                        $scope.pdfSettings.imageWatermark.height = response.data.height;
                        $scope.fileName = null;
                    } else {
                        NotificationService.error('Failed to upload file');
                    }
                    $scope.postPDFSettings();
                });
            }else{
                $scope.postPDFSettings();
            }
            console.log($scope.pdfSettings);
        };


    });