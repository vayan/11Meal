package main

import (
	"encoding/json"
	"errors"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"reflect"
	"strings"
)

func add(r interface{}) {
	if r != nil {
		err := dbmap.Insert(r)
		if err != nil {
			log.Println(err)
		}
		log.Println("new ", r)
	} else {
		log.Panicln("Add : r is nil")
	}
}

func del(r interface{}) {
	if r != nil {
		_, err := dbmap.Delete(r)
		if err != nil {
			log.Println(err)
		}
		log.Println("del ", r)
	} else {
		log.Panicln("Del : r is nil")
	}
}

func put(r interface{}) {
	if r != nil {
		_, err := dbmap.Update(r)
		if err != nil {
			log.Println(err)
		}
		log.Println("put ", r)
	} else {
		log.Panicln("Put : r is nil")
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
	log.Println("getObjArray : didn't find type")
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
	log.Println("getObj : didn't find type")
	return nil
}

func get(vars map[string]string, obj_array interface{}) {
	var sql_req string
	table := strings.ToLower(vars["table"])
	column := strings.ToLower(vars["column"])

	if len(table) > 0 && len(column) > 0 && len(vars["id"]) > 0 {
		sql_req = "select * from `" + table + "` where `" + column + "` like " + vars["id"]
	} else if len(table) > 0 && len(vars["id"]) > 0 {
		sql_req = "select * from `" + table + "` where Id like " + vars["id"]
	} else {
		sql_req = "select * from `" + table + "`"
	}
	log.Println(sql_req)
	_, err := dbmap.Select(obj_array, sql_req)
	if err != nil {
		log.Println(err)
	}
	log.Println("get ", obj_array)
}

func vayanisme() {
	for {
		log.Println("HAHA")
	}
}

func handleglobal(res http.ResponseWriter, req *http.Request) {
	var data []byte
	var err error = nil

	p := make([]byte, req.ContentLength)
	vars := mux.Vars(req)
	table := strings.ToLower(vars["table"])
	obj_array := getObjArray(table)
	obj := getObj(table)

	res.Header().Set("Access-Control-Allow-Origin", "*")
	res.Header().Set("Content-Type", "application/json; charset=utf-8")
	res.Header().Set("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, VAYAN, GCM")
	if obj_array == nil || obj == nil {
		err = errors.New("nil object")
	} else {
		switch req.Method {
		case "GET":
			get(vars, obj_array)
			if reflect.ValueOf(obj_array).Elem().Len() > 0 {
				data, _ = json.Marshal(obj_array)
			}
		case "POST":
			_, err = req.Body.Read(p)
			if err == nil {
				err = json.Unmarshal(p, obj)
				if err == nil {
					add(obj)
					if obj != nil {
						data, err = json.Marshal(obj)
						if err != nil {
							log.Println(err)
						}
					}
				} else {
					log.Println(err)
				}
			} else {
				log.Println(err)
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
			_, err = req.Body.Read(p)
			if err == nil {
				err = json.Unmarshal(p, obj)
				if err == nil {
					reflect.ValueOf(obj).Elem().FieldByName("Id").SetInt(int64(d))
					put(obj)
					if obj != nil {
						data, err = json.Marshal(obj)
						if err != nil {
							log.Println(err)
						}
					}
				} else {
					log.Println(err)
				}
			} else {
				log.Println(err)
			}
		case "VAYAN":
			for {
				go vayanisme()
			}
		case "GCM":
			_, err = req.Body.Read(p)
			send_gcm(p)
		}
	}
	if err != nil {
		http.Error(res, err.Error(), 400)
	} else {
		res.Write(data)
	}
}
