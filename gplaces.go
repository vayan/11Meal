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

// TODO : go func something to upload in DB and handle multiple page of result

func get_data() {
	log.Println("=== Start getting data...")
	location := "39.9075000,116.3972300"
	h := GetMD5Hash(location)
	if !is_cached(h) {
		log.Println("Get data from google")
		err := get_place_nearby(location, h, "")
		if err != nil {
			log.Panicln(err)
		}
	}
	log.Println("=== Data restored")
}

func restaurantGoogleinDB(restos []RestaurantGoogle, h string) {
	log.Println("Adding", len(restos), "restaurants in db")
	for _, resto := range restos {
		// use resto.Geometry for location
		descr := strings.Join(resto.Types, ", ")
		r := Restaurant{
			Name:        resto.Name,
			Address:     resto.Formatted_address,
			Id_request:  h,
			Description: descr}
		add(&r)
	}
}

func get_place_nearby(location string, h string, pagetoken string) error {
	var restos []RestaurantGoogle
	var res interface{}
	var data []byte

	client := &http.Client{}
	url := "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&radius=50000&types=restaurant&sensor=false&key=" + GPLACES_API_KEY

	if len(pagetoken) > 1 {
		log.Println("loading new result page")
		url += "&pagetoken=" + pagetoken
	}

	req, _ := http.NewRequest("GET", url, nil)
	resp, err := client.Do(req)
	if err != nil {
		log.Println(err)
		return err
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err)
		return err
	}
	err = json.Unmarshal(body, &res)
	if err != nil {
		log.Println(err)
		return err
	}
	m := res.(map[string]interface{})
	if m["next_page_token"] != nil {
		get_place_nearby(location, h, m["next_page_token"].(string))
	} else if m["status"].(string) == "INVALID_REQUEST" && len(pagetoken) > 1 {
		log.Println("Wait between request...")
		get_place_nearby(location, h, pagetoken)
	}
	if m["status"].(string) == "OK" {
		data, err = json.Marshal(m["results"])
		if err != nil {
			log.Println(err)
			return err
		}
		err = json.Unmarshal(data, &restos)
		if err != nil {
			log.Println(err)
			return err
		}
		go restaurantGoogleinDB(restos, h)
	} else {
		return errors.New(ErrorAPI[m["status"].(string)])
	}
	return nil
}
