package main

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"log"
	"net/http"
	"strings"
)

// https://developers.google.com/places/documentation/search#PlaceSearchRequests

func is_cached(hash string) bool {
	i64, err := dbmap.SelectInt("select count(*) from restaurant where id_request=?", hash)
	if err != nil {
		log.Println(err)
		return false
	}
	if i64 > 0 {
		log.Println("data already in DB")
		return true
	}
	return false
}

func get_data() {
	log.Println("=== Start getting data...")
	location := "39.9075000,116.3972300"
	h := GetMD5Hash(location)
	if !is_cached(h) {
		log.Println("Get data from google")
		restos, err := get_place_nearby(location, h)
		if err != nil {
			log.Panicln(err)
		}
		for _, resto := range restos {
			descr := strings.Join(resto.Types, ", ")
			log.Println(resto.Formatted_address)
			r := Restaurant{
				Name:        resto.Name,
				Address:     resto.Formatted_address,
				Id_request:  h,
				Description: descr}
			add(&r)
		}
	}
	log.Println("=== Data restored")
}

func get_place_nearby(location string, h string) ([]RestaurantGoogle, error) {
	var restos []RestaurantGoogle
	var res interface{}
	var data []byte

	client := &http.Client{}
	url := "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&radius=50000&types=restaurant&sensor=false&key=" + GPLACES_API_KEY
	req, _ := http.NewRequest("GET", url, nil)
	resp, err := client.Do(req)
	if err != nil {
		log.Println(err)
		return restos, err
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err)
		return restos, err
	}
	err = json.Unmarshal(body, &res)
	if err != nil {
		log.Println(err)
		return restos, err
	}
	m := res.(map[string]interface{})
	if m["next_page_token"] != nil {
		log.Println("there is more pages")
	}
	if m["status"].(string) == "OK" {
		data, err = json.Marshal(m["results"])
		if err != nil {
			log.Println(err)
			return restos, err
		}
		err = json.Unmarshal(data, &restos)
		if err != nil {
			log.Println(err)
			return restos, err
		}
	} else {
		return restos, errors.New(ErrorAPI[m["status"].(string)])
	}
	return restos, nil
}
