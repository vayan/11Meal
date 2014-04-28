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
	//os.Remove("./foo.db")
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
	//err = dbmap.TruncateTables()
	//checkErr(err, "TruncateTables failed")
	return dbmap
}

func start_web_server() {
	r := mux.NewRouter()
	r.Headers("Content-Type", "application/json; charset=utf-8")
	r.HandleFunc("/restaurants", handleResto)
	r.HandleFunc("/restaurants/{id}", handleResto)

	r.HandleFunc("/table", handleTable)
	r.HandleFunc("/table/{id}", handleTable)

	r.HandleFunc("/reservation", handleReservation)
	r.HandleFunc("/reservation/{id}", handleReservation)

	r.HandleFunc("/users", handleUsers)
	r.HandleFunc("/users/{id}", handleUsers)

	r.HandleFunc("/orders", handleOrder)
	r.HandleFunc("/orders/{id}", handleOrder)

	r.HandleFunc("/meal", handleMeal)
	r.HandleFunc("/meal/{id}", handleMeal)

	http.Handle("/", r)

	log.Println("Starting and listening on ", port, "...")
	err := http.ListenAndServe(":"+port, nil)
	checkErr(err, "Failed to start server")
}
