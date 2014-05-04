var signinController = angular.module('signinController', []);

signinController.controller('signinCtrl', function ($scope, $http) {
    
    $http.get('data/signIn.json').success(function(data) {
	$scope.langs = data;
   console.log($scope.langs);

    });

    
});

signinController.controller('signinController', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.phoneId = $routeParams.phoneId;
}]);
