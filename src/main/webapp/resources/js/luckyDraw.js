/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
cruiseApp.controller('UserGroupCtrl', function ($scope, $modal, $http) {
    $scope.submit = function (form) {
        if(form.$valid) {
            $http({
                method: "post",
                url: 'addLuckyDraw.json',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                data: $.param($scope.userForm)
            }).success(function(data, status, headers, config) {
                    if(!data.success){
                        bootbox.alert(data.errorMsg);
                    } else {
                        $scope.userForm.name = "";
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
        $scope.userForm = {};
    }

    $scope.$watch(
        function () { return document.getElementById('datetimepicker10').innerHTML },
        function(newval, oldval){
            $('#datetimepicker10').datetimepicker({
                showToday: true
            });
            $("#datetimepicker10").on("dp.change",function (e) {
                $scope.userForm.triggerTime = e.date.format('YYYY/MM/DD HH:mm');
            });
        }, true);

});

window.operateEvents = {
    'click .remove': function (e, value, row, index) {
        bootbox.confirm("Are  you sure you want to remove it", function(result) {
            if(result) {
                $.ajax({
                    type: "GET",
                    url: 'deleteLuckyDraw.json',
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


function operateFormatter(value, row, index) {
    return [

        '<a class="remove ml10" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>   '
    ].join('');
}


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

