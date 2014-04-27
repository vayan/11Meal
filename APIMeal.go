package main

import (
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
)

var (
	port = "8181"
)

func handleIndex(res http.ResponseWriter, req *http.Request) {

}

func main() {
	log.Println("Starting and listening on ", port)
	http.HandleFunc("/", handleIndex)

	err := http.ListenAndServe(":"+port, nil)
	if err != nil {
		log.Fatal("Error: %v", err)
	}

}
