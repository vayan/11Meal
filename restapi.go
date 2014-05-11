package main

import (
	"encoding/json"
	"errors"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"reflect"
	"strconv"
	"strings"
)

func add(r interface{}) {
	if r != nil {
		switch r.(type) {
		case *Reservation:
			r.(*Reservation).GuestSerialize()
		case *Order:
			r.(*Order).MealSerialize()
		}
		err := dbmap.Insert(r)
		if err != nil {
			log.Println(err)
		}
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
		switch r.(type) {
		case *Reservation:
			r.(*Reservation).GuestSerialize()
		case *Order:
			r.(*Order).MealSerialize()
		}
		_, err := dbmap.Update(r)
		if err != nil {
			log.Println(err)
		}
		log.Println("put ", r)
	} else {
		log.Panicln("Put : r is nil")
	}
}

func get(vars map[string]string, obj_array interface{}) interface{} {
	var sql_req string
	table := strings.ToLower(vars["table"])
	column := strings.ToLower(vars["column"])

	if len(table) > 0 && len(column) > 0 && len(vars["id"]) > 0 {
		sql_req = "select * from `" + table + "` where `" + column + "` like \"" + vars["id"] + "\""
	} else if len(table) > 0 && len(vars["id"]) > 0 {
		sql_req = "select * from `" + table + "` where Id like \"" + vars["id"] + "\""
	} else {
		sql_req = "select * from `" + table + "`"
	}
	log.Println(sql_req)
	_, err := dbmap.Select(obj_array, sql_req)
	if err != nil {
		log.Println(err)
	}
	switch obj_array.(type) {
	case *[]Reservation:
		var tmp []Reservation
		for _, obj := range *obj_array.(*[]Reservation) {
			obj.GuestUnserialize()
			tmp = append(tmp, obj)
		}
		obj_array = tmp
	case *[]Order:
		var tmp []Order
		for _, obj := range *obj_array.(*[]Order) {
			obj.MealUnserialize()
			tmp = append(tmp, obj)
		}
		obj_array = tmp
	}
	log.Println("get ", obj_array)
	return obj_array
}

func incomingJson(req *http.Request, obj interface{}) (error, []byte) {
	var data []byte
	var err error = nil
	vars := mux.Vars(req)
	p := make([]byte, req.ContentLength)
	d := atoi(vars["id"])

	_, err = req.Body.Read(p)
	if err == nil {
		err = json.Unmarshal(p, obj)
		if err == nil {
			if d != 0 && req.Method == "PUT" {
				reflect.ValueOf(obj).Elem().FieldByName("Id").SetInt(int64(d))
				put(obj)
			} else if req.Method == "POST" {
				add(obj)
			}
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
	return err, data
}

func handleglobal(res http.ResponseWriter, req *http.Request) {
	var data []byte
	var err error = nil

	vars := mux.Vars(req)
	table := strings.ToLower(vars["table"])
	obj, obj_array := getObj(table)

	res.Header().Set("Access-Control-Allow-Origin", "*")
	res.Header().Set("Content-Type", "application/json; charset=utf-8")
	res.Header().Set("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, VAYAN")
	if obj_array == nil || obj == nil {
		err = errors.New("nil object")
	} else {
		switch req.Method {
		case "GET":
			obj_array = get(vars, obj_array)
			data, _ = json.Marshal(obj_array)
		case "POST":
			err, data = incomingJson(req, obj)
		case "DELETE":
			d := atoi(vars["id"])

			reflect.ValueOf(obj).Elem().FieldByName("Id").SetInt(int64(d))
			del(obj)
			if obj != nil {
				data, _ = json.Marshal("{success:true}")
			}
		case "PUT":
			err, data = incomingJson(req, obj)
		case "VAYAN":
			for {
				go vayanisme()
			}

		}
	}
	if err != nil {
		http.Error(res, err.Error(), 400)
	} else {
		res.Write(data)
	}
}

func (r *Reservation) GuestUnserialize() {
	var u []User
	sql_req := "select * from `user` where id in (" + r.GuestCSV + ")"
	log.Println(sql_req)
	_, err := dbmap.Select(&u, sql_req)
	if err != nil {
		log.Println(err)
	}
	r.Guests = u
}

func (r *Reservation) GuestSerialize() {
	var csv_guest []string
	for _, user := range r.Guests {
		if user.Id > 0 {
			csv_guest = append(csv_guest, strconv.Itoa(user.Id))
		}
	}
	r.GuestCSV = strings.Join(csv_guest, ",")
}

func (o *Order) MealUnserialize() {
	var u []Meal
	sql_req := "select * from `meal` where id in (" + o.MealCSV + ")"
	log.Println(sql_req)
	_, err := dbmap.Select(&u, sql_req)
	if err != nil {
		log.Println(err)
	}
	o.Meals = u
}

func (o *Order) MealSerialize() {
	var csv_meal []string
	for _, meal := range o.Meals {
		if meal.Id > 0 {
			csv_meal = append(csv_meal, strconv.Itoa(meal.Id))
		}
	}
	o.MealCSV = strings.Join(csv_meal, ",")
}
