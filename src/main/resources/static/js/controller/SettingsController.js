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

        $scope.sortableFileSettings = {
            update: function(e, ui) {
                var logEntry = $scope.files.map(function(item){
                    return item.id;
                }).join(', ');
                console.log('Update: ' + logEntry);
            },
            stop: function(e, ui) {
                // this callback has the changed model
                var listOfOrder =  $scope.files.map(function(item){
                    return item.id;
                });
                // $scope.files = logEntry;
                console.log(listOfOrder);

                // Upload.upload({
                //     method: 'POST',
                //     url: '/update/order-of-files',
                //     data: listNewOrder
                // });

                $.ajax({
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    url: '/update/order-of-files',
                    data: JSON.stringify(listOfOrder),
                    success: function(result){
                        console.log("updatedOrderOfFiles");
                    },
                    error: function(e) {
                        console.log(e);
                    }});
            }
        };

        $scope.saveAllChanges = function(){
            // page breaks
            for(var i = 0; i < $scope.files.length; i++) {
                var file = $scope.files[i];
                $scope.files[i].settings.pageBreak = document.getElementById('settings-' + file.id +  '-page-break').checked;
                console.log($scope.files[i]);
            }

            $.ajax({
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                url: '/update/settings',
                data: JSON.stringify($scope.files),
                success: function(result){
                    console.log("updatedSettingsOfFiles");
                },
                error: function(e) {
                    console.log(e);
                }});
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
        };

    });