angular.module('PDF')
    .controller('FilesSettingsCtrl', function ($rootScope, $scope, $sessionStorage, $http, Upload, $location, NotificationService) {

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

        $scope.files = [];
        $scope.alignmentSelected=[];
        // $scope.log = '';
            $http({
                method: 'GET',
                url: '/convert/' + $sessionStorage.user.id + '/'
            }).then(function successCallback(response) {
                if (response.data != undefined) {
                    $scope.files = response.data;
                    $rootScope.filesNo = $scope.files.length;
                    $sessionStorage.filesNo = $rootScope.filesNo;
                    // $("doc-pages-to-bind-1").val($scope.files[0].settings.pagesToBind);
                } else {
                    NotificationService.error("No Files or files not retrieved correctly");
                }
            });

        $scope.alignmentOptions = ["LEFT", "MIDDLE", "RIGHT", "TEXTWRAP", "UNDERLYING"];
/*        $http({
            method: 'GET',
            url: '/update/settings/image-alignment-options/'
        }).then(function successCallback(response) {
            if (response.data != "") {
                $scope.alignmentOptions = response.data;
                console.log($scope.alignmentOptions);
            } else {
                console.log("Image Alignment options GET error");
            }
        });*/

        $scope.deleteFile = function (file, index) {
            Upload.upload({
                method: 'DELETE',
                headers: {'Content-Type': 'application/json'},
                url: '/convert/' + $sessionStorage.user.id + '/' + file.name + '/' + file.extension
            }).then(function (resp) {
                if(resp.data == true){
                    $scope.files.splice(index, 1);
                    $rootScope.filesNo = $scope.files.length;
                }else{
                    NotificationService.error('Failed to delete' + file.name + '.' + file.extension);
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
                    url: '/update/' + $sessionStorage.user.id + '/order-of-files/',
                    data: JSON.stringify(listOfOrder),
                    success: function(result){
                        NotificationService.info("Updated order of files");
                    },
                    error: function(e) {
                        NotificationService.error('Error updating order of files: ' + e);
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
                // console.log($scope.files[i]);
            }

            $.ajax({
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                url: '/update/' + $sessionStorage.user.id + '/settings',
                data: JSON.stringify($scope.files),
                success: function(result){
                    NotificationService.success("Saved changes.");
                },
                error: function(e) {
                    console.log(e);
                }});
        };

    });