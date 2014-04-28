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
}

func get(r Global) {
	log.Println("before selected ", r, "is type ", reflect.TypeOf(r))
	obj, err := dbmap.Get(reflect.ValueOf(r).Interface(), r.getID())
	checkErr(err, "get resto failed")
	//rest = *obj.(*Global)
	log.Println("after selected ", obj)
}

func getAll(g Global, tp string) []Global {
	// glo := getSliceObj(tp)
	// _, err := dbmap.Select(&glo, "select * from "+tp)
	// checkErr(err, "Select failed")
	// log.Println("All rows:")
	// for x, p := range glo {
	// 	log.Printf("    %d: %v\n", x, p)
	// }
	return nil
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
			test := getAll(obj, vars["table"])
			log.Println("result ", test)
		} else {
			log.Println("seting id to ", d)
			obj = obj.setID(d)
			log.Println("id is now ", obj.getID())
			get(obj)
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
	data, _ := json.Marshal("{'request':'success'}")
	res.Write(data)
}
