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
    });

    API.get($routeParams.objClass, null, null).done(function(data) {
	var arraytmp = [];
	
	angular.forEach(data, function(value, key){
	    if ($routeParams.objClass == "restaurant") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Restaurant(value);
	    }
	    else if ($routeParams.objClass == "meal") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Meal(value);	
	    }
	});
	$scope.List = arraytmp;
	$scope.$apply();
    });
	$scope.Type = $routeParams.objClass;
	console.log($scope.Type);
}]);

var deleteController = angular.module('deleteController', []);

deleteController.controller('deleteController', ['$scope', '$routeParams', function($scope, $routeParams) {
    API.remove($routeParams.objClass, $routeParams.id).done(function(data) {
	location.assign("index.html#/list/"+$routeParams.objClass);
    });
}]);


var createFormController = angular.module('createFormController', ['ngSanitize']);

createFormController.controller('createFormController', ['$scope', '$routeParams', function($scope, $routeParams){
    // do this to show the menu on the left
    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
    });
    
    $scope.createIt = function (name, price, cat) {
	location.assign('index.html#/create/meal/'+name+'/'+price+'/'+cat);
    }

}]);

var createController = angular.module('createController', []);

createController.controller('createController', ['$scope', '$routeParams', function($scope, $routeParams) {
    
}]);

//added by Seven
var restaurantDetailsController = angular.module('restaurantDetailsController',[]);

restaurantDetailsController.controller('restaurantDetailsController', ['$scope', '$routeParams', function($scope, $routeParams) {
    // do this to show the menu on the left
    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
    });
}]);


// AFF MENU ON THE LEFT

function showMenu() {
    return $.get( "partials/leftMenu.html");
}
