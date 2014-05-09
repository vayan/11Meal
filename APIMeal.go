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
	db, err := sql.Open("sqlite3", "./APIMeal.db")
	checkFatal(err)
	dbmap := &gorp.DbMap{Db: db, Dialect: gorp.SqliteDialect{}}

	dbmap.AddTableWithName(Table{}, "table").SetKeys(true, "Id")
	dbmap.AddTableWithName(Restaurant{}, "restaurant").SetKeys(true, "Id")
	dbmap.AddTableWithName(Reservation{}, "reservation").SetKeys(true, "Id")
	dbmap.AddTableWithName(Order{}, "order").SetKeys(true, "Id")
	dbmap.AddTableWithName(Meal{}, "meal").SetKeys(true, "Id")
	dbmap.AddTableWithName(UserMeal{}, "usermeal").SetKeys(true, "Id")
	dbmap.AddTableWithName(User{}, "user").SetKeys(true, "Id")

	err = dbmap.CreateTablesIfNotExists()
	checkFatal(err)
	log.Println("Db created")
	return dbmap
}

func start_web_server() {
	r := mux.NewRouter()

	r.HandleFunc("/{table}/{column}/{id}", handleglobal)
	r.HandleFunc("/{table}/{id}", handleglobal)
	r.HandleFunc("/{table}", handleglobal)
	r.HandleFunc("/gcm", handleGCM)

	http.Handle("/", r)

	log.Println("Starting and listening on ", port, "...")
	err := http.ListenAndServe(port, nil)
	checkFatal(err)
}

func main() {
	dbmap = init_db()
	defer dbmap.Db.Close()
	start_web_server()
}
