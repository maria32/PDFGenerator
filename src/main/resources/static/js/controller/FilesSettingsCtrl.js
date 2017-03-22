angular.module('PDF')
    .controller('FilesSettingsCtrl', function ($rootScope, $scope, $http, Upload, $sce) {

        $scope.files = [];
        $scope.alignmentSelected=[];
        // $scope.log = '';
            $http({
                method: 'GET',
                url: '/convert/'
            }).then(function successCallback(response) {
                console.log(response.data);
                if (response.data != "") {
                    $scope.files = response.data;
                    $rootScope.filesNo = $scope.files.length;
                    // $("doc-pages-to-bind-1").val($scope.files[0].settings.pagesToBind);
                } else {
                    console.log("No Files or files not retrieved correctly");
                }
            });

        $scope.alignmentOptions = [];
        $http({
            method: 'GET',
            url: '/update/settings/image-alignment-options/'
        }).then(function successCallback(response) {
            if (response.data != "") {
                $scope.alignmentOptions = response.data;
                console.log($scope.alignmentOptions);
            } else {
                console.log("Image Alignment options GET error");
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
                    $rootScope.filesNo = $scope.files.length;
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
                //pdf
                if($scope.files[i].settings.type == "pdf"){
                    if($scope.files[i].settings.pagesIncluded == "")
                        $scope.files[i].settings.pagesIncluded = "All";
                }
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

    });