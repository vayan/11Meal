package main

import (
	"database/sql"
	"encoding/json"
	"github.com/coopernurse/gorp"
	"github.com/gorilla/mux"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
	"os"
	"strconv"
)

var (
	port  = "8181"
	dbmap *gorp.DbMap
)

type Table struct {
	Id      int
	Is_free bool
	Seats   int
}

type Meal struct {
	Id    int
	Name  string
	Price int
}

type Order struct {
	Id int
}

type User struct {
	Id         int
	First_name string
	Last_name  string
	Email      string
	Phone      string
	Login      string
}

type Reservation struct {
	Id          int
	Is_finished bool
}

type Resto struct {
	Id       int
	Name     string
	Address  string
	Position string
}

func init_db() *gorp.DbMap {
	os.Remove("./foo.db")
	db, err := sql.Open("sqlite3", "./foo.db")
	checkErr(err, "Failed open DB")
	dbmap := &gorp.DbMap{Db: db, Dialect: gorp.SqliteDialect{}}

	dbmap.AddTableWithName(Table{}, "tables").SetKeys(true, "Id")
	dbmap.AddTableWithName(Resto{}, "restos").SetKeys(true, "Id")
	dbmap.AddTableWithName(Reservation{}, "reservations").SetKeys(true, "Id")
	dbmap.AddTableWithName(Order{}, "orders").SetKeys(true, "Id")
	dbmap.AddTableWithName(Meal{}, "meals").SetKeys(true, "Id")

	err = dbmap.CreateTablesIfNotExists()
	checkErr(err, "Create tables failed")

	//debug
	err = dbmap.TruncateTables()
	checkErr(err, "TruncateTables failed")
	return dbmap
}

func (rest Resto) getAll() []Resto { //GET no param
	var restos []Resto
	log.Println("get all resto ")
	_, err := dbmap.Select(&restos, "select * from restos order by Id")
	checkErr(err, "getAll rest failed")
	log.Println("All rows:")
	for x, p := range restos {
		log.Printf("    %d: %v\n", x, p)
	}
	return restos
}

func (rest Resto) get() { //GET with ID
	log.Println("before selected ", rest)
	err := dbmap.SelectOne(&rest, "select * from restos where Id=?", rest.Id)
	checkErr(err, "get resto failed")
	log.Println("after selected ", rest)
}

func (rest Resto) add() { //new one
	log.Println("add new resto")
	err := dbmap.Insert(&rest)
	checkErr(err, "Insert resto failed")
	log.Println("new resto added", rest)
}

func (rest Resto) del() { //delete
	log.Println("del resto", rest)
	count, err := dbmap.Delete(&rest)
	checkErr(err, "Del resto failed")
	log.Println("resto deleted:", count)
}

func (rest Resto) put() { //update
	log.Println("update resto", rest)
	count, err := dbmap.Update(&rest)
	checkErr(err, "Update resto failed")
	log.Println("resto updated:", count)
}

func checkErr(err error, msg string) {
	if err != nil {
		log.Fatalln(msg, err)
	}
}

func atoi(s string) int {
	d, err := strconv.Atoi(s)
	if err != nil {
		log.Panicln(err)
		return 0
	}
	return d
}

func handleResto(res http.ResponseWriter, req *http.Request) {
	vars := mux.Vars(req)
	r := Resto{0, "foo", "test", "test"}
	if vars["id"] != "" {
		r.Id = atoi(vars["id"])
	}
	log.Println("rest is ", r)
	switch req.Method {
	case "POST":
		r.add()
	case "GET":
		r.get()
	case "DELETE":
		r.del()
	case "PUT":
		r.put()
	}
	data, _ := json.Marshal("{'request':'success'}")
	res.Write(data)
}

func handleReservation(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}

func handleUsers(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}

func handleOrder(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}

func handleMeal(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}

func handleTable(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}
