/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
cruiseApp.controller('UserGroupCtrl', function ($scope, $modal, $log) {
    $scope.open = function (size, userForm) {
        var modalInstance = $modal.open({
            templateUrl: 'addUserGroup.html',
            controller: ModalInstanceCtrl,
            size: size,
            backdrop: 'static',
            windowClass: 'modal-top',
            resolve: {
                userForm: function () {
                    return userForm;
                }
            }
        });
    };

});

var ModalInstanceCtrl = function ($scope, $modalInstance, $http, userForm) {

    $scope.userForm = userForm || {};
    $scope.userForm.isUpdate = $scope.userForm.id > 0;
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.submit = function (form) {
        if(form.$valid) {
            $http({
                method: "post",
                url: "addUserGroup.json",
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param($scope.userForm)
            }).success(function(data, status, headers, config) {
                    console.log(data);
                    if(!data.success){
                        bootbox.alert(data.errorMsg);
                    } else {
                        $modalInstance.close();
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
                        url: 'deleteUserGroup.json',
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
};

function operateFormatter(value, row, index) {
    return [
        '<a class="edit pdr-10" href="javascript:void(0)" title="Edit">',
        '<i class="glyphicon glyphicon-edit" ></i>',
        '</a>',
        '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>'
    ].join('');
}

window.operateEvents = {
    'click .edit': function (e, value, row, index) {
        delete row.regDate;
        delete row.modDate;
        var scope = angular.element('[ng-controller=UserGroupCtrl]').scope();
        scope.open(null, row);
    },
    'click .remove': function (e, value, row, index) {
        bootbox.confirm("Are  you sure you want to remove it", function(result) {
            if(result) {
                $.ajax({
                    type: "GET",
                    url: 'deleteUserGroup.json',
                    data: {
                        ids: row.id
                    },
                    dataType: 'json'
                })
                .done(function(data) {
                    $('#userGroupTable').bootstrapTable('refresh');
                })
                .fail(function() {

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
        url: 'listUserGroup.json'
    })}, 100);
});
