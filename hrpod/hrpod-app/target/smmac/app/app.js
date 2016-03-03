angular.module("smmac", [
    "ngRoute",
    "ngAnimate",     
    "ui.bootstrap",
    "home"
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
                        .otherwise({redirectTo: "/"});


            }]);


