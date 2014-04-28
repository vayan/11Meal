package main

import (
	_ "github.com/mattn/go-sqlite3"
	"net/http"
)

type Meal struct {
	Id    int
	Name  string
	Price int
}

func handleMeal(res http.ResponseWriter, req *http.Request) {
	switch req.Method {
	case "POST":

	case "GET":

	case "DELETE":

	case "PUT":

	}
}
