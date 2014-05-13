package main

import (
	"crypto/md5"
	"encoding/hex"
	"github.com/coopernurse/gorp"
	"github.com/gorilla/schema"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"strconv"
)

var (
	ErrorAPI = map[string]string{
		"ZERO_RESULTS":     "Search was successful but returned no results.",
		"OVER_QUERY_LIMIT": "You are over your quota.",
		"REQUEST_DENIED":   "Your request was denied, check sensor parameter.",
		"INVALID_REQUEST":  "Required query parameter (eg : location or radius) is missing."}
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
	case "promo":
		return new(Promo), new([]Promo)
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
		log.Println("Atoi()", err)
		return 0
	}
	return d
}

func checkFatal(err error) {
	if err != nil {
		log.Fatalln(err)
	}
}

func GetMD5Hash(text string) string {
	hasher := md5.New()
	hasher.Write([]byte(text))
	return hex.EncodeToString(hasher.Sum(nil))
}
