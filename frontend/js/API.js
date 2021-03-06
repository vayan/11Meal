var API = {
    url : "http://localhost:8181/",
    id : null,
    get : function (objClass, column, id) {

	console.log(objClass+"|"+column+"|"+id);

	var geturl = this.url + objClass;

	if (column != null)
	    geturl = geturl + "/" + column;
	if (id != null)
	    geturl = geturl + "/" + id;
	console.log(geturl);
	var res = $.getJSON(geturl);//.done(function( data ) {this.res = data;return res});
	console.log(res);
	return res;
    },
    update : function (objClass, obj, id) {
    	var updateUrl = this.url+objClass+'/'+id;
	console.log(">>>>"+updateUrl);
	console.log(JSON.stringify(obj));
	var res = $.ajax({
	    type : "PUT",
	    url : updateUrl,
	    data : JSON.stringify(obj)
	});
	return res;
    },
    remove : function (objClass, id) {
	var deleteUrl = this.url + objClass + "/" + id;
	var res = $.ajax({
	    type : "DELETE",
	    url : deleteUrl
	});
	return res;
    },
    create : function (objClass, obj) {
	var createUrl = this.url + objClass;
	console.log(createUrl);
	console.log(JSON.stringify(obj));
	var res = $.post(createUrl, JSON.stringify(obj),function( data ) {}, "json");
	return res;
    },
    authenticate : function (login, password) {
	//TODO: if server return true, change 1234 to real uuid
	API.get("restaurant","Id",password).done(function (data){
	    console.log(data[0].Name);
	    if (data[0].Name == login) {   
		utils.createCookie("session",data[0].Id, 100);
		this.id = data[0].Id;
		console.log(this.id);
		location.assign("index.html#/");
	    }
	    return true;
	});
	return true;
    }
}

function User (tmp) {
    this.id = tmp.Id;
    this.type = "user";
    this.firstName = tmp.First_name;
    this.lastName = tmp.Last_name;
    this.email = tmp.Email;
    this.phone = tmp.Phone;
    this.phoneUID = tmp.PhoneUID;
    this.login = tmp.Login;
}

function Reservation (tmp) {
    this.id = tmp.Id;
    this.type = "reservation";
    this.user = tmp.User;
    this.restaurant = tmp.Restaurant;
    this.date = tmp.Date;
    this.state = tmp.State;
    this.payMethod = tmp.PayementMethod;

    this.changeState = function (newState) {
	this.state = newState;
	
	//TODO: update reservation in API
	API.update(this);
    };
}

function Restaurant (tmp) {
    this.id = tmp.Id;
    this.type = "restaurant";
    this.address = tmp.Address;
    this.position = tmp.Position;
    this.name = tmp.Name;
    this.schedule = tmp.Schedule;
    this.phone = tmp.Phone;
    this.description = tmp.Description;
}

function Meal (tmp) {
    this.id = tmp.Id;
    this.type = "meal";
    this.restaurant = tmp.Restaurant;
    this.name = tmp.Name;
    this.price = tmp.Price;
    this.cat = tmp.Type;
}

function Order (tmp) {
    this.id = tmp.Id;
    this.type = "order";
    this.totalPrice = tmp.Total_price;
    this.user = tmp.User;
    this.reservation = tmp.Reservation;
    this.meals = tmp.Meals;
}

function Promo (tmp) {
    this.id = tmp.Id;
    this.type = "promo";
    this.name = tmp.Name;
    this.description = tmp.Description;
    this.discount = tmp.P100_reduc;
    this.restaurant = tmp.Restaurant;
}

function UserMeal (tmp) {
    this.id = tmp.Id;
    this.type = "meal";
    this.order = tmp.Order;
    this.meal = tmp.Meal;
    this.restaurant = tmp.Restaurant;
    this.name = tmp.Name;
    this.price = tmp.Price;
    this.quantity = tmp.Quantity;
}


// usefull fonctions, they should be in another file (><)

var ReservationState = {
    OPENED : 0,
    CONFIRMED : 1,
    QUEUED : 2,
    DONE : 3,
    CANCELED : 4
}

var PayMethod = {
    CASH : 0,
    CHECK : 1,
    CARD : 2
    }

var FoodCat = {
    STARTER : 0,
    MAIN : 1,
    DESSERT : 2,
    DRINK : 3,
    SOUP : 4,
    UNDEFINED : 5
}

var ReservationStateString = [
    "OPENED",
    "CONFIRMED",
    "QUEUED",
    "DONE",
    "CANCELED"
];

var PayMethodString = [
    "CASH",
    "CHECK",
    "CARD"
];

var FoodCatString = [
    "STARTER",
    "MAIN",
    "DESSERT",
    "DRINK",
    "SOUP",
    "UNDEFINED"
];

var utils = {
    createCookie : function(name,value,days) {
	if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            var expires = "; expires="+date.toGMTString();
	}
	else var expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
    },
    readCookie : function(name) {
	var nameEQ = name + "=";
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
            var c = ca[i];
            while (c.charAt(0)==' ') c = c.substring(1,c.length);
            if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
	}
	return null;
    },
    eraseCookie : function(name) {
	createCookie(name,"",-1);
    },
    genUuid : function() {
	return ('xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	    var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
	    return v.toString(16);
	}));
    },
    showTime : function(timestamp) {
	timestamp = new Date(parseInt(timestamp)*1000);
	var weekday = new Array(7);
	weekday[0]=  "Sunday";
	weekday[1] = "Monday";
	weekday[2] = "Tuesday";
	weekday[3] = "Wednesday";
	weekday[4] = "Thursday";
	weekday[5] = "Friday";
	weekday[6] = "Saturday";

	var timeString = timestamp.getMonth()+"/"+timestamp.getDate()+"/"+timestamp.getFullYear()+" "+timestamp.getHours()+":"+timestamp.getMinutes();
	return timeString;
    }
}
