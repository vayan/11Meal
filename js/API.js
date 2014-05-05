var API = {
    url : "http://localhost:8181/",
    
    get : function (objClass, column, id) {
	var geturl = this.url + objClass;

	if (column != null)
	    geturl = geturl + "/" + column;
	if (id != null)
	    geturl = geturl + "/" + id;
	var res = $.getJSON(geturl);//.done(function( data ) {this.res = data;return res});
	console.log(res);
	return res;
    },
    update : function (object) {return true},
    remove : function (objClass, id) {
	var deleteUrl = this.url + objClass + "/" + id;
	var res = $.ajax({
	    type : "DELETE",
	    url : deleteUrl
	})
	return res;
    },
    create : function (object) {return true},
    authenticate : function (login, password) 
    {
	//TODO: if server return true, change 1234 to real uuid
	createCookie("session", 1234, 100);
	return true;
    }
}

function User () {
    this.id = 0;
    this.type = "user";
    this.firstName = "";
    this.lastName = "";
    this.email = "";
    this.phone = "";
    this.login = "";
}

function Reservation (tmp) {
    this.id = tmp.Id;
    this.type = "reservation";
    this.date = date.now();
    this.state = ReservationState.OPENED;
    this.isFinished = tmp.Is_finished;
    this.payMethod = PayMethod.CASH;

    this.changeState = function (newState) {
	this.state = newState;
	
	//TODO: update reservation in API
	API.update(this);
    };
    //cannot be called delete, it already exist in JS
    this.remove = function () {
	//TODO: delete reservation in API
	API.delete(this);
    };
    
}

function Restaurant (tmp) {
    this.id = tmp.Id;
    this.type = "restaurant";
    this.address = tmp.Address;
    this.position = tmp.Position;
    this.name = tmp.Name;
    this.timetable = "";
    this.phone = "";

    this.update = function () {
	//TODO: update into API
    };
    this.remove = function () {
	//TODO: remove into API
    };
}

function Meal (tmp) {
    this.id = tmp.Id;
    this.type = "Meal";
    this.restaurant = tmp.Restaurant;
    this.name = tmp.Name;
    this.price = tmp.Price;
    this.cat = FoodCat.UNDEFINED;

    this.update = function () {
	//TODO: update into API
    };
    this.remove = function () {
	//TODO: remove into API
    };
}

function Order (tmp) {
    this.id = tmp.Id;
    this.type = "Order";
    this.totalPrice = tmp.Total_price;
    this.ser = tmp.User;
    this.reservation = tmp.Reservation;
    this.meals = tmp.Meals;
    //TODO:update orders ? addMeal ? deleteMeal ?

    this.getTotalPrice = function () {
	return 12;
    }
}



    // usefull fonctions, they should be in another file (><)

    var ReservationState = {
	OPENED : 0,
	CONFIRMED : 1,
	CANCELED : 2
    }

    var PayMethod = {
	CASH : 0,
	CARD : 1,
	CHECK : 2
    }

    var FoodCat = {
	STARTER : 0,
	MAIN : 1,
	DESSERT : 2,
	DRINK : 3,
	SOUP : 4,
	UNDEFINED : 5
    }

    function createCookie(name,value,days) {
	if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
    }
    function readCookie(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1,c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
    }
    function eraseCookie(name) {
	createCookie(name,"",-1);
    }
    function genUuid() {
	return ('xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
	    return v.toString(16);
	}));
    }
