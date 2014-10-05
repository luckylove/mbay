/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
var addModelURL = "addUser.json";

cruiseApp.controller('UserGroupCtrl', function ($scope, $modal, $http) {
    $http({
        method: "get",
        url: "getLogUser.json",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        data: $.param({})
    }).success(function(data, status, headers, config) {
        if(data.success){
            $scope.userForm = data.result;
            delete  $scope.userForm['regDate'];
            delete  $scope.userForm['modDate'];
        }
    }).
    error(function(data, status, headers, config) {
        alert("have error")
    });


    $scope.submit = function (form) {
        if(form.$valid) {

            $http({
                method: "post",
                url: addModelURL,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param($scope.userForm)
            }).success(function(data, status, headers, config) {
                    if(!data.success){
                        bootbox.alert(data.errorMsg);
                    } else {
                        bootbox.alert("Your  profile is updated");
                    }
                }).
                error(function(data, status, headers, config) {
                    alert("have error")
                });
        } else {
            alert("Please input required field");
        }
    };


});

