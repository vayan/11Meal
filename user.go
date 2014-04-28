package main

import (
	_ "github.com/mattn/go-sqlite3"
	"net/http"
)

type User struct {
	Id         int
	First_name string
	Last_name  string
	Email      string
	Phone      string
	Login      string
}

func handleUsers(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}
