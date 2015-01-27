/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
cruiseApp.controller('UserGroupCtrl', function ($scope, $modal, $http) {
    $scope.userForm = {numberUser: 1};
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

});

window.operateEvents = {

    'click .detail': function (e, value, row, index) {
        document.location.href = "luckydraw.html?id=" + row.id
    }
};


function operateFormatter(value, row, index) {
    return [

      '<a class="detail ml10" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-arrow-right"></i>',
        '</a>'
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
    if(row.userIds) {
        return {
            classes: 'success'
        };
    }
    return {
        classes: 'active'
    };
}

