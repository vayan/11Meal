package main

import (
	"github.com/coopernurse/gorp"
	"log"
	"strconv"
)

var (
	port  = "8181"
	dbmap *gorp.DbMap
)

func checkErr(err error, msg string) {
	if err != nil {
		log.Fatalln(msg, err)
	}
}

func atoi(s string) int {
	d, err := strconv.Atoi(s)
	if err != nil {
		log.Panicln(err)
		return 0
	}
	return d
}

func main() {
	dbmap = init_db()
	defer dbmap.Db.Close()

	start_web_server()

}
