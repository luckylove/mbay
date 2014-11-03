/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
cruiseApp.controller('UserGroupCtrl', function ($scope, $modal, $http) {
    $scope.open = function (size, userForm) {
        var modalInstance = $modal.open({
            templateUrl: 'listUser.html',
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
    $scope.openActive = function (size, userForm) {
        var modalInstance = $modal.open({
            templateUrl: 'listActiveUser.html',
            controller: ModalActiveInstanceCtrl,
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
    $scope.message = {sendType: 'NonApp'};
    $scope.selectType = function($event, id, name){
        $scope.message.sendType = id;
        $($event.target).parents('.btn-group').find('.dropdown-toggle').html(name+' <span class="caret"></span>');
    }

    $scope.selectUser = function($event, id, name){
        //$scope.message.selectedUserId = id;
        if(id == '-1'){
            //$scope.message.type = "all";
        } else {
            //$scope.message.type = "group";
            //call ajax to get list users
            $http({
                method: "get",
                url: "listUserOfGroup.json?groupId=" + id + "&userType=" +  $scope.message.sendType
            }).success(function(data, status, headers, config) {
                    $scope.addSelectedUser(data);
            });
        }
        $($event.target).parents('.btn-group').find('.dropdown-toggle').html(name+' <span class="caret"></span>');
    }
    $scope.message.selectedUserId = {};
    $scope.addSelectedUser = function(ids){
        $scope.message.type = "users";
        $.each(ids, function(idx, row){
            $('#inputUserTag').tagsinput('add',  row);
            $scope.message.selectedUserId[row.id] = row.id;
        });

    }

    $scope.send = function (form) {
        if(form.$valid) {
            if(!$scope.message.selectedUserId || $scope.message.selectedUserId == ""){
                bootbox.alert("Please select user");
                return;
            }
            $http({
                method: "post",
                url: "sendMessage.json",
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param({
                    userIds: $scope.serializeUserId(),
                    message: $scope.message.message,
                   // type: $scope.message.type,
                    sendType: $scope.message.sendType
                })
            }).success(function(data, status, headers, config) {
                    //console.log(data);
                    if(!data.success){
                        bootbox.alert(data.errorMsg);
                    } else {
                        bootbox.alert(data.result);
                        $('#userGroupTable').bootstrapTable('refresh');
                    }
                }).
                error(function(data, status, headers, config) {
                    alert("have error")
                });
        } else {
            bootbox.alert("Message is required field");
        }
    };

    $scope.delete = function () {
        var selected = $('#userGroupTable').bootstrapTable('getSelections');
        var ids = [];
        if(selected && selected.length >0) {
            $.each(selected, function(idx, row){
                if(row.role != 'ADMIN'){
                    ids.push(row.id);
                }
            })
        }
        if(ids.length > 0) {
            bootbox.confirm("Are  you sure you want to remove it", function(result) {
                if(result) {
                    $.ajax({
                        type: "GET",
                        url: 'deleteMessage.json',
                        data: {
                            ids: ids.join(',')
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

    $scope.serializeUserId = function(){
        var ids = [];
        $.each($scope.message.selectedUserId, function(key, val){
            ids.push(val);
        })
        return ids.join(",");
    }

    $('#inputUserTag').tagsinput({
        itemValue: 'id',
        itemText: 'userName'
    });

    $scope.$watch(
        function () { return document.getElementById('myTextarea').innerHTML },
        function(newval, oldval){
            $('#myTextarea').limit('168','#charsLeft');
        }, true);

});

var ModalInstanceCtrl = function ($scope, $modalInstance, $http, userForm) {
    //attach reference here
    var prSscope = angular.element('[ng-controller=UserGroupCtrl]').scope();
    prSscope.modalInstanceCtrlScope = $scope;
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.addUser = function () {
        var selected = $('#userTable').bootstrapTable('getSelections');
        if(selected && selected.length >0) {
            prSscope.addSelectedUser(selected);
            $scope.cancel();
        } else {
            bootbox.alert("Please select user");
        }
    };

    $scope.filterItemSelect = function($event, id, name){
        $scope.filterId = id;
        $($event.target).parents('.btn-group').find('.dropdown-toggle').html(name+' <span class="caret"></span>');
        //trigger refesh table
        $('#userTable').bootstrapTable('refresh');
    }
    //init table
    setTimeout(function(){$('#userTable').bootstrapTable({
        url: 'listUser.json?notInIds='+ prSscope.serializeUserId() + "&userType=" + prSscope.message.sendType
    })}, 100);


};

var ModalActiveInstanceCtrl = function ($scope, $modalInstance, $http, userForm) {
    //attach reference here
    var prSscope = angular.element('[ng-controller=UserGroupCtrl]').scope();
    prSscope.modalActiveInstanceCtrlScope = $scope;
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.addUser = function () {
        var selected = $('#userActiveTable').bootstrapTable('getSelections');
        if(selected && selected.length >0) {
            prSscope.addSelectedUser(selected);
            $scope.cancel();
        } else {
            bootbox.alert("Please select user");
        }

    };

    $scope.filter = function () {
        $('#userActiveTable').bootstrapTable('refresh');
    };


    $scope.$watch(
        function () { return document.getElementById('startDate').innerHTML },
        function(newval, oldval){
            $('#startDate').datetimepicker({
                showToday: true
            });
            $("#startDate").on("dp.change",function (e) {
                $('#endDate').data("DateTimePicker").setMinDate(e.date);
                $scope.startDate = e.date.format('YYYY/MM/DD');
            });
        }, true);

    $scope.$watch(
        function () { return document.getElementById('endDate').innerHTML },
        function(newval, oldval){
            $('#endDate').datetimepicker({
                showToday: true
            });
            $("#endDate").on("dp.change",function (e) {
                $scope.endDate = e.date.format('YYYY/MM/DD');
                $('#startDate').data("DateTimePicker").setMaxDate(e.date);
            });
        }, true);

    //init table
    setTimeout(function(){$('#userActiveTable').bootstrapTable({
        url: 'listTopActiveUser.json?notInIds='+ prSscope.serializeUserId()  + "&userType=" + prSscope.message.sendType
    })}, 100);



};

function operateFormatter(value, row, index) {
    return [

        '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>   '+
        '<a class="detail ml10" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-arrow-right"></i>',
        '</a>'
    ].join('');
}

window.operateEvents = {
    'click .remove': function (e, value, row, index) {
        bootbox.confirm("Are  you sure you want to remove it", function(result) {
            if(result) {
                $.ajax({
                    type: "GET",
                    url: 'deleteMessage.json',
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
    } ,
    'click .detail': function (e, value, row, index) {
        document.location.href = "sentUserList.html?sendId=" + row.id;
        return false;
    }
};

function queryParams(params) {
    var scope = angular.element('[ng-controller=UserGroupCtrl]').scope();
    return {
        limit: params.pageSize,
        offset: params.pageSize * (params.pageNumber - 1),
        search: params.searchText,
        name: params.sortName,
        order: params.sortOrder,
        filterId: scope.filterId
    };
}

function  queryActiveUserParams(params) {
    var activeScope = angular.element('[ng-controller=UserGroupCtrl]').scope().modalActiveInstanceCtrlScope;
    return {
        limit: params.pageSize,
        offset: params.pageSize * (params.pageNumber - 1),
        search: params.searchText,
        name: params.sortName,
        order: params.sortOrder,
        startDate: activeScope.startDate,
        endDate: activeScope.endDate
    };
}

function queryUserParams(params) {
    var scope = angular.element('[ng-controller=UserGroupCtrl]').scope().modalInstanceCtrlScope;
    return {
        limit: params.pageSize,
        offset: params.pageSize * (params.pageNumber - 1),
        name: params.sortName,
        search: params.searchText,
        order: params.sortOrder,
        filterId: scope.filterId
    };
}

function rowStyle(row, index) {
    if(row.role === 'ADMIN') {
        return {
            classes: 'danger'
        };
    }
    if (index % 2 === 0) {
        return {
            classes: 'active'
        };
    }
    return {};
}

$(function () {
    setTimeout(function(){$('#userGroupTable').bootstrapTable({
        url: 'listNotification.json'
    })}, 100);
});
