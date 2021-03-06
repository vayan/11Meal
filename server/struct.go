package main

import ()

type Reservation struct {
	Id             int    `db:"id"`
	User           int    `db:"user"`
	GuestCSV       string `db:"guest" json:"-"`
	Guests         []User `db:"-"`
	Restaurant     int    `db:"restaurant"`
	Date           string `db:"date"`
	State          int    `db:"state"`
	PayementMethod int    `db:"payementmethod"`
}

type Promo struct {
	Id          int    `db:"id"`
	Name        string `db:"name"`
	Description string `db:"description"`
	P100_reduc  int    `db:"p100reduc"`
	Restaurant  int    `db:"restaurant"`
}

type User struct {
	Id         int    `db:"id"`
	First_name string `db:"first_name"`
	Last_name  string `db:"last_name"`
	Email      string `db:"email"`
	Phone      string `db:"phone"`
	PhoneUID   string `db:"phoneuid"`
	GCM_ID     string `db:"gcmid"`
	GPS        string `db:"gps"`
	Login      string `db:"login"`
	Password   string `db:"password"`
}

type Order struct {
	Id          int    `db:"id"`
	Total_price int    `db:"total_price"`
	User        int    `db:"user"`
	MealCSV     string `db:"meals" json:"-"`
	Meals       []Meal `db:"-"`
	Restaurant  int    `db:"restaurant"`
	Reservation int    `db:"reservation"`
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
	Id          int     `db:"id"`
	UID         string  `db:"uid"`
	Name        string  `db:"name"`
	Address     string  `db:"address"`
	Position    string  `db:"position"`
	Lat         float64 `db:"lat"`
	Lng         float64 `db:"lng"`
	Schedule    string  `db:"schedule"`
	Phone       string  `db:"phone"`
	Description string  `db:"description"`
	Promos      []Promo `db:"-"`
}

type RequestGCM struct {
	API_Key          string            `json:"-"`
	Registration_ids []string          `json:"registration_ids"`
	Data             map[string]string `json:"data"`
}

type RestaurantGoogle struct {
	Formatted_address string      `json:"formatted_address"`
	Vicinity          string      `json:"vicinity"`
	Geometry          interface{} `json:"geometry"`
	Icon              string      `json:"icon"`
	Id                string      `json:"id"`
	Name              string      `json:"name"`
	Rating            float32     `json:"rating"`
	Reference         string      `json:"reference"`
	Types             []string    `json:"types"`
}
