package main

import (
	"encoding/json"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"reflect"
	"strings"
)

func add(r interface{}) {
	if r != nil {
		err := dbmap.Insert(r)
		checkErr(err, "new failed")
		log.Println("new ", r)
	} else {
		log.Fatalln("Add : r is nil")
	}
}

func del(r interface{}) {
	if r != nil {
		_, err := dbmap.Delete(r)
		checkErr(err, "del failed")
		log.Println("del ", r)
	} else {
		log.Fatalln("Del : r is nil")
	}
}

func put(r interface{}) {
	if r != nil {
		_, err := dbmap.Update(r)
		checkErr(err, "Update failed")
		log.Println("put ", r)
	} else {
		log.Fatalln("Put : r is nil")
	}
}

func getObjArray(t string) interface{} {
	switch t {
	case "restaurant":
		return (new([]Restaurant))
	case "table":
		return (new([]Table))
	case "reservation":
		return (new([]Reservation))
	case "order":
		return (new([]Order))
	case "meal":
		return (new([]Meal))
	case "user":
		return (new([]User))
	}
	log.Fatalln("getObjArray : didn't find type")
	return nil
}

func getObj(t string) interface{} {
	switch t {
	case "restaurant":
		return (new(Restaurant))
	case "table":
		return (new(Table))
	case "reservation":
		return (new(Reservation))
	case "order":
		return (new(Order))
	case "meal":
		return (new(Meal))
	case "user":
		return (new(User))
	}
	log.Fatalln("getObj : didn't find type")
	return nil
}

func get(vars map[string]string, obj_array interface{}) {
	var sql_req string
	table := strings.ToLower(vars["table"])
	column := strings.ToLower(vars["column"])

	if len(table) > 0 && len(column) > 0 && len(vars["id"]) > 0 {
		sql_req = "select * from " + table + " where " + column + " like " + vars["id"]
	} else if len(table) > 0 && len(vars["id"]) > 0 {
		sql_req = "select * from " + table + " where Id like " + vars["id"]
	} else {
		sql_req = "select * from " + table
	}
	_, err := dbmap.Select(obj_array, sql_req)
	checkErr(err, "Error sql select")
	log.Println("get ", obj_array)
}

func handleglobal(res http.ResponseWriter, req *http.Request) {
	var data []byte

	vars := mux.Vars(req)
	table := strings.ToLower(vars["table"])
	obj_array := getObjArray(table)
	obj := getObj(table)
	data, _ = json.Marshal("{success:false}")
	res.Header().Set("Access-Control-Allow-Origin", "*")
	res.Header().Set("Content-Type", "application/json; charset=utf-8")
	res.Header().Set("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, VAYAN")
	if obj_array == nil || obj == nil {
	} else {
		switch req.Method {
		case "GET":
			get(vars, obj_array)
			if reflect.ValueOf(obj_array).Elem().Len() > 0 {
				data, _ = json.Marshal(obj_array)
			}
		case "POST":
			err := req.ParseForm()
			checkErr(err, "Error parse form")
			err = decoder.Decode(obj, req.PostForm)
			checkErr(err, "Error decode form")
			add(obj)
			if obj != nil {
				data, err = json.Marshal(obj)
				checkErr(err, "Error marshal obj")
			}
		case "DELETE":
			d := atoi(vars["id"])

			reflect.ValueOf(obj).Elem().FieldByName("Id").SetInt(int64(d))
			del(obj)
			if obj != nil {
				data, _ = json.Marshal("{success:true}")
			}
		case "PUT":
			d := atoi(vars["id"])
			err := req.ParseForm()
			checkErr(err, "Error parse form")
			err = decoder.Decode(obj, req.PostForm)
			checkErr(err, "Error decode form")
			reflect.ValueOf(obj).Elem().FieldByName("Id").SetInt(int64(d))
			put(obj)
			if obj != nil {
				data, err = json.Marshal(obj)
				checkErr(err, "Error marshal obj")
			}
		}
	}
	res.Write(data)
}
