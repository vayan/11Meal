package main

import (
	"github.com/coopernurse/gorp"
	"github.com/gorilla/schema"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"strconv"
)

var (
	GCM_API_KEY     string
	GPLACES_API_KEY string
	port            = ":8181"
	dbmap           *gorp.DbMap
	decoder         = schema.NewDecoder()
)

func getObj(t string) (interface{}, interface{}) {
	switch t {
	case "restaurant":
		return new(Restaurant), new([]Restaurant)
	case "table":
		return new(Table), new([]Table)
	case "reservation":
		return new(Reservation), new([]Reservation)
	case "order":
		return new(Order), new([]Order)
	case "meal":
		return new(Meal), new([]Meal)
	case "user":
		return new(User), new([]User)
	case "usermeal":
		return new(UserMeal), new([]UserMeal)
	}
	log.Println("getObjArray : didn't find type")
	return nil, nil
}

func vayanisme() {
	for {
		log.Println("HAHA")
	}
}

func atoi(s string) int {
	d, err := strconv.Atoi(s)
	if err != nil {
		log.Println(err)
		return 0
	}
	return d
}

func checkFatal(err error) {
	if err != nil {
		log.Fatalln(err)
	}
}
