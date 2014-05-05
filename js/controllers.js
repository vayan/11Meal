var signinController = angular.module('signinController', []);
var rListController = angular.module('rListController', []);

signinController.controller('signinCtrl', function ($scope, $http) {
    
    $http.get('data/signIn.json').success(function(data) {
	$scope.langs = data;
   console.log($scope.langs);

    });

    
});

signinController.controller('signinController', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.phoneId = $routeParams.phoneId;
}]);

//

rListController.controller('rListCtrl', function ($scope, $http) {
    console.log("toto");
    $http({method: 'GET', url: 'http://localhost:8181/restaurant'}).success(function(data) {
//    $http.get('http://localhost:8181/restaurant').success(function(data) {
	$scope.resList = data;
	console.log($scope.resList);
    });
});


rListController.controller('rListController', ['$scope', '$routeParams', function($scope, $routeParams) {
    $scope.phoneId = $routeParams.phoneId;
}]);
