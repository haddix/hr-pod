angular.module("jobs", ['ngFileUpload']);

angular.module("jobs").controller("JobsCtrl", ["$scope", "$http", 'Upload', function ($scope, $http, Upload) {

        $scope.onFileSelect = function ($files) {
            //$files: an array of files selected, each file has name, size, and type.
            for (var i = 0; i < $files.length; i++) {
                var $file = $files[i];
                Upload.upload({
                    url: 'upload?type=jobs',
                    file: $file,
                    progress: function (e) {
                    }
                }).then(function (data, status, headers, config) {
                    $scope.closeUploadModal();
                    //console.log(data);
                });
            }
        }
        
        $scope.openUploadModal = function () {
            $('#uploadModal').modal('toggle');
        };
        
        $scope.closeUploadModal = function () {
            $('#uploadModal').modal('toggle');
        };
        
        $scope.$on('$viewContentLoaded', function () {

        });

    }]);
