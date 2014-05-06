package main

import (
	"bytes"
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

func atoi(s string) int {
	d, err := strconv.Atoi(s)
	if err != nil {
		log.Println(err)
		return 0
	}
	return d
}

func send_gcm(msg []byte) error {
	resp, err := http.Post("http://example.com/upload", "application/json", bytes.NewReader(msg))
	if err != nil {

	}
	defer resp.Body.Close()
	p := make([]byte, resp.ContentLength)
	_, err = resp.Body.Read(p)
	log.Println("Body ", p)
	return err
}

func init_db() *gorp.DbMap {
	db, err := sql.Open("sqlite3", "./APIMeal.db")
	if err != nil {
		log.Fatalln(err)
	}
	dbmap := &gorp.DbMap{Db: db, Dialect: gorp.SqliteDialect{}}

	dbmap.AddTableWithName(Table{}, "table").SetKeys(true, "Id")
	dbmap.AddTableWithName(Restaurant{}, "restaurant").SetKeys(true, "Id")
	dbmap.AddTableWithName(Reservation{}, "reservation").SetKeys(true, "Id")
	dbmap.AddTableWithName(Order{}, "order").SetKeys(true, "Id")
	dbmap.AddTableWithName(Meal{}, "meal").SetKeys(true, "Id")
	dbmap.AddTableWithName(User{}, "user").SetKeys(true, "Id")

	err = dbmap.CreateTablesIfNotExists()
	if err != nil {
		log.Fatalln(err)
	}
	log.Println("Db created")
	return dbmap
}

func start_web_server() {
	r := mux.NewRouter()

	r.HandleFunc("/{table}/{column}/{id}", handleglobal)
	r.HandleFunc("/{table}/{id}", handleglobal)
	r.HandleFunc("/{table}", handleglobal)

	http.Handle("/", r)

	log.Println("Starting and listening on ", port, "...")
	err := http.ListenAndServe(port, nil)
	if err != nil {
		log.Fatalln(err)
	}
}

func main() {
	dbmap = init_db()
	defer dbmap.Db.Close()
	start_web_server()
}
