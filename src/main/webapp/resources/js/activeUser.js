/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */

var listURL = "listActiveUser.json";
var addModelURL = "addActiveUser.json";
var deleteModelURL = "deleteCruise.json";

cruiseApp.controller('ActiveCtrl', function ($scope, $modal, $log, $http) {

    //$scope.userForm.isUpdate = $scope.userForm.id > 0;
    $scope.getActiveUser = function(val) {
        return $http.get('listUserByLicense.json', {
            params: {
                license: val
            }
        }).then(function(response){
            return response.data;
        });
    };

    $scope.submit = function (form) {
        if(!$scope.asyncSelected || typeof $scope.asyncSelected === 'string') {
            bootbox.alert("User not found");
            return;
        }
        if(form.$valid) {
            $http({
                method: "post",
                url: addModelURL,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                params: {
                    userId: $scope.asyncSelected.id
                }
            }).success(function(data, status, headers, config) {
                if(!data.success){
                    bootbox.alert(data.errorMsg);
                } else {
                    $scope.asyncSelected = null;
                    $('#userGroupTable').bootstrapTable('refresh');
                }
            }).
            error(function(data, status, headers, config) {
                alert("have error")
            });
        } else {
            alert("Please input required field");
        }
    };

    $scope.clear = function() {
        $scope.asyncSelected = "";
    }

    $scope.delete = function () {
        var selected = $('#userGroupTable').bootstrapTable('getSelections');
        var ids = [];
        if(selected && selected.length >0) {
            $.each(selected, function(idx, row){
                ids.push(row.id);
            })
        }
        if(ids.length > 0) {
            bootbox.confirm("Are  you sure you want to remove it", function(result) {
                if(result) {
                    $.ajax({
                        type: "GET",
                        url: deleteModelURL,
                        data: {
                            ids: ids.join(',')
                        },
                        dataType: 'json'
                    })
                    .done(function(data) {
                        $('#userGroupTable').bootstrapTable('refresh');
                    });
                }
            });
        }
    };
});



function operateFormatter(value, row, index) {
    return [
        '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>'
    ].join('');
}

window.operateEvents = {
    'click .remove': function (e, value, row, index) {
        bootbox.confirm("Are  you sure you want to remove it", function(result) {
            if(result) {
                $.ajax({
                    type: "GET",
                    url: deleteModelURL,
                    data: {
                        ids: row.id
                    },
                    dataType: 'json'
                })
                .done(function(data) {
                    $('#userGroupTable').bootstrapTable('refresh');
                });
            }
        });
    }
};

function queryParams(params) {
    return {
        limit: params.pageSize,
        offset: params.pageSize * (params.pageNumber - 1),
        search: params.searchText,
        name: params.sortName,
        order: params.sortOrder
    };
}

$(function () {
    setTimeout(function(){$('#userGroupTable').bootstrapTable({
        url: listURL
    })}, 100);
});
