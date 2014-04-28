package main

import ()

// func (r Resto) getAll() []Global { //GET no param
// 	var restos []Resto
// 	log.Println("get all resto ")
// 	_, err := dbmap.Select(&restos, "select * from restos order by Id")
// 	checkErr(err, "getAll rest failed")
// 	log.Println("All rows:")
// 	g := make([]Global, len(restos))
// 	for x, p := range restos {
// 		log.Printf("    %d: %v\n", x, p)
// 		g[x] = p
// 	}
// 	return g
// }

// func (rest Resto) get() { //GET with ID
// 	log.Println("before selected ", rest)
// 	obj, err := dbmap.Get(Resto{}, rest.Id)
// 	checkErr(err, "get resto failed")
// 	rest = *obj.(*Resto)
// 	log.Println("after selected ", rest)
// }

// func (rest Resto) add() { //new one
// 	log.Println("add new resto")
// 	err := dbmap.Insert(&rest)
// 	checkErr(err, "Insert resto failed")
// 	log.Println("new resto added", rest)
// }

// func (rest Resto) del() { //delete
// 	log.Println("del resto", rest)
// 	count, err := dbmap.Delete(&rest)
// 	checkErr(err, "Del resto failed")
// 	log.Println("resto deleted:", count)
// }

// func (rest Resto) put() { //update
// 	log.Println("update resto", rest)
// 	count, err := dbmap.Update(&rest)
// 	checkErr(err, "Update resto failed")
// 	log.Println("resto updated:", count)
// }
