package main

import (
	"database/sql"
	"github.com/coopernurse/gorp"
	"github.com/gorilla/mux"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
)

func init_db() *gorp.DbMap {
	db, err := sql.Open("sqlite3", "./foo.db")
	checkErr(err, "Failed open DB")
	dbmap := &gorp.DbMap{Db: db, Dialect: gorp.SqliteDialect{}}

	dbmap.AddTableWithName(Table{}, "tables").SetKeys(true, "Id")
	dbmap.AddTableWithName(Resto{}, "restos").SetKeys(true, "Id")
	dbmap.AddTableWithName(Reservation{}, "reservations").SetKeys(true, "Id")
	dbmap.AddTableWithName(Order{}, "orders").SetKeys(true, "Id")
	dbmap.AddTableWithName(Meal{}, "meals").SetKeys(true, "Id")
	dbmap.AddTableWithName(User{}, "users").SetKeys(true, "Id")

	err = dbmap.CreateTablesIfNotExists()
	checkErr(err, "Create tables failed")

	//debug
	//err = dbmap.TruncateTables()
	//checkErr(err, "TruncateTables failed")
	return dbmap
}

func start_web_server() {
	r := mux.NewRouter()
	r.Headers("Content-Type", "application/json; charset=utf-8")

	r.HandleFunc("/{table}/{id}", handleglobal)
	r.HandleFunc("/{table}", handleglobal)

	http.Handle("/", r)

	log.Println("Starting and listening on ", port, "...")
	err := http.ListenAndServe(":"+port, nil)
	checkErr(err, "Failed to start server")
}
