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

    if ($routeParams.objClass == "reservation" 
	|| $routeParams.objClass == "meal"
	|| $routeParams.objClass == "promo"
	|| ($routeParams.objClass == "order" 
	    && $routeParams.column == "null")) {
	column = "Restaurant";
	value = id;
    }

    API.get($routeParams.objClass, column, value).done(function(data) {
	var arraytmp = [];
	$scope.resState = ReservationStateString; 
	$scope.payMethod = PayMethodString; 
	$scope.foodCat = FoodCatString; 
	$scope.showTime = utils.showTime;
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
	    else if ($routeParams.objClass == "promo") {
		console.log($routeParams.objClass);
		console.log(value);
		arraytmp[key] = new Promo(value);
	    }
	});
	$scope.List = arraytmp;
	$scope.$apply()
	$scope.colorState();
    });
    $scope.Type = $routeParams.objClass;
    console.log($scope.Type);


    $scope.deleteIt = function (type, id) {
	location.assign('#/delete/'+type+'/'+id);
    }

    $scope.showRelated = function (objClass, type, item) {
	if (item.state != ReservationState.CANCELED 
	    && item.state != ReservationState.OPENED)
	location.assign('#/list/'+objClass+'/'+type+'/'+item.id);
    }

    $scope.updateState = function (item, state) {
        var obj = {};
        obj["User"] = item.user;
	obj["Restaurant"] = item.restaurant;
	obj["Date"] = item.date;
	obj["State"] = parseInt(state);
	obj["PayementMethod"] = item.payMethod;
        API.update("reservation", obj, item.id).done(function(data) {
	    //location.reload();
	});
    }

    $scope.colorState = function (state) {
	$scope.CANCELED = "danger";
	$scope.CONFIRMED = "warning";
	$scope.QUEUED = "info";
	$scope.DONE = "success";
	return $scope.resState[state];
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
	    location.assign("index.html#/list/"+$routeParams.objClass+"/null/null");
	});
    }

    $scope.createIt = function (name, price, cat) {
	location.assign('#/create/meal/'+name+'/'+price+'/'+cat);
    }

    $scope.createPromo = function (promo) {
        console.log("in create promo");
        var obj = {};
        obj["Name"] = promo.name;
        obj["Description"] = promo.description;
        obj["P100_reduc"] = parseInt(promo.discount);
        obj["Restaurant"] =  parseInt(utils.readCookie("session"));

        API.create($routeParams.objClass, obj).done(function(data) {
	    location.assign("index.html#/list/"+$routeParams.objClass+"/null/null");
	});
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


var statisticsController = angular.module('statisticsController', []);

statisticsController.controller('statisticsController', ['$scope', '$routeParams', function($scope, $ro1uteParams) {

    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
    });

    API.get("restaurant/"+parseInt(utils.readCookie("session"))+"/meal/top", null, null).done(function(data) {
	$scope.foodCat = FoodCatString; 
	var arraytmp = [];
	angular.forEach(data, function(value, key){
	    if (value.Id != "0")
		arraytmp[key] = new Meal(value);
	});
	$scope.List = arraytmp;
	$scope.$apply();
    });

}]);


var restaurantDetailsController = angular.module('restaurantDetailsController',[]);

restaurantDetailsController.controller('restaurantDetailsController', ['$scope', '$routeParams', function($scope, $routeParams) {
    var id = utils.readCookie("session");

    // do this to show the menu on the left
    showMenu().done(function(data) {
	$scope.leftMenu = data;
	$scope.$apply();
    });

    API.get("restaurant", "Id", id).done(function(data) {
	var arraytmp = [];

	angular.forEach(data, function(value, key){
	    arraytmp[key] = new Restaurant(value);
	});
	console.log(arraytmp[0].name);
	$scope.List = arraytmp;
	$scope.restaurant.name = arraytmp[0].name;
	$scope.restaurant.address = arraytmp[0].address;
	$scope.restaurant.position = arraytmp[0].position;
	$scope.restaurant.schedule = arraytmp[0].schedule;
	$scope.restaurant.phone = arraytmp[0].phone;
	$scope.restaurant.description = arraytmp[0].description;
	$scope.$apply();
    });
    console.log($scope.Type);

    $scope.updateIt = function (restaurant) {
        console.log("in create resto");
        var obj = {};
        obj["Name"] = restaurant.name;
        obj["Address"] = restaurant.address;
        obj["Position"] = restaurant.position ;
        obj["Schedule"] = restaurant.schedule;
        obj["Phone"] = restaurant.phone;
        obj["Description"] = restaurant.description;

        API.update("restaurant", obj, id).done(function(data) {
	    location.reload();
	});
    }

}]);

// AFF MENU ON THE LEFT

function showMenu() {
    return $.get( "partials/leftMenu.html");
}
