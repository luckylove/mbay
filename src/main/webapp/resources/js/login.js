/**
 * Created with IntelliJ IDEA.
 * User: Nguyen
 * Date: 9/20/14
 * Time: 12:25 PM
 * To change this template use File | Settings | File Templates.
 */
angular.module('cruiseApp', []).controller('LoginCtrl', function ($scope) {
    $scope.focusInput = true;
    $scope.register = {};
    $scope.showLogin = function(){
        $scope.logForm = false;
        $scope.regForm = true;
    }

    $scope.showRegister = function(){
        $scope.logForm = true;
        $scope.regForm = false;
    }
    $scope.selectTaxi = function($event, id, name){
        $scope.register.taxiId = id;
        $($event.target).parents('.btn-group').find('.dropdown-toggle').html(name+' <span class="caret"></span>');
    }

    //$scope.showLogin();
}).directive('focusMe', function($timeout, $parse) {
    return {
        //scope: true,   // optionally create a child scope
        link: function(scope, element, attrs) {
            var model = $parse(attrs.focusMe);
            scope.$watch(model, function(value) {
                //console.log('value=',value);
                if(value === true) {
                    $timeout(function() {
                        element[0].focus();
                    });
                }
            });
            // to address @blesh's comment, set attribute value to 'false'
            // on blur event:
            element.bind('blur', function() {
                //console.log('blur');
                scope.$apply(model.assign(scope, false));
            });
        }
    };
});;

