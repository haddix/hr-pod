angular.module("home", [])
        .factory("securityFactory", function ($http) {
            return{
                authenticateUser: function () {
                    console.log("In Service authenticateUser");
                    var url = "security";
                    return $http({
                        method: "POST",
                        url: url,
                        headers: {"content-type": "application/x-www-form-urlencoded"}
                    })
                            .success(function (data) {
                                console.log(data);
                            })
                            .error(function (data) {
                                console.log("ERROR in Service for authenticateUser")
                            })

                }
            }
        });

angular.module("home").controller("HomeCtrl", ["$scope", "$http", "securityFactory", function ($scope, $http, securityFactory) {

        $scope.getUserData = function () {
            console.log("getting user data in headerCtrl");
            securityFactory.authenticateUser().success(function (data) {
                console.log(data);
                $scope.userData = data;
            })

        };


        $scope.$watch('$viewContentLoaded', function () {
            $scope.getUserData();
        });

    }]);
