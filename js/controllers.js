var signinCtrl = angular.module('signinCtrl', []);

signinCtrl.controller('signinCtrl', ['$scope', '$http', function($scope, $http) {
    $http.get('data/signIn.json').success(function(data) {
	$scope.langs = data;
    });

    $scope.signIn = function (login, password) {
	console.log(login+"|"+password);
	API.authenticate(login, password);
    }

}]);

var rListCtrl = angular.module('rListCtrl', ['ngSanitize']);

rListCtrl.controller('rListCtrl', ['$scope', '$routeParams',function($scope, $routeParams) {
    var column = $routeParams.column;
    var value = $routeParams.value;
    if ($routeParams.column == "null")
	column = null;
    if ($routeParams.value == "null")
	value = null;
    var id = utils.readCookie("session");

    // do this to show the menu on the left
    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
    });

    if ($routeParams.objClass == "reservation") {
	column = "Restaurant";
	value = id;
    }

    API.get($routeParams.objClass, column, value).done(function(data) {
	var arraytmp = [];

	var arraymeals = [];
	angular.forEach(data, function(value, key){
	    if ($routeParams.objClass == "restaurant") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Restaurant(value);
	    }
	    else if ($routeParams.objClass == "meal") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Meal(value);
	    }
	    else if ($routeParams.objClass == "reservation") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Reservation(value);
	    }
	    else if ($routeParams.objClass == "order") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Order(value);
	    }
	    else if ($routeParams.objClass == "usermeal") {
		console.log($routeParams.objClass);
		console.log(value);
		arraytmp[key] = new UserMeal(value);
	    }
	});
	$scope.List = arraytmp;
	$scope.$apply();
    });
    $scope.Type = $routeParams.objClass;
    console.log($scope.Type);


    $scope.deleteIt = function (type, id) {
	location.assign('#/delete/'+type+'/'+id);
    }

    $scope.showRelated = function (objClass, type, id) {
	location.assign('#/list/'+objClass+'/'+type+'/'+id);
    }


}]);

var rDetailCtrl = angular.module('rDetailCtrl',[]);

rDetailCtrl.controller('rDetailCtrl', ['$scope', '$routeParams', function($scope, $routeParams) {
    var column = $routeParams.id;
    var value = $routeParams.id;
    var id = $routeParams.id;

    // do this to show the menu on the left
    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
    });

    if ($routeParams.objClass == "order") {
	column = "Order";
    }

    API.get($routeParams.objClass, column, id).done(function(data) {
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
	    else if ($routeParams.objClass == "reservation") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Reservation(value);
	    }
	    else if ($routeParams.objClass == "reservation") {
		console.log($routeParams.objClass);
		arraytmp[key] = new Reservation(value);
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
	location.assign("index.html#/list/"+$routeParams.objClass+"/null/null");
    });

}]);


var createController = angular.module('createController', ['ngSanitize']);

createController.controller('createFormController', ['$scope', '$routeParams', function($scope, $routeParams){
    // do this to show the menu on the left
    $scope.Type = $routeParams.objClass;

    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
    });

    $scope.createRest = function (name, address, position, schedule, phone, description) {
        console.log("in create resto");
        var obj = {};
        obj["Name"] = name;
        obj["Address"] = address;
        obj["Position"] = position ;
        obj["Schedule"] = schedule;
        obj["Phone"] = phone;
        obj["Description"] = description;

        API.create($routeParams.objClass, obj).done(function(data) {
    location.assign("index.html#/list/"+$routeParams.objClass);
    });
    }

    $scope.createIt = function (name, price, cat) {
	location.assign('#/create/meal/'+name+'/'+price+'/'+cat);
    }

}]);


createController.controller('createController', ['$scope', '$routeParams', function($scope, $routeParams) {
    if ($routeParams.objClass == "meal") {
	var obj = {};
	obj["Name"] = $routeParams.param1;
	obj["Price"] = parseInt($routeParams.param2);  // integer.
	obj["Type"] = parseInt($routeParams.param3);
	obj["Restaurant"] = parseInt(utils.readCookie("session"));
    }
    API.create($routeParams.objClass, obj).done(function(data) {
	location.assign("index.html#/list/"+$routeParams.objClass+"/null/null");
    });
}]);


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