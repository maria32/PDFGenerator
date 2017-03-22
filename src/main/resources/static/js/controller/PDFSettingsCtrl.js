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
                if(response.data.imageWatermark != null){
                    $scope.fileName = response.data.imageWatermark.fileName;
                }else{
                    $scope.fileName = null;
                }
                console.log($scope.pdfSettings);
            } else {
                console.log("PDFSettings GET error");
            }
        });

        $scope.positions = ['TOP_LEFT', 'TOP_CENTER', 'TOP_RIGHT', 'MIDDLE_LEFT', 'CENTER', 'MIDDLE_RIGHT', 'BOTTOM_LEFT', 'BOTTOM_CENTER', 'BOTTOM_RIGHT'];

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
                url: '/pdf-settings/',
                data: JSON.stringify($scope.pdfSettings),
                success: function(result){
                    console.log("Success: PDF Settings saved.");
                    if(result != "") {

                    }
                },
                error: function(e) {
                    console.log("Error. PDF Settings not saved.");
                }
            });
        };

        $scope.submitPDFSettings = function(){
            if ($scope.file != null) {
                Upload.upload({
                    method: 'POST',
                    //headers: {'Content-Type': 'application/json'},
                    url: '/pdf-settings/upload-watermark-picture',
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
                        console.log('upload file error');
                    }
                    $scope.postPDFSettings();
                });
            }else{
                $scope.postPDFSettings();
            }
            console.log($scope.pdfSettings);
        }

    });