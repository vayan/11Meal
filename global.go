package main

import (
	"encoding/json"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"reflect"
)

func get(r interface{}) interface{} {
	if r == nil {
		return nil
	}
	id := reflect.ValueOf(r).FieldByName("Id")
	log.Println("get ", id)
	obj, err := dbmap.Get(reflect.ValueOf(r).Interface(), id)
	checkErr(err, "get resto failed")
	if obj == nil {
		return nil
	}
	return obj
}

func add(r interface{}) {
	err := dbmap.Insert(r)
	checkErr(err, "Insert resto failed")
}

func del(r interface{}) {
	_, err := dbmap.Delete(r)
	checkErr(err, "Del resto failed")
}

func put(r interface{}) {
	_, err := dbmap.Update(r)
	checkErr(err, "Update resto failed")
}

func getObjArray(t string) interface{} {
	switch t {
	case "restos":
		return (new([]Resto))
	case "tables":
		return (new([]Table))
	case "reservations":
		return (new([]Reservation))
	case "orders":
		return (new([]Order))
	case "meals":
		return (new([]Meal))
	case "users":
		return (new([]User))
	}
	return nil
}

func getObj(t string) interface{} {
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
	var data []byte
	var sql_req string
	vars := mux.Vars(req)
	obj_array := getObjArray(vars["table"])
	obj := getObj(vars["table"])
	data, _ = json.Marshal("{success:false}")
	switch req.Method {
	case "GET":
		if len(vars["table"]) > 0 && len(vars["column"]) > 0 && len(vars["id"]) > 0 {
			sql_req = "select * from " + vars["table"] + " where " + vars["column"] + " like " + vars["id"]
		} else if len(vars["table"]) > 0 && len(vars["id"]) > 0 {
			sql_req = "select * from " + vars["table"] + " where Id like " + vars["id"]
		} else {
			sql_req = "select * from " + vars["table"]
		}
		log.Println(obj_array)
		_, err := dbmap.Select(obj_array, sql_req)
		checkErr(err, "error sql select")
		log.Println(obj_array)
		data, _ = json.Marshal(obj_array)
	case "POST":
		err := req.ParseForm()
		checkErr(err, "Error parse form")
		err = decoder.Decode(obj, req.PostForm)
		checkErr(err, "Error decode form")
		add(obj)
		data, _ = json.Marshal(obj)
	case "DELETE":
		d := atoi(vars["id"])
		reflect.ValueOf(obj).Elem().FieldByName("Id").SetInt(int64(d))
		del(obj)
		data, _ = json.Marshal("{success:true}")
	case "PUT":
		d := atoi(vars["id"])
		err := req.ParseForm()
		checkErr(err, "Error parse form")
		err = decoder.Decode(obj, req.PostForm)
		checkErr(err, "Error decode form")
		reflect.ValueOf(obj).Elem().FieldByName("Id").SetInt(int64(d))
		put(obj)
		data, _ = json.Marshal(obj)
	}
	res.Write(data)
}
