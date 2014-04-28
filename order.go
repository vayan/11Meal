package main

import (
	_ "github.com/mattn/go-sqlite3"
	"net/http"
)

type Order struct {
	Id int
}

func handleOrder(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}
