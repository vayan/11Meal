var restaurantApp = angular.module('restaurantApp', [
    'ngRoute',
    'signinCtrl',
    'rListCtrl',
    'deleteController'
]);

restaurantApp.config(['$routeProvider',
  function($routeProvider) {
      console.log($routeProvider);
    $routeProvider.
      when('/signin', {
        templateUrl: 'partials/signIn.html',
        controller: 'signinCtrl'
      }).
      when('/list/:objClass', {
        templateUrl: 'partials/reservationList.html',
        controller: 'rListCtrl'
      }).
      when('/delete/:objClass/:id', {
        templateUrl: 'partials/delete.html',
        controller: 'deleteController'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);
