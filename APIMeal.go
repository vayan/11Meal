package main

import (
	"database/sql"
	"encoding/json"
	"github.com/gorilla/mux"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
	"os"
	"strconv"
)

var (
	port = "8181"
	sqdb *sql.DB
)

type Resto struct {
	id   int
	name string
}

func init_db() {
	os.Remove("./foo.db")
	db, err := sql.Open("sqlite3", "./foo.db")
	if err != nil {
		log.Fatal(err)
	}
	sql := `
create table restaurant (id integer not null primary key, name text);
`
	_, err = db.Exec(sql)
	if err != nil {
		log.Printf("%q: %s\n", err, sql)
		return
	}
	sqdb = db
}

func (rest Resto) getAll() { //GET no param

}

func (rest Resto) get() { //GET with ID
	log.Println(rest.id)
}

func (rest Resto) add() { //new one
	tx, err := sqdb.Begin()
	if err != nil {
		log.Fatal(err)
	}
	stmt, err := tx.Prepare("insert into restaurant(id, name) values(?, ?)")
	if err != nil {
		log.Fatal(err)
	}
	defer stmt.Close()
	_, err = stmt.Exec(rest.id, rest.name)
	if err != nil {
		log.Fatal(err)
	}
	tx.Commit()
}

func (rest Resto) del() { //delete
	tx, err := sqdb.Begin()
	if err != nil {
		log.Fatal(err)
	}
	stmt, err := tx.Prepare("delete from restaurant where id=?")
	if err != nil {
		log.Fatal(err)
	}
	defer stmt.Close()
	_, err = stmt.Exec(rest.id)
	if err != nil {
		log.Fatal(err)
	}
	tx.Commit()
}

func atoi(s string) int {
	d, err := strconv.Atoi(s)
	if err != nil {
		log.Panicln(err)
		return 0
	}
	return d
}

func (rest Resto) put() { //update

}

func handleResto(res http.ResponseWriter, req *http.Request) {
	vars := mux.Vars(req)
	r := Resto{atoi(vars["id"]), vars["name"]}
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

func main() {
	init_db()

	r := mux.NewRouter()
	r.Headers("Content-Type", "application/json; charset=utf-8")
	r.HandleFunc("/restaurants", handleResto)
	r.HandleFunc("/restaurants/{id}", handleResto)
	r.HandleFunc("/restaurants/{id}/{name}", handleResto)
	r.HandleFunc("/restaurants/{name}", handleResto)
	http.Handle("/", r)

	log.Println("Starting and listening on ", port, "...")
	err := http.ListenAndServe(":"+port, nil)
	if err != nil {
		log.Fatal("Error: %v", err)
	}
	sqdb.Close()
}
