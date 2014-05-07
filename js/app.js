var restaurantApp = angular.module('restaurantApp', [
    'ngRoute',
    'signinCtrl',
    'rListCtrl',
    'rDetailCtrl',
    'deleteController',
    'createController',
    'restaurantDetailsController'
]);

restaurantApp.config(['$routeProvider',function($routeProvider) {
    console.log($routeProvider);
    $routeProvider.
	when('/signin', {
            templateUrl: 'partials/signIn.html',
            controller: 'signinCtrl'
	}).
	when('/list/:objClass/:column/:value', {
            templateUrl: 'partials/list.html',
            controller: 'rListCtrl'
	}).
	when('/detail/:objClass/:id', {
            templateUrl: 'partials/detail.html',
            controller: 'rDetailCtrl'
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
	when('/restaurant', {
            templateUrl: 'partials/restaurantDetails.html',
            controller: 'restaurantDetailsController'
	}).
	otherwise({
            redirectTo: '/'
	});
}]);
