package main

import (
	"bytes"
	"encoding/json"
	_ "github.com/mattn/go-sqlite3"
	"io/ioutil"
	"log"
	"net/http"
)

func get_all_gcm_id() []string {
	var gmcids []string
	var users []User
	sql_req := "select * from `user`"
	_, err := dbmap.Select(users, sql_req)
	if err != nil {
		log.Println(err)
	}
	for _, user := range users {
		gmcids = append(gmcids, user.GCM_ID)
	}
	return gmcids
}

func send_gcm(gcmid []string, msg string) error {
	reqGCM := RequestGCM{
		GCM_API_KEY, gcmid,
		map[string]string{"test": msg}}
	data, err := json.Marshal(reqGCM)
	if err != nil {
		log.Println(err)
		return err
	}
	log.Println("send this to google ", string(data))

	client := &http.Client{}

	req, _ := http.NewRequest(
		"POST",
		"https://android.googleapis.com/gcm/send",
		bytes.NewReader(data))
	req.Header.Add("Content-Type", "application/json")
	req.Header.Add("Authorization", "key="+reqGCM.API_Key)
	resp, err := client.Do(req)
	if err != nil {
		log.Println(err)
	}
	defer resp.Body.Close()
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		log.Println(err)
	}
	log.Println("Body ", string(body))
	return err
}

func handleGCM(res http.ResponseWriter, req *http.Request) {
	p := make([]byte, req.ContentLength)

	res.Header().Set("Access-Control-Allow-Origin", "*")
	res.Header().Set("Content-Type", "application/json; charset=utf-8")
	res.Header().Set("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE")

	switch req.Method {
	case "GET":

	case "POST":
		_, err := req.Body.Read(p)
		if err != nil {
			log.Println(err)
		} else {
			send_gcm(get_all_gcm_id(), string(p))
		}
	case "PUT":
	case "DELETE":
	}
}
