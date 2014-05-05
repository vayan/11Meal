var signinCtrl = angular.module('signinCtrl', []);

signinCtrl.controller('signinCtrl', ['$scope', '$http', function($scope, $http) {
    $http.get('data/signIn.json').success(function(data) {
	$scope.langs = data;
    });    
}]);

var rListCtrl = angular.module('rListCtrl', []);

rListCtrl.controller('rListCtrl', ['$scope', '$routeParams',function($scope, $routeParams) {
    API.get($routeParams.objClass, null, null).done(function(data) {
	var arraytmp = [];
	
	angular.forEach(data, function(value, key){
	    if ($routeParams.objClass == "restaurant") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Restaurant(value);
	    }
	});
	$scope.List = arraytmp;
	$scope.$apply();
    });
}]);

var deleteController = angular.module('deleteController', []);

deleteController.controller('deleteController', ['$scope', '$routeParams', function($scope, $routeParams) {
    API.remove($routeParams.objClass, $routeParams.id).done(function(data) {
	location.assign("index.html#/list/"+$routeParams.objClass);
    });
}]);
