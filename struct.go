package main

import ()

type Reservation struct {
	Id          int
	Id_user     int
	Id_resto    int
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
	Id             int
	Total_price    int
	Id_user        int
	Id_reservation int
	Meals          string
}

type Meal struct {
	Id      int
	Id_rest int
	Name    string
	Price   int
}

type Table struct {
	Id      int
	Id_rest int
	Is_free bool
	Seats   int
}

type Resto struct {
	Id       int
	Name     string
	Address  string
	Position string
}
