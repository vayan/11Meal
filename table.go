package main

import (
	_ "github.com/mattn/go-sqlite3"
	"net/http"
)

type Table struct {
	Id      int
	Is_free bool
	Seats   int
}

func handleTable(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}
