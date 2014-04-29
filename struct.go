package main

import (
	"log"
)

type Reservation struct {
	Id          int
	Is_finished bool
}

type User struct {
	Id         int
	First_name string
	Last_name  string
	Email      string
	Phone      string
	Login      string
}

type Order struct {
	Id int
}

type Meal struct {
	Id    int
	Name  string
	Price int
}

type Table struct {
	Id      int
	Is_free bool
	Seats   int
}

type Resto struct {
	Id       int
	Name     string
	Address  string
	Position string
}

func (r Table) setID(id int) Global {
	r.Id = id
	return r
}

func (r Table) getID() int {
	return r.Id
}

func (r Resto) setID(id int) Global {
	r.Id = id
	return r
}

func (r Resto) getID() int {
	return r.Id
}

func (r Reservation) setID(id int) Global {
	r.Id = id
	return r
}

func (r Reservation) getID() int {
	return r.Id
}
func (r User) setID(id int) Global {
	r.Id = id
	return r
}

func (r User) getID() int {
	return r.Id
}

func (r Order) setID(id int) Global {
	r.Id = id
	return r
}

func (r Order) getID() int {
	return r.Id
}

func (r Meal) setID(id int) Global {
	r.Id = id
	return r
}

func (r Meal) getID() int {
	return r.Id
}

func (r Resto) getAll() []Global {
	var glo []Resto
	_, err := dbmap.Select(&glo, "select * from restos")
	checkErr(err, "Select failed")
	log.Println("All rows:", glo)
	ret := make([]Global, len(glo))
	for x, p := range glo {
		log.Printf("    %d: %v\n", x, p)
		ret[x] = p
	}
	return ret
}

func (r Table) getAll() []Global {
	var glo []Table
	_, err := dbmap.Select(&glo, "select * from tables")
	checkErr(err, "Select failed")
	log.Println("All rows:", glo)
	ret := make([]Global, len(glo))
	for x, p := range glo {
		log.Printf("    %d: %v\n", x, p)
		ret[x] = p
	}
	return ret
}

func (r Reservation) getAll() []Global {
	var glo []Reservation
	_, err := dbmap.Select(&glo, "select * from reservations")
	checkErr(err, "Select failed")
	log.Println("All rows:", glo)
	ret := make([]Global, len(glo))
	for x, p := range glo {
		log.Printf("    %d: %v\n", x, p)
		ret[x] = p
	}
	return ret
}

func (r User) getAll() []Global {
	var glo []User
	_, err := dbmap.Select(&glo, "select * from users")
	checkErr(err, "Select failed")
	log.Println("All rows:", glo)
	ret := make([]Global, len(glo))
	for x, p := range glo {
		log.Printf("    %d: %v\n", x, p)
		ret[x] = p
	}
	return ret
}

func (r Order) getAll() []Global {
	var glo []Order
	_, err := dbmap.Select(&glo, "select * from orders")
	checkErr(err, "Select failed")
	log.Println("All rows:", glo)
	ret := make([]Global, len(glo))
	for x, p := range glo {
		log.Printf("    %d: %v\n", x, p)
		ret[x] = p
	}
	return ret
}

func (r Meal) getAll() []Global {
	var glo []Meal
	_, err := dbmap.Select(&glo, "select * from meals")
	checkErr(err, "Select failed")
	log.Println("All rows:", glo)
	ret := make([]Global, len(glo))
	for x, p := range glo {
		log.Printf("    %d: %v\n", x, p)
		ret[x] = p
	}
	return ret
}
