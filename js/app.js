var restaurantApp = angular.module('restaurantApp', [
  'ngRoute',
  'signinController'
]);

restaurantApp.config(['$routeProvider',
  function($routeProvider) {
      console.log($routeProvider);
    $routeProvider.
      when('/signin', {
        templateUrl: 'partials/signIn.html',
        controller: 'signinController'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);
