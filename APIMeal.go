package main

import (
	"database/sql"
	"github.com/coopernurse/gorp"
	"github.com/gorilla/mux"
	"github.com/gorilla/schema"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
	"strconv"
)

var (
	port    = ":8181"
	dbmap   *gorp.DbMap
	decoder = schema.NewDecoder()
)

func checkErr(err error, msg string) {
	if err != nil {
		log.Panicln(msg, err)
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

func init_db() *gorp.DbMap {
	db, err := sql.Open("sqlite3", "./APIMeal.db")
	checkErr(err, "Failed open DB")
	dbmap := &gorp.DbMap{Db: db, Dialect: gorp.SqliteDialect{}}

	dbmap.AddTableWithName(Table{}, "table").SetKeys(true, "Id")
	dbmap.AddTableWithName(Restaurant{}, "restaurant").SetKeys(true, "Id")
	dbmap.AddTableWithName(Reservation{}, "reservation").SetKeys(true, "Id")
	dbmap.AddTableWithName(Order{}, "order").SetKeys(true, "Id")
	dbmap.AddTableWithName(Meal{}, "meal").SetKeys(true, "Id")
	dbmap.AddTableWithName(User{}, "user").SetKeys(true, "Id")

	err = dbmap.CreateTablesIfNotExists()
	checkErr(err, "Create tables failed")
	log.Println("Db created")
	return dbmap
}

func start_web_server() {
	r := mux.NewRouter()
	r.Headers("Content-Type", "application/json; charset=utf-8")

	r.HandleFunc("/{table}/{column}/{id}", handleglobal)
	r.HandleFunc("/{table}/{id}", handleglobal)
	r.HandleFunc("/{table}", handleglobal)

	http.Handle("/", r)

	log.Println("Starting and listening on ", port, "...")
	err := http.ListenAndServe(port, nil)
	checkErr(err, "Failed to start server")
}

func main() {
	dbmap = init_db()
	defer dbmap.Db.Close()
	start_web_server()
}
