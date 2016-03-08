angular.module("hrpod", [
    "ngRoute",
    "ngAnimate",     
    "ui.bootstrap",
    "home",
    "search",
    "resumes",
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
                        .when("/search", {
                            controller: "SearchCtrl",
                            templateUrl: "app/search/search-view.html"
                        })
                        .when("/resumes", {
                            controller: "ResumesCtrl",
                            templateUrl: "app/resumes/resumes-view.html"
                        })
                        .otherwise({redirectTo: "/"});


            }]);


