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
		case *User:
			send_gcm(get_all_gcm_id(), "new user :)")
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
			r.(*Reservation).CheckChange()
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

func get_special(obj_array interface{}) interface{} {
	switch obj_array.(type) {
	case *[]Reservation:
		var tmp []Reservation
		for _, obj := range *obj_array.(*[]Reservation) {
			obj.GuestUnserialize()
			tmp = append(tmp, obj)
		}
		return tmp
	case *[]Order:
		var tmp []Order
		for _, obj := range *obj_array.(*[]Order) {
			obj.MealUnserialize()
			obj.ComputePrice()
			tmp = append(tmp, obj)
		}
		return tmp
	case *[]Restaurant:
		var tmp []Restaurant
		for _, obj := range *obj_array.(*[]Restaurant) {
			obj.PromoUnserialize()
			tmp = append(tmp, obj)
		}
		return tmp
	}
	return obj_array
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
	obj_array = get_special(obj_array)
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

func (o *Order) ComputePrice() {
	total_price := 0
	for _, meal := range o.Meals {
		total_price += meal.Price
	}
	o.Total_price = total_price
}

func (o *Reservation) CheckChange() {
	var oldone Reservation

	err := dbmap.SelectOne(&oldone, "select * from reservation where id=?", o.Id)
	if err != nil {
		log.Println(err)
	}
	if o.State != oldone.State {
		send_gcm(get_gcm_id(o.Guests), "Reservation status changed")
	}
}

func (o *Restaurant) PromoUnserialize() {
	var u []Promo
	sql_req := "select * from `promo` where restaurant =" + strconv.Itoa(o.Id)
	log.Println(sql_req)
	_, err := dbmap.Select(&u, sql_req)
	if err != nil {
		log.Println(err)
	}
	o.Promos = u
}