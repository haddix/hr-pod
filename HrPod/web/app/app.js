angular.module("hrpod", [
    "ngRoute",
    "ngAnimate",     
    "ui.bootstrap",
    "home",
    "connect",
    "resumes",
    "jobs"
])
        
        .config(["$routeProvider", function ($routeProvider) {


                $routeProvider.when("/", {
                    controller: "HomeCtrl",
                    templateUrl: "app/home/home-view.html"
                })
                        .when("/home", {
                            controller: "HomeCtrl",
                            templateUrl: "app/home/home-view.html"
                        })   
                        .when("/connect", {
                            controller: "ConnectCtrl",
                            templateUrl: "app/connect/connect-view.html"
                        })
                        .when("/connectionEdit", {
                            controller: "ConnectCtrl",
                            templateUrl: "app/connect/connectionEdit-view.html"
                        })
                        .when("/connectionNew", {
                            controller: "ConnectCtrl",
                            templateUrl: "app/connect/connectionNew-view.html"
                        })
                        .when("/connectionNew2", {
                            controller: "ConnectCtrl",
                            templateUrl: "app/connect/connectionNew2-view.html"
                        })
                        .when("/resumes", {
                            controller: "ResumesCtrl",
                            templateUrl: "app/resumes/resumes-view.html"
                        })
                        .when("/jobs", {
                            controller: "JobsCtrl",
                            templateUrl: "app/jobs/jobs-view.html"
                        })
                        .otherwise({redirectTo: "/"});


            }]);


