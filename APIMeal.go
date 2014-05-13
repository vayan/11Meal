package main

import (
	"database/sql"
	"github.com/coopernurse/gorp"
	"github.com/gorilla/mux"
	"github.com/jimlawless/cfg"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
	"os"
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
	dbmap.AddTableWithName(User{}, "user").SetKeys(true, "Id")
	dbmap.AddTableWithName(Promo{}, "promo").SetKeys(true, "Id")
	err = dbmap.CreateTablesIfNotExists()
	checkFatal(err)
	log.Println("=== Db init")
	return dbmap
}

func start_web_server() {
	r := mux.NewRouter()

	r.HandleFunc("/restaurant/gps/{coord}/{distance}", handleGPS)
	r.HandleFunc("/restaurant/gps/{coord}", handleGPS)
	r.HandleFunc("/gcm", handleGCM)
	r.HandleFunc("/{table}/{column}/{id}", handleglobal)
	r.HandleFunc("/{table}/{id}", handleglobal)
	r.HandleFunc("/{table}", handleglobal)

	http.Handle("/", r)

	log.Println("=== Starting and listening on", port, "...")
	err := http.ListenAndServe(port, nil)
	checkFatal(err)
}

func load_conf() {
	if len(os.Args) < 2 {
		log.Fatalln("Usage :", os.Args[0], "path/server.cfg")
	}
	log.Println("=== Start loading conf files")
	conf := make(map[string]string)
	err := cfg.Load(os.Args[1], conf)
	checkFatal(err)
	if conf["gcm_api"] == "" || conf["place_api"] == "" {
		log.Fatalln("Please put the API key in the config file")
	}
	GCM_API_KEY = conf["gcm_api"]
	GPLACES_API_KEY = conf["place_api"]
	if len(conf["port"]) > 0 {
		port = ":" + conf["port"]
	}
	log.Println("=== Conf loaded, contain : \n", conf)
}

func main() {
	load_conf()
	dbmap = init_db()
	defer dbmap.Db.Close()
	start_web_server()
}
