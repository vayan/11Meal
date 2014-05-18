package main

import (
	"log"
	"strconv"
	"strings"
)

func (o *Restaurant) PromoUnserialize() {
	var u []Promo
	sql_req := "select * from `promo` where restaurant =" + strconv.Itoa(o.Id)
	log.Println(sql_req)
	_, err := dbmap.Select(&u, sql_req)
	if err != nil {
		log.Println(err)
	}
	o.Promos = u
}

func (r *Reservation) GuestUnserialize() {
	var u []User
	sql_req := "select * from `user` where id in (" + r.GuestCSV + ")"
	log.Println(sql_req)
	_, err := dbmap.Select(&u, sql_req)
	if err != nil {
		log.Println(err)
	}
	r.Guests = u
}

func (r *Reservation) GuestSerialize() {
	var csv_guest []string
	for _, user := range r.Guests {
		if user.Id > 0 {
			csv_guest = append(csv_guest, strconv.Itoa(user.Id))
		}
	}
	r.GuestCSV = strings.Join(csv_guest, ",")
}

func (o *Order) MealUnserialize() {
	var u []Meal
	sql_req := "select * from `meal` where id in (" + o.MealCSV + ")"
	log.Println(sql_req)
	_, err := dbmap.Select(&u, sql_req)
	if err != nil {
		log.Println(err)
	}
	o.Meals = u
}

func (o *Order) MealSerialize() {
	var csv_meal []string
	for _, meal := range o.Meals {
		if meal.Id > 0 {
			csv_meal = append(csv_meal, strconv.Itoa(meal.Id))
		}
	}
	o.MealCSV = strings.Join(csv_meal, ",")
}
