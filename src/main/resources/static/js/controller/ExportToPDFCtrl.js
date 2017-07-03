angular.module('PDF')
    .controller('ExportToPDFCtrl', function ($sce, $rootScope, $scope, $sessionStorage, $http, Upload, $location, NotificationService) {

        $scope.exportAs = 'download';
        $scope.preview = false;
        $scope.conversionMessage = '';
        $scope.convertedPDF = undefined;

        $rootScope.generatePDFPreview = function () {
            $scope.preview = !$scope.preview;

            if ($rootScope.filesNo > 0 && $sessionStorage.user.id != null) {
                console.log($scope.exportAs);
                $http({
                    url: '/convert/' + $sessionStorage.user.id + '/generatePDF/' + "none",
                    method: "GET",
                    headers: {'Accept': 'application/pdf'},
                    responseType: 'arraybuffer'
                }).then(function (response) {
                    if (response.data != null) {
                        var file = new Blob([response.data], {type: 'application/pdf'});
                        var fileURL = URL.createObjectURL(file);
                        $scope.content = $sce.trustAsResourceUrl(fileURL);
                    } else {
                        NotificationService.error($sessionStorage.language == 'Romanian' ? 'Eroare preview' : 'Preview error');
                    }
                });
            } else {
                NotificationService.warn($sessionStorage.language == 'Romanian' ? 'Conversia nu se poate realiza. Asigurati-va ca ati trecut prin Pasul 3 inainte de conversie' : "No files to convert! Make sure you clicked 'Step 3' before converting.");
            }
        };


        $rootScope.showProgressCircle = false;
        $rootScope.progress = 0;

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
            $scope.conversionMessage = '';
            if ($rootScope.filesNo > 0 && $sessionStorage.user.id != null) {
                $rootScope.showProgressCircle = true;
                if ($scope.exportAs == 'email'){
                    $scope.conversionMessage = $sessionStorage.language == 'Romanian' ? 'Conversie in desfasurare...' : 'Conversion started...';
                    NotificationService.info($scope.conversionMessage);
                }

                $http({
                    url: '/convert/' + $sessionStorage.user.id + '/generatePDF/' + $scope.exportAs,
                    method: "GET",
                    headers: {'Accept': 'application/pdf'},
                responseType: 'arraybuffer'
                }).then(function (response) {
                    console.log(response);
                    if (response.data != '') {
                        if ($scope.exportAs == 'email') {
                            $scope.conversionMessage = $sessionStorage.language == 'Romanian' ? 'Un mail e fost trimis catre tine cu fisierul PDF convertit.' : 'An email was send to your address with the converted file';
                            NotificationService.info($scope.conversionMessage);
                        } else if ($scope.exportAs == 'download') {
                            console.log(response.data);
                            file = new Blob([response.data], {type: 'application/pdf'});
                            fileURL = URL.createObjectURL(file);
                            link = document.createElement("a");
                            link.download = "GeneratedPDF";
                            link.href = fileURL;
                            document.body.appendChild(link);
                            link.click();
                            document.body.removeChild(link);
                            delete link;
                        }
                    } else {
                        console.log('GeneratePDF ERROR');
                    }
                });
                $rootScope.progressBarStatus = setInterval(getProgressBarStatus, 500);
            }else {
                NotificationService.warn($sessionStorage.language == 'Romanian' ? 'Conversia nu se poate realiza. Asigurati-va ca ati trecut prin Pasul 3 inainte de conversie' : "No files to convert! Make sure you clicked 'Step 3' before converting.");
            }
        };

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

    });