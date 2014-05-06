var restaurantApp = angular.module('restaurantApp', [
    'ngRoute',
    'signinCtrl',
    'rListCtrl',
    'deleteController',
    'createFormController',
    'createController'
]);

restaurantApp.config(['$routeProvider',function($routeProvider) {
    console.log($routeProvider);
    $routeProvider.
	when('/signin', {
            templateUrl: 'partials/signIn.html',
            controller: 'signinCtrl'
	}).
	when('/list/:objClass', {
            templateUrl: 'partials/list.html',
            controller: 'rListCtrl'
	}).
	when('/delete/:objClass/:id', {
            templateUrl: 'partials/delete.html',
            controller: 'deleteController'
	}).
	when('/create/:objClass', {
            templateUrl: 'partials/createForm.html',
            controller: 'createFormController'
	}).
	when('/create/:objClass/:param1/:param2/:param3', {
            templateUrl: 'partials/create.html',
            controller: 'createController'
	}).
	otherwise({
            redirectTo: '/'
	});
}]);
