package main

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"log"
	"net/http"
)

// https://developers.google.com/places/documentation/search#PlaceSearchRequests

func get_data() {
	log.Println("=== Start getting data...")
	_, err := get_place_nearby()
	if err != nil {
		log.Panicln(err)
	}
	log.Println("=== Data restored")
}

func get_place_nearby() ([]RestaurantGoogle, error) {
	var restos []RestaurantGoogle
	var res interface{}
	var data []byte
	location := "-33.8670522,151.1957362"

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
