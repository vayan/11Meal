package main

import ()

type Global interface {
	setID(id int) Global
	getID() int
}

func get(r Global) {
	log.Println("before selected ", r, "is type ", reflect.TypeOf(r))
	obj, err := dbmap.Get(reflect.ValueOf(r).Interface(), r.getID())
	checkErr(err, "get resto failed")
	//rest = *obj.(*Global)
	log.Println("after selected ", obj)
}

// func getAll(r Global, tp string) []Global {
//  restos := reflect.Zero(reflect.SliceOf(reflect.TypeOf(r))).Interface()
//  log.Println("get all resto ")
//  _, err := dbmap.Select(&restos, "select * from ? order by Id", tp)
//  checkErr(err, "getAll rest failed")
//  log.Println("All rows:")
//  g := make([]Global, len(restos))
//  for x, p := range restos {
//      log.Printf("    %d: %v\n", x, p)
//      g[x] = p
//  }
//  return g
// }

func add(r Global) {
	log.Println("add new resto")
	err := dbmap.Insert(r)
	checkErr(err, "Insert resto failed")
	log.Println("new resto added", r)
}

func handleglobal(res http.ResponseWriter, req *http.Request) {
	vars := mux.Vars(req)
	var obj Global

	switch vars["table"] {
	case "restos":
		obj = new(Resto)
	case "tables":
		obj = new(Table)
	}
	switch req.Method {
	case "POST":
		err := req.ParseForm()
		checkErr(err, "Error parse form")
		err = decoder.Decode(obj, req.PostForm)
		checkErr(err, "Error decode form")
		add(obj)
	case "GET":
		d, err := strconv.Atoi(vars["id"])
		if err != nil {
			//test := getAll(obj, vars["table"])
			//log.Println("result ", test)
		} else {
			log.Println("seting id to ", d)
			obj = obj.setID(d)
			log.Println("id is now ", obj.getID())
			get(obj)
		}
	case "DELETE":
		d := atoi(vars["id"])
		obj.setID(d)
		//obj.del()

	case "PUT":
		d := atoi(vars["id"])
		err := req.ParseForm()
		checkErr(err, "Error parse form")
		err = decoder.Decode(obj, req.PostForm)
		checkErr(err, "Error decode form")
		obj.setID(d)
		//obj.put()
	}
	data, _ := json.Marshal("{'request':'success'}")
	res.Write(data)
}
