package main

import (
	"encoding/json"
	"errors"
	"github.com/gorilla/mux"
	"io/ioutil"
	"log"
	"net/http"
	"strconv"
	"strings"
)

// https://developers.google.com/places/documentation/search#PlaceSearchRequests

func handleGPS(res http.ResponseWriter, req *http.Request) {
	var data []byte
	var err error = nil
	vars := mux.Vars(req)

	res.Header().Set("Access-Control-Allow-Origin", "*")
	res.Header().Set("Content-Type", "application/json; charset=utf-8")
	res.Header().Set("Access-Control-Allow-Methods", "GET")

	location := vars["coord"]
	m_distance := 200.
	if len(vars["distance"]) > 1 {
		m_distance_t, _ := strconv.ParseFloat(vars["distance"], 64)
		if m_distance_t > 200 {
			m_distance = m_distance_t
		}
	}
	restos := get_close_restaurant(location, m_distance)
	data, err = json.Marshal(restos)
	if err != nil {
		http.Error(res, err.Error(), 400)
	} else {
		res.Write(data)
	}
}

func isInDB(UID string) bool {
	count, err := dbmap.SelectInt("select count(*) from restaurant where uid=?", UID)
	if err != nil {
		log.Println("isInDB", err)
	}
	if count > 0 {
		return true
	}
	return false
}

func get_restaurant_nearby(loc string, m_distance float64) []Restaurant {
	var resto []Restaurant
	coord := strings.Split(loc, ",")

	///////////// NOT PRECISE
	long_deg_per_m := 0.00010406 //degrees per meter
	lat_deg_per_m := 0.000008999 //degrees per meter

	long_distance := long_deg_per_m * m_distance
	lat_distance := lat_deg_per_m * m_distance

	lat, _ := strconv.ParseFloat(coord[0], 64)
	lng, _ := strconv.ParseFloat(coord[1], 64)
	var _, err = dbmap.Select(&resto,
		"select * from restaurant where lat between ? and ? and lng between ? and ?", lat-lat_distance, lat+lat_distance, lng-long_distance, lng+long_distance)
	if err != nil {
		log.Println(err)
	}
	return resto
}

func get_close_restaurant(loc string, m_distance float64) []Restaurant {
	log.Println("Start getting restaurant data...")
	restos := get_restaurant_nearby(loc, m_distance)
	if len(restos) < 10 {
		log.Println("Not enough data in DB, let's ask Google API more stuff...")
		err := get_place_nearby(loc, "")
		if err != nil {
			log.Println(err)
			return []Restaurant{}
		}
		return get_restaurant_nearby(loc, m_distance)
	}
	log.Println("Enough data in DB, we didn't ask Google API.")
	return restos
}

func putGoogleRestaurantInDB(restos []RestaurantGoogle) {
	i := 0
	for _, resto := range restos {
		if isInDB(resto.Id) {
			continue
		}
		loc := resto.Geometry.(map[string]interface{})["location"].(map[string]interface{})
		var lat float64 = -1
		var lng float64 = -1
		latlong := ""
		if loc != nil && loc["lat"] != nil && loc["lng"] != nil {
			lat = loc["lat"].(float64)
			lng = loc["lng"].(float64)
			latlong = strconv.FormatFloat(lat, 'f', 6, 64) + "," + strconv.FormatFloat(lng, 'f', 6, 64)
		}
		descr := strings.Join(resto.Types, ", ")
		r := Restaurant{
			Name:        resto.Name,
			Address:     resto.Vicinity,
			Position:    latlong,
			Lat:         lat,
			Lng:         lng,
			UID:         resto.Id,
			Description: descr}
		add(&r)
		i++
	}
	log.Printf("%d/%d restaurant(s) added in DB", i, len(restos))
}

func get_place_nearby(location string, pagetoken string) error {
	var restos []RestaurantGoogle
	var res interface{}
	var data []byte

	client := &http.Client{}
	url := "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location + "&types=restaurant&rankby=distance&sensor=false&key=" + GPLACES_API_KEY

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
		get_place_nearby(location, m["next_page_token"].(string))
	} else if m["status"].(string) == "INVALID_REQUEST" && len(pagetoken) > 1 {
		log.Println("Wait between request...")
		get_place_nearby(location, pagetoken)
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
		putGoogleRestaurantInDB(restos)
	} else {
		return errors.New(ErrorAPI[m["status"].(string)])
	}
	return nil
}
