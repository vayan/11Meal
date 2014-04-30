package main

import ()

type Reservation struct {
	Id          int  `db:"id"`
	User        int  `db:"user"`
	Restaurant  int  `db:"restaurant"`
	Is_finished bool `db:"is_finished"`
}

type User struct {
	Id         int    `db:"id"`
	First_name string `db:"first_name"`
	Last_name  string `db:"last_name"`
	Email      string `db:"email"`
	Phone      string `db:"phone"`
	Login      string `db:"login"`
}

type Order struct {
	Id          int    `db:"id"`
	Total_price int    `db:"total_price"`
	User        int    `db:"user"`
	Reservation int    `db:"reservation"`
	Meals       string `db:"meals"`
}

type Meal struct {
	Id         int    `db:"id"`
	Restaurant int    `db:"restaurant"`
	Name       string `db:"name"`
	Price      int    `db:"price"`
}

type Table struct {
	Id         int  `db:"id"`
	Restaurant int  `db:"restaurant"`
	Is_free    bool `db:"is_free"`
	Seats      int  `db:"seats"`
}

type Restaurant struct {
	Id       int    `db:"id"`
	Name     string `db:"name"`
	Address  string `db:"address"`
	Position string `db:"position"`
}
