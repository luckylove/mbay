/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
var addModelURL = "updateCruisePort.json";

cruiseApp.controller('UserGroupCtrl', function ($scope, $modal, $http) {
    $http({
        method: "get",
        url: "getCruisePort.json",
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        data: $.param({})
    }).success(function(data, status, headers, config) {
        if(data.success){
            $scope.userForm = data.result;
            $('#summernoteInfo').code(data.result.information);
            $('#summernoteSurcharge').code(data.result.surcharge);
            $('#summernoteDirection').code(data.result.direction);
            delete  $scope.userForm['regDate'];
            delete  $scope.userForm['modDate'];
        }
    }).
    error(function(data, status, headers, config) {
        alert("have error")
    });


    $scope.submit = function (form) {
        if(form.$valid) {

            var info = $('#summernoteInfo').code();
            var sur = $('#summernoteSurcharge').code();
            var dir = $('#summernoteDirection').code();
            $.extend($scope.userForm, {
                information: info,
                surcharge: sur,
                direction: dir
            })
            $http({
                method: "post",
                url: addModelURL,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param($scope.userForm)
            }).success(function(data, status, headers, config) {
                    if(!data.success){
                        bootbox.alert(data.errorMsg);
                    } else {
                        bootbox.alert("Cruise port is updated");
                    }
                }).
                error(function(data, status, headers, config) {
                    alert("have error")
                });
        } else {
            alert("Please input required field");
        }
    };

    $scope.$watch(
        function () { return document.getElementById('summernoteInfo').innerHTML },
        function(newval, oldval){
            $('#summernoteInfo').summernote({
                height: 200  ,
                toolbar: [
                    //[groupname, [button list]]

                    ['style', ['bold', 'italic', 'underline', 'clear']],
                    ['font', ['strikethrough']],
                    ['fontsize', ['fontsize']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['height', ['height']],
                ]
            });
        }, true);

    $scope.$watch(
        function () { return document.getElementById('summernoteSurcharge').innerHTML },
        function(newval, oldval){
            $('#summernoteSurcharge').summernote({
                height: 200   ,
                toolbar: [
                    //[groupname, [button list]]

                    ['style', ['bold', 'italic', 'underline', 'clear']],
                    ['font', ['strikethrough']],
                    ['fontsize', ['fontsize']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['height', ['height']],
                ]
            });
        }, true);

    $scope.$watch(
        function () { return document.getElementById('summernoteDirection').innerHTML },
        function(newval, oldval){
            $('#summernoteDirection').summernote({
                height: 200    ,
                toolbar: [
                    //[groupname, [button list]]

                    ['style', ['bold', 'italic', 'underline', 'clear']],
                    ['font', ['strikethrough']],
                    ['fontsize', ['fontsize']],
                    ['color', ['color']],
                    ['para', ['ul', 'ol', 'paragraph']],
                    ['height', ['height']],
                ]
            });
        }, true);


});

