package main

import (
	"encoding/json"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"reflect"
	"strconv"
)

type Global interface {
	setID(id int) Global
	getID() int
	getAll() []Global
}

func get(r Global) Global {
	log.Println("get ", r.getID())
	obj, err := dbmap.Get(reflect.ValueOf(r).Interface(), r.getID())
	checkErr(err, "get resto failed")
	if obj == nil {
		return nil
	}
	return obj.(Global)
}

func add(r Global) {
	err := dbmap.Insert(r)
	checkErr(err, "Insert resto failed")
}

func del(r Global) {
	_, err := dbmap.Delete(r)
	checkErr(err, "Del resto failed")
}

func put(r Global) {
	_, err := dbmap.Update(r)
	checkErr(err, "Update resto failed")
}

func getObj(t string) Global {
	switch t {
	case "restos":
		return (new(Resto))
	case "tables":
		return (new(Table))
	case "reservations":
		return (new(Reservation))
	case "orders":
		return (new(Order))
	case "meals":
		return (new(Meal))
	case "users":
		return (new(User))
	}
	return nil
}

func handleglobal(res http.ResponseWriter, req *http.Request) {
	vars := mux.Vars(req)
	var obj = getObj(vars["table"])
	var data []byte
	data, _ = json.Marshal("{success:false}")
	if obj != nil {
		switch req.Method {
		case "POST":
			err := req.ParseForm()
			checkErr(err, "Error parse form")
			err = decoder.Decode(obj, req.PostForm)
			checkErr(err, "Error decode form")
			add(obj)
			data, _ = json.Marshal(obj)
		case "GET":
			d, err := strconv.Atoi(vars["id"])
			if err != nil {
				test := obj.getAll()
				data, _ = json.Marshal(test)
			} else {
				obj = obj.setID(d)
				obj = get(obj)
				if obj != nil {
					data, _ = json.Marshal(obj)
				}
			}
		case "DELETE":
			d := atoi(vars["id"])
			obj = obj.setID(d)
			del(obj)
			data, _ = json.Marshal("{success:true}")
		case "PUT":
			d := atoi(vars["id"])
			err := req.ParseForm()
			checkErr(err, "Error parse form")
			err = decoder.Decode(obj, req.PostForm)
			checkErr(err, "Error decode form")
			obj = obj.setID(d)
			put(obj)
			data, _ = json.Marshal(obj)
		}
	}
	res.Write(data)
}
