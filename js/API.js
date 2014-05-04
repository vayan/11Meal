var API = {
    get : function (from, column, uuid) {return true;},
    update : function (object) {return true},
    delete : function (object) {return true},
    create : function (object) {return true},
    authenticate : function (login, password) 
    {
	//TODO: if server return true, change 1234 to real uuid
	createCookie("session", 1234, 100);
	return true;
    }
}

function user () {
    this.id = "";
    this.firstName = "";
    this.lastName = "";
    this.email = "";
    this.phone = "";
    this.login = "";
}

function reservation () {
    this.id = genUuid();
    this.date = Math.round(new Date().getTime() / 1000);
    this.state = ReservationState.OPENED;
    this.inviteFinished = false;
    this.payMethod = PayMethod.CASH;

    this.changeState = function (newState)
    {
	this.state = newState;

	//TODO: update reservation in API
	API.update(this);
    }

    //cannot be called delete, it already exist in JS
    this.remove = function () 
    {
	//TODO: delete reservation in API
	API.delete(this);
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
