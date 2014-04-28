package main

import (
	"encoding/json"
	"github.com/gorilla/mux"
	_ "github.com/mattn/go-sqlite3"
	"log"
	"net/http"
	"strconv"
)

type Resto struct {
	Id       int
	Name     string
	Address  string
	Position string
}

func (rest Resto) getAll() []Resto { //GET no param
	var restos []Resto
	log.Println("get all resto ")
	_, err := dbmap.Select(&restos, "select * from restos order by Id")
	checkErr(err, "getAll rest failed")
	log.Println("All rows:")
	for x, p := range restos {
		log.Printf("    %d: %v\n", x, p)
	}
	return restos
}

func (rest Resto) get() { //GET with ID
	log.Println("before selected ", rest)
	obj, err := dbmap.Get(Resto{}, rest.Id)
	checkErr(err, "get resto failed")
	rest = *obj.(*Resto)
	log.Println("after selected ", rest)
}

func (rest Resto) add() { //new one
	log.Println("add new resto")
	err := dbmap.Insert(&rest)
	checkErr(err, "Insert resto failed")
	log.Println("new resto added", rest)
}

func (rest Resto) del() { //delete
	log.Println("del resto", rest)
	count, err := dbmap.Delete(&rest)
	checkErr(err, "Del resto failed")
	log.Println("resto deleted:", count)
}

func (rest Resto) put() { //update
	log.Println("update resto", rest)
	count, err := dbmap.Update(&rest)
	checkErr(err, "Update resto failed")
	log.Println("resto updated:", count)
}

func handleResto(res http.ResponseWriter, req *http.Request) {
	vars := mux.Vars(req)
	log.Printf("recv %s : %s\n", req.Method, vars)
	switch req.Method {
	case "POST":
		err := req.ParseForm()
		r := new(Resto)
		checkErr(err, "Error parse form")
		err = decoder.Decode(r, req.PostForm)
		checkErr(err, "Error decode form")
		r.add()
	case "GET":
		d, err := strconv.Atoi(vars["id"])
		r := Resto{0, "", "", ""}
		if err != nil {
			r.getAll()
		} else {
			r.Id = d
			r.get()
		}
	case "DELETE":
		d := atoi(vars["id"])
		r := Resto{d, "", "", ""}
		r.del()
	case "PUT":
		d := atoi(vars["id"])
		err := req.ParseForm()
		r := new(Resto)
		checkErr(err, "Error parse form")
		err = decoder.Decode(r, req.PostForm)
		checkErr(err, "Error decode form")
		r.Id = d
		r.put()
	}
	data, _ := json.Marshal("{'request':'success'}")
	res.Write(data)
}
