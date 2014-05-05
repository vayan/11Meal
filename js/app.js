var restaurantApp = angular.module('restaurantApp', [
    'ngRoute',
    'signinController',
    'rListController'
]);

restaurantApp.config(['$routeProvider',
  function($routeProvider) {
      console.log($routeProvider);
    $routeProvider.
      when('/signin', {
        templateUrl: 'partials/signIn.html',
        controller: 'signinController'
      }).
      when('/reservationList', {
        templateUrl: 'partials/reservationList.html',
        controller: 'rListController'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);
