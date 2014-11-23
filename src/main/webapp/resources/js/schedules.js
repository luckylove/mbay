/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
var listURL = "listSchedules.json";
var listDashboardURL = "listDashboard.json";
var addModelURL = "addSchedules.json";
var deleteModelURL = "deleteSchedules.json";

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

    $scope.clear = function($event, id, name){
        $scope.departureTime = null;
        $event.stopPropagation();
        $event.preventDefault();
        $('#userGroupTable').bootstrapTable('refresh');
    }
    $scope.filterItemSelect = function($event, id, name){
        $scope.filterId = id;
        $($event.target).parents('.btn-group').find('.dropdown-toggle').html(name+' <span class="caret"></span>');
        //trigger refesh table
        $('#userGroupTable').bootstrapTable('refresh');
    }

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
                        url: deleteModelURL,
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

    $scope.filterByDate = function(chooseDate){
        var dd = chooseDate.format("YYYY/MM/DD");
        if( $scope.departureTime !== dd) {
            $scope.departureTime = dd;
            $('#userGroupTable').bootstrapTable('refresh');
        }
    }
    $scope.$watch(
        function () { return document.getElementById('datetimeSchedule').innerHTML },
        function(newval, oldval){
            $('#datetimeSchedule').datetimepicker({
                showToday: true
            });
            $("#datetimeSchedule").on("dp.change",function (e) {
                $scope.filterByDate(e.date);
            });
        }, true);

    $scope.todayCnt = 0;
    $scope.tomorrowCnt = 0;
    $scope.nextCnt = 0;
    $scope.increaseToday=function(){
        $scope.todayCnt += 1;
    }
    $scope.increaseTomorrow=function(){
        $scope.tomorrowCnt += 1;
    }
    $scope.increaseNext=function(){
        $scope.nextCnt += 1;
    }
});

var ModalInstanceCtrl = function ($scope, $modalInstance, $http, userForm) {

    $scope.userForm = userForm || {};
    $scope.userForm.isUpdate = $scope.userForm.id > 0;

    if(!$scope.userForm.cruiseId) {
        $scope.userForm.cruiseName = 'Select cruise';
        $scope.userForm.passengers = 0;
        $scope.userForm.callType = 'Transit';
    }

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
    $scope.itemSelect = function($event, id, name, passenger){
        $scope.userForm.cruiseId = id;
        $scope.userForm.passengers = passenger;
        $($event.target).parents('.btn-group').find('.dropdown-toggle').html(name+' <span class="caret"></span>');
    }

    $scope.callTypeSelect = function($event, id, name){
        $scope.userForm.callType = id;
        $($event.target).parents('.btn-group').find('.dropdown-toggle').html(name+' <span class="caret"></span>');
    }

    $scope.submit = function (form) {
        if(form.$valid) {
            if(!$scope.userForm.cruiseId){
                bootbox.alert('Please select crusie name');
                return ;
            }
            $http({
                method: "post",
                url: addModelURL,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param($scope.userForm)
            }).success(function(data, status, headers, config) {
                if(!data.success){
                    bootbox.alert(data.errorMsg);
                } else {
                    $modalInstance.close();
                    $('#dashboardTable').bootstrapTable('refresh');
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

    $scope.$watch(
        function () { return document.getElementById('datetimepicker9').innerHTML },
        function(newval, oldval){
            $('#datetimepicker9').datetimepicker({
                minDate: moment(),
                showToday: true
            });
            $("#datetimepicker9").on("dp.change",function (e) {
                $('#datetimepicker10').data("DateTimePicker").setMinDate(e.date);
                $scope.userForm.arrivalTime = e.date.format('YYYY/MM/DD HH:mm');
            });
    }, true);

    $scope.$watch(
        function () { return document.getElementById('datetimepicker10').innerHTML },
        function(newval, oldval){
            $('#datetimepicker10').datetimepicker({
                showToday: true
            });
            $("#datetimepicker10").on("dp.change",function (e) {
                $scope.userForm.departureTime = e.date.format('YYYY/MM/DD HH:mm');
                $('#datetimepicker9').data("DateTimePicker").setMaxDate(e.date);
            });
    }, true);

};

function operateFormatter(value, row, index) {
    return [
        '<a class="edit" href="javascript:void(0)" title="Edit">',
        '<i class="glyphicon glyphicon-edit" ></i>',
        '</a>',
        ' <a class="remove ml10" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>'
    ].join('');
}

function iconFormatter(value, row, index) {
    if(role === 'QC' || row.today){
        return [
            '<span class="number">' + value + '</span>',
            '     <a class="decrease ml10" href="javascript:void(0)" title="Decrease">',
            '<i class="glyphicon glyphicon-minus"></i>',
            '</a>' ,
            ' <a class="increase pdr-10" href="javascript:void(0)" title="Increase">',
            '<i class="glyphicon glyphicon-plus"></i>',
            '</a>'
        ].join('');
    }
}

window.taxiOnQEvents = {

    'click .increase': function (e, value, row, index) {
        $.ajax({
            type: "GET",
            url: 'addTaxiOnQueue.json',
            data: {
                id: row.id,
                type:'increase'
            },
            dataType: 'json'
        })
            .done(function(data) {
                $('#userGroupTable').find("tr[data-index=" + index + "] td:eq(6) span").html(data.result);
            });
    },
    'click .decrease': function (e, value, row, index) {
        $.ajax({
            type: "GET",
            url: 'addTaxiOnQueue.json',
            data: {
                id: row.id  ,
                type:'decrease'
            },
            dataType: 'json'
        })
            .done(function(data) {
                $('#userGroupTable').find("tr[data-index=" + index + "] td:eq(6) span").html(data.result);
            });
    }
};

window.passOnQEvents = {

    'click .increase': function (e, value, row, index) {
        $.ajax({
            type: "GET",
            url: 'addPassOnQueue.json',
            data: {
                id: row.id,
                type:'increase'
            },
            dataType: 'json'
        })
            .done(function(data) {
                $('#userGroupTable').find("tr[data-index=" + index + "] td:eq(7) span").html(data.result);
            });
    },
    'click .decrease': function (e, value, row, index) {
        $.ajax({
            type: "GET",
            url: 'addPassOnQueue.json',
            data: {
                id: row.id  ,
                type:'decrease'
            },
            dataType: 'json'
        })
            .done(function(data) {
                $('#userGroupTable').find("tr[data-index=" + index + "] td:eq(7) span").html(data.result);
            });
    }
};

window.operateEvents = {
    'click .edit': function (e, value, row, index) {
        var $scope = angular.element('[ng-controller=UserGroupCtrl]').scope();
        var record = $.extend(row, {
            arrivalTime: row.arrivalTimeStr,
            departureTime: row.departureTimeStr
        });
        delete record['regDate'];
        delete record['modDate'];
        $scope.open(null, record);
    } ,

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
                })
                .fail(function() {

                });
            }
        });
    } ,
    'click .increase': function (e, value, row, index) {
        $.ajax({
            type: "GET",
            url: 'addTaxiOnQueue.json',
            data: {
                id: row.id,
                type:'increase'
            },
            dataType: 'json'
        })
            .done(function(data) {
                $('#userGroupTable').find("tr[data-index=" + index + "] td:eq(6)").html(data.result);
            });
    },
    'click .decrease': function (e, value, row, index) {
        $.ajax({
            type: "GET",
            url: 'addTaxiOnQueue.json',
            data: {
                id: row.id  ,
                type:'decrease'
            },
            dataType: 'json'
        })
        .done(function(data) {
                $('#userGroupTable').find("tr[data-index=" + index + "] td:eq(6)").html(data.result);
        });
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
        filterId: scope.filterId,
        departureTime: scope.departureTime
    };
}

function rowStyle(row, index) {

    if (index % 2 === 0) {
        return {
            classes: 'active'
        };
    }
    return {};
}

function rowBoardStyle(row, index) {
    var spDate = moment(row.departureTimeStr, "YYYY/MM/DD H:mm:ss");
    var $scope = angular.element('[ng-controller=UserGroupCtrl]').scope();
    if(moment().diff(spDate, 'days') == 0){
        $scope.$apply(function() {
            $scope.increaseToday();
        });
        return {
            classes: 'danger'
        };
    } else if(spDate.diff(moment(), 'days') == 1){
        $scope.$apply(function() {
            $scope.increaseTomorrow();
        });
        return {
            classes: 'success'
        };
    } else {
        $scope.$apply(function() {
            $scope.increaseNext();
        });
        return {
            classes: 'info'
        };
    }
    return {};
}

$(function () {
    setTimeout(function(){
        $('#dashboardTable').bootstrapTable({
            url: listDashboardURL
        });
        $('#userGroupTable').bootstrapTable({
            url: listURL
        })
    }, 100);

});
