'use strict';

angular
    .module('PDF', [
        //external dependencies
        'ngFileUpload',
        'ngSanitize', //for using of ng-bind-html
        'ngRoute',
        'ui.bootstrap',
        'ui.sortable', //for changing order of files
        'nya.bootstrap.select', //for <select> multiple nice loop with ng-repeat
        'ngStorage'
    ])

    .run(function ($rootScope, $location, Upload,$http, NotificationService, $sessionStorage) {


        console.log($sessionStorage.language);
        if ($sessionStorage.language == undefined) {
            $sessionStorage.language = 'Romanian';
        }

        $( document ).ready(function() {
            if ($sessionStorage.language == 'Romanian') {
                document.getElementById("language-ro").click();
            } else if ($sessionStorage.language == 'English') {
                document.getElementById("language-en").click();
            }
        });


        $rootScope.romanianVersion = function () {
            console.log("Romanian version");
            $sessionStorage.language = 'Romanian';
            $rootScope.description = {
                part1: 'Converteste Word, Excel, PowerPoint, imagini si',
                part2: 'multe altele',
                part3: 'la PDF.',
                part4: 'Go4PDF este o aplicatie gratuita. Creeaza un cont si incepe sa lucrezi. Simplu! '
            };
            $rootScope.description2 = 'In 4 pasi simpli iti poti crea PDF-ul dorit cu multe optiuni de setari pentru fisierele tale si nu numai.';
            $rootScope.step = 'Pasul';
            $rootScope.demo = {
                here: 'Aici',
                demo: 'Poti vedea un demo despre cum functioneaza aplicatia.',
                before: 'Este o idee buna sa vezi acest demo inainte de crearea unui cont, sa te asiguri ca este ceea ce doresti'
            };
            $rootScope.uploadFiles = {
                title: 'Incarcare fisiere',
                description: 'Incarca fisierele dorite cu un click sau drag-and-drop.',
                extensionsText: {
                    part1: 'Vezi toate ',
                    part2: 'extensiile de fisiere',
                    part3: ' pe care le poti incarca.'
                },
                toKnow: 'De stiut:',
                toKnow1: 'Marimea maxima acceptata a unui fisier este de 5MB',
                toKnow2: 'Pentru a adauga mai multe fisiere prin click, mentine tasta Ctrl apasata in selectia de fisiere',
                uploadStyle: 'drag-and-drop sau click',
                uploadedFiles: 'Fisiere incarcate:',
                uploadStatus: 'Status de incarcare'
            };
            $rootScope.extensions = {
                title: 'Formate de fisiere acceptate',
                backButton: 'Inapoi'
            };
            $rootScope.outputSettings = {
                title: 'Setari PDF',
                description: 'Adauga setari la PDF-ul rezultat: parola, watermark (text sau imagine) cu setari pentru dimensiune, pozitie si transparenta. Acest pas este optional.',
                noPasswordSet: 'Nu ati setat o parola.',
                noWatermarkSet: 'Nu ati setat un watermark.',
                menu: {
                    securityOptions: {
                        title: 'Optiuni de securitate',
                        password: 'Parola',
                        lock: {
                            title: 'Protectie',
                            options: ['Printare', 'Copiere', 'Modificare']
                        }
                    },
                    watermark: {
                        title: 'Watermark',
                        text: {
                            title: 'Text'
                        },
                        image: {
                            title: 'Imagine',
                            uploadStyle: 'Drop sau click pentru incarcare',
                            settings: {
                                title: 'Setari watermark',
                                position: {
                                    title: 'Pozitie',
                                    option1: 'Pozitie predefinita...',
                                    option2: 'Pozitie absoluta',
                                    sizesInfo: 'Info marime',
                                    x0: 'x = 0 inseamna in stanga paginii.',
                                    y0: 'y=0 inseamna in josul paginii'
                                },
                                scale: 'Dimensiune',
                                opacity: 'Opacitate'
                            }
                        }
                    }
                },
                saveButton: 'Salvare'
            };
            $rootScope.filesSettings = {
                title: 'Setari fisiere',
                description: 'Personalizeaza-ti fiecare fisier in parte. Poti specifica ce pagini sa fie luate in considerare, poti roti imagini sau sa schimbi ordinea fisierelor. Plus multe altele.'
            };
            $rootScope.exportToPDF = {
                title: 'Exporta la PDF',
                description: 'Conversie la PDF. Previzualizeaza inainte de download, downloadeaza direct sau seteaza ca PDF-ul sa iti vina pe mail.',
                preview: 'Preview PDF rezultat',
                hidePreview: 'Ascunde preview',
                method: 'Metoda de download',
                pdfName: 'Nume PDF',
                option1: 'Download',
                option2: 'Trimitere prin email',
                startButton: 'START conversie'
            };
        };

        $rootScope.englishVersion = function () {
            console.log("English version");
            $sessionStorage.language = 'English';
            $rootScope.description = {
                part1: 'Convert Word, Excel, PowerPoint, images and',
                part2: 'more',
                part3: 'to PDF.',
                part4: 'Go4PDF is a free application. Create an account and start converting - that simple'
            };
            $rootScope.description2 = 'In 4 simple steps, you can get almost any file converted to PDF, with customizable options at your hand.';
            $rootScope.step = 'Step';
            $rootScope.demo = {
                here: 'Here',
                demo: 'you can find a demo of how the application works.',
                before: 'You might want to check it out before creating an account, just so you know this is what you need.'
            };
            $rootScope.uploadFiles = {
                title: 'Upload files',
                description: 'Upload all of your files with a single click or drag and drop.',
                extensionsText: {
                    part1: 'See all ',
                    part2: 'extensions',
                    part3: ' that you can upload.'
                },
                toKnow: 'Nice to know:',
                toKnow1: 'Maximum size per file is 5MB.',
                toKnow2: 'To add multiple files, press the Ctrl-key.',
                uploadStyle: 'Drop or click to upload',
                uploadedFiles: 'Last file uploaded:',
                uploadStatus: 'Upload Log:'
            };
            $rootScope.extensions = {
                title: 'Supported format files',
                backButton: 'Back'
            };
            $rootScope.outputSettings = {
                title: 'Output Settings',
                description: 'Personalize your PDF output. Add a password, watermark (image or text) by settings its size, position and opacity. This step is optional.',
                noPasswordSet: 'No password set.',
                noWatermarkSet: 'No watermark set.',
                menu: {
                    securityOptions: {
                        title: 'Security options',
                        password: 'Password',
                        lock: {
                            title: 'Lock',
                            options: ['Printing', 'Copying', 'Modifying']
                        }
                    },
                    watermark: {
                        title: 'Watermark',
                        text: {
                            title: 'Text'
                        },
                        image: {
                            title: 'Image',
                            uploadStyle: 'Drop or click to upload',
                            settings: {
                                title: 'Watermark Settings',
                                position: {
                                    title: 'Position',
                                    option1: 'Set position from...',
                                    option2: 'Set absolute position',
                                    sizesInfo: 'Sizes info',
                                    x0: 'x = 0 means left of page.',
                                    y0: 'y=0 means bottom of page.'
                                },
                                scale: 'Scale',
                                opacity: 'Opacity'
                            }
                        }
                    }
                },
                saveButton: 'Save'
            };
            $rootScope.filesSettings = {
                title: 'Files settings',
                description: 'Customize each of your files before conversion. You can select specific files from a document, rotate images, or change order of files after upload.'
            };
            $rootScope.exportToPDF = {
                title: 'Export to PDF',
                description: 'Conversion to PDF is a click away. Preview the output before conversion, download directly or have your PDF sent to your email address.',
                preview: 'Preview resulted PDF',
                hidePreview: 'Hide preview',
                method: 'How to download PDF',
                pdfName: 'Name your pdf here',
                option1: 'Download',
                option2: 'Send to my email',
                startButton: 'START conversion'
            };
        };

        $rootScope.user = $sessionStorage.user;

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

        $rootScope.filesNo = $sessionStorage.filesNo;

        var headers = $rootScope.credentials ? {authorization : "Basic " + btoa($rootScope.credentials.username + ":" + $rootScope.credentials.password)} : {};

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

        $rootScope.login = function () {
            if ($rootScope.credentials.username != undefined && $rootScope.credentials.password != undefined) {
                $http({
                    method: 'POST',
                    url: '/login/credentials',
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify($rootScope.credentials)
                }).then(function successCallback(response) {
                    if (response.data != null && response.data != "") {
                        $rootScope.user = response.data;
                        $sessionStorage.user = response.data;
                        $location.path("/upload");
                        NotificationService.success("Successful login");
                    } else {
                        console.log("Login failed.");
                        $rootScope.user = undefined;
                        $location.path("/login");
                    }
                });
            } else {
                NotificationService.warn("Empty credentials.");
            }

            $('#signInModal').modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();

        };

        $rootScope.signup = function () {
            console.log("Sending credentials:");
            console.log($rootScope.credentials);
            if ($rootScope.credentials.username != undefined && $rootScope.credentials.password != undefined && $rootScope.credentials.email != undefined) {
                $http({
                    method: 'PUT',
                    url: '/login/sing-up',
                    headers: {'Content-Type': 'application/json'},
                    data: JSON.stringify($rootScope.credentials)
                }).then(function successCallback(response) {
                    console.log("Result from server:");
                    console.log(headers()['Content-Range']);
                    if (response.data != null && response.data != "") {
                        $rootScope.user = response.data;
                        $sessionStorage.user = response.data;
                        $location.path("/upload");
                        NotificationService.success("Successful login");
                    } else {
                        NotificationService.error("Username or email already exists.");
                        $rootScope.user = undefined;
                        $location.path("/login");
                    }
                });
            } else {
                NotificationService.warn("Empty credentials.");
            }

            $('#signUpModal').modal('hide');
            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();

        };

        $rootScope.logout = function () {
            $http({
                method: 'GET',
                url: '/logout/' + getCookie("remember-me"),
                headers: {'Content-Type': 'application/json'},
            }).then(function successCallback(response) {
                $sessionStorage.user = undefined;
                $rootScope.user = undefined;
                $sessionStorage.filesNo = undefined;
                $rootScope.filesNo = undefined;
                $location.path('/login');
                console.log("User logged out.");
            });
        };




//        $rootScope.username = null;

        // function sleep(miliseconds) {
        //     var currentTime = new Date().getTime();
        //
        //     while (currentTime + miliseconds >= new Date().getTime()) {
        //     }
        //
        // }
        $('#extensions').draggable();

        $rootScope.positions = ['TOP_LEFT', 'TOP_CENTER', 'TOP_RIGHT', 'MIDDLE_LEFT', 'CENTER', 'MIDDLE_RIGHT', 'BOTTOM_LEFT', 'BOTTOM_CENTER', 'BOTTOM_RIGHT'];
        $rootScope.positionsWithinText = ["None", "Left", "Middle", "Right", "Underlying"];
    })

    .config(function ($routeProvider, $httpProvider) {

        $routeProvider.when('/upload', {
            templateUrl: 'template/upload.html',
            controller: 'UploadCtrl'
        }).when('/login', {
            templateUrl: 'login.html'

        }).when('/pdf-settings', {
            templateUrl: 'template/pdf-settings.html',
            controller: 'PDFSettingsCtrl'
        }).when('/settings', {
            templateUrl: 'template/files-settings.html',
            controller: 'FilesSettingsCtrl'
        }).when('/export-to-pdf', {
            templateUrl: 'template/export-to-pdf.html',
            controller: 'ExportToPDFCtrl'

        }).when('/image-settings', {
            templateUrl: 'template/settings/image.html'
        }).when('/document-settings', {
            templateUrl: 'template/settings/document.html'
        }).when('/text-settings', {
            templateUrl: 'template/settings/text.html'
        }).when('/extensions', {
            templateUrl: 'template/extensions.html'
        }).when('/demo-instructions', {
            templateUrl: 'template/demo-instructions.html'
        }).when('/preview', {
            templateUrl: 'template/GeneratedPDF.pdf'
        }).otherwise({
            redirectTo: '/upload'
        });

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';


    });