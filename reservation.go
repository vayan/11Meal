package main

import (
	_ "github.com/mattn/go-sqlite3"
	"net/http"
)

type Reservation struct {
	Id          int
	Is_finished bool
}

func handleReservation(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}
