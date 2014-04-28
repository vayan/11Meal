package main

import (
	"github.com/gorilla/mux"
	"log"
	"net/http"
)

func main() {
	dbmap = init_db()
	defer dbmap.Db.Close()

	r := mux.NewRouter()
	r.Headers("Content-Type", "application/json; charset=utf-8")
	r.HandleFunc("/restaurants", handleResto)
	r.HandleFunc("/restaurants/{id}", handleResto)
	r.HandleFunc("/restaurants/{id}/{name}", handleResto)
	r.HandleFunc("/restaurants/{name}", handleResto)

	r.HandleFunc("/table", handleTable)
	r.HandleFunc("/table/{id}", handleTable)

	r.HandleFunc("/reservation", handleReservation)
	r.HandleFunc("/reservation/{id}", handleReservation)

	r.HandleFunc("/users", handleUsers)
	r.HandleFunc("/users/{id}", handleUsers)

	r.HandleFunc("/orders", handleOrder)
	r.HandleFunc("/orders/{id}", handleOrder)

	r.HandleFunc("/meal", handleMeal)
	r.HandleFunc("/meal/{id}", handleMeal)

	http.Handle("/", r)

	log.Println("Starting and listening on ", port, "...")
	err := http.ListenAndServe(":"+port, nil)
	checkErr(err, "Failed to start server")
}
