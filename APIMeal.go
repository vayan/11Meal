package main

import (
	"bytes"
	"database/sql"
	"encoding/json"
	"github.com/coopernurse/gorp"
	"github.com/gorilla/mux"
	"github.com/gorilla/schema"
	_ "github.com/mattn/go-sqlite3"
	"io/ioutil"
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

func send_gcm(msg string) error {
	reqGCM := RequestGCM{
		"AIzaSyBAEuckZUufUFtiMw4zq6gYCDAhBD6Iy9w",
		[]string{"APA91bEdfxCFNZCYdmHmQbO2EgUSfZcFdKtQWf9zp7uVw2DekZQKGevqvvNV_z-9iGi_wtvYEELMXVK6Ac-0-yPJ9UeFfBYbmhxcx2lCB2Zqbhq79qYGYw7QMYoHZuYYXpZwpljOVILbPwRwzBBKypDPFxZnCyrlkw", "3", "4"},
		map[string]string{"test": "nothing"}}
	data, err := json.Marshal(reqGCM)
	if err != nil {
		log.Println(err)
		return err
	}
	log.Println("send this to google ", string(data))

	client := &http.Client{}

	req, _ := http.NewRequest(
		"POST",
		"https://android.googleapis.com/gcm/send",
		bytes.NewReader(data))
	req.Header.Add("Content-Type", "application/json")
	req.Header.Add("Authorization", "key="+reqGCM.API_Key)
	resp, err := client.Do(req)
	if err != nil {
		log.Println(err)
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err)
	}
	log.Println("Body ", string(body))
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
	dbmap.AddTableWithName(UserMeal{}, "usermeal").SetKeys(true, "Id")
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
	r.HandleFunc("/gcm", handleGCM)

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
