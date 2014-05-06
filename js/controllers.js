var signinCtrl = angular.module('signinCtrl', []);

signinCtrl.controller('signinCtrl', ['$scope', '$http', function($scope, $http) {
    $http.get('data/signIn.json').success(function(data) {
	$scope.langs = data;
    });    
}]);

var rListCtrl = angular.module('rListCtrl', ['ngSanitize']);

rListCtrl.controller('rListCtrl', ['$scope', '$routeParams',function($scope, $routeParams) {

// do this to show the menu on the left
    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
	console.log($scope.leftMenu);
    });

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
	console.log($scope.leftMenu);
    });

}]);

var deleteController = angular.module('deleteController', []);

deleteController.controller('deleteController', ['$scope', '$routeParams', function($scope, $routeParams) {
    API.remove($routeParams.objClass, $routeParams.id).done(function(data) {
	location.assign("index.html#/list/"+$routeParams.objClass);
    });
}]);


// AFF MENU ON THE LEFT

function showMenu() {
    return $.get( "partials/leftMenu.html");
}
