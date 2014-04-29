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
	log.Println("before selected ", r, "is type ", reflect.TypeOf(r))
	obj, err := dbmap.Get(reflect.ValueOf(r).Interface(), r.getID())
	checkErr(err, "get resto failed")
	log.Println("after selected ", obj)
	return obj.(Global)
}

func add(r Global) {
	log.Println("add new resto")
	err := dbmap.Insert(r)
	checkErr(err, "Insert resto failed")
	log.Println("new resto added", r)
}

func del(r Global) {
	log.Println("del resto", r)
	count, err := dbmap.Delete(r)
	checkErr(err, "Del resto failed")
	log.Println("resto deleted:", count)
}

func put(r Global) { //update
	log.Println("update resto", r)
	count, err := dbmap.Update(r)
	checkErr(err, "Update resto failed")
	log.Println("resto updated:", count)
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

	switch req.Method {
	case "POST":
		err := req.ParseForm()
		checkErr(err, "Error parse form")
		err = decoder.Decode(obj, req.PostForm)
		checkErr(err, "Error decode form")
		add(obj)
	case "GET":
		d, err := strconv.Atoi(vars["id"])
		if err != nil {
			test := obj.getAll()
			log.Println("result ", test)
		} else {
			log.Println("seting id to ", d)
			obj = obj.setID(d)
			log.Println("id is now ", obj.getID())
			obj = get(obj)

		}
	case "DELETE":
		d := atoi(vars["id"])
		obj = obj.setID(d)
		del(obj)

	case "PUT":
		d := atoi(vars["id"])
		err := req.ParseForm()
		checkErr(err, "Error parse form")
		err = decoder.Decode(obj, req.PostForm)
		checkErr(err, "Error decode form")
		obj = obj.setID(d)
		put(obj)
	}
	data, _ = json.Marshal(obj)
	res.Write(data)
}
