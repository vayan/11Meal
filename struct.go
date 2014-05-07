package main

import ()

type Reservation struct {
	Id   int `db:"id"`
	User int `db:"user"`
	//Guest          []User `db:"guest"`
	Restaurant     int    `db:"restaurant"`
	Date           string `db:"date"`
	State          int    `db:"state"`
	PayementMethod int    `db:"payementmethod"`
}

type User struct {
	Id         int    `db:"id"`
	First_name string `db:"first_name"`
	Last_name  string `db:"last_name"`
	Email      string `db:"email"`
	Phone      string `db:"phone"`
	PhoneUID   string `db:"phoneuid"`
	GCM_ID     string `db:"gcmid"`
	Login      string `db:"login"`
}

type Order struct {
	Id          int `db:"id"`
	Total_price int `db:"total_price"`
	User        int `db:"user"`
	Reservation int `db:"reservation"`
}

type UserMeal struct {
	Id       int `db:"id"`
	Order    int `db:"order"`
	Meal     int `db:"meal"`
	Quantity int `db:"quantity"`
}

type Meal struct {
	Id         int    `db:"id"`
	Restaurant int    `db:"restaurant"`
	Name       string `db:"name"`
	Type       int    `db:"type"`
	Price      int    `db:"price"`
}

type Table struct {
	Id         int  `db:"id"`
	Restaurant int  `db:"restaurant"`
	Is_free    bool `db:"is_free"`
	Seats      int  `db:"seats"`
}

type Restaurant struct {
	Id          int    `db:"id"`
	Name        string `db:"name"`
	Address     string `db:"address"`
	Position    string `db:"position"`
	Schedule    string `db:"schedule"`
	Phone       string `db:"phone"`
	Description string `db:"description"`
}

type RequestGCM struct {
	API_Key          string            `json:"-"`
	Registration_ids []string          `json:"registration_ids"`
	Data             map[string]string `json:"data"`
}
