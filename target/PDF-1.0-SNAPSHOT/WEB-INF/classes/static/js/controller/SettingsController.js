angular.module('PDF')
    .controller('SettingsController', function ($scope, $http, Upload, $sce) {

        $scope.files = [];
        // $scope.log = '';
            $http({
                method: 'GET',
                url: '/convert/'
            }).then(function successCallback(response) {
                console.log(response.data);
                if (response.data != "") {
                    $scope.files = response.data;
                    console.log("am ajuns aici");
                    // $("doc-pages-to-bind-1").val($scope.files[0].settings.pagesToBind);
                } else {
                    console.log("AUTHENTICATION ERROR!");
                }
            });



        $scope.deleteFile = function (file, index) {
            Upload.upload({
                method: 'DELETE',
                headers: {'Content-Type': 'application/json'},
                url: '/convert/' + file.name + '/' + file.extension
            }).then(function (resp) {
                if(resp.data == true){
                    $scope.files.splice(index, 1);
                }else{
                    console.log('DELETE ERROR');
                }
            });
        };

        $scope.generatePDF = function(){
            Upload.upload({
                method: 'GET',
                headers: {'Content-Type': 'application/json'},
                url: '/convert/generatePDF'
            }).then(function (resp) {
                if(resp.data != ''){

                }else{
                    console.log('GeneratePDF ERROR');
                }
            });
        }

    });