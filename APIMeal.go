package main

import (
	"database/sql"
	"encoding/json"
	"fmt"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
	"os"
)

var (
	port = "8181"
	sqdb *sql.DB
)

func init_db() {
	os.Remove("./foo.db")
	db, err := sql.Open("sqlite3", "./foo.db")
	if err != nil {
		log.Fatal(err)
	}
	sql := `
create table foo (id integer not null primary key, name text);
delete from foo;
`
	_, err = db.Exec(sql)
	if err != nil {
		log.Printf("%q: %s\n", err, sql)
		return
	}
	sqdb = db
}

func handleIndex(res http.ResponseWriter, req *http.Request) {
	data, _ := json.Marshal("{'connected':'true'}")
	res.Header().Set("Content-Type", "application/json; charset=utf-8")
	res.Write(data)
}

func handleAdd(res http.ResponseWriter, req *http.Request) {
	tx, err := sqdb.Begin()
	if err != nil {
		log.Fatal(err)
	}
	stmt, err := tx.Prepare("insert into foo(id, name) values(?, ?)")
	if err != nil {
		log.Fatal(err)
	}
	defer stmt.Close()
	for i := 0; i < 100; i++ {
		_, err = stmt.Exec(i, fmt.Sprintf("dd %03d", i))
		if err != nil {
			log.Fatal(err)
		}
	}
	tx.Commit()
}

func handleGet(res http.ResponseWriter, req *http.Request) {
	rows, err := sqdb.Query("select id, name from foo")
	if err != nil {
		log.Fatal(err)
	}
	defer rows.Close()
	for rows.Next() {
		var id int
		var name string
		rows.Scan(&id, &name)
		fmt.Println(id, name)
	}
	rows.Close()
}

func main() {
	init_db()
	log.Println("Starting and listening on ", port)
	http.HandleFunc("/", handleIndex)
	http.HandleFunc("/add", handleAdd)
	http.HandleFunc("/get", handleGet)

	err := http.ListenAndServe(":"+port, nil)
	if err != nil {
		log.Fatal("Error: %v", err)
	}
	sqdb.Close()
}
