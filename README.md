11Meal
======

# How to install :

 * Install go : http://golang.org/
 * Create a new folder somewhere e.g : /foo/bar
 * Create an env with this folder GOPATH=/foo/bar
 * `go get` all the dep. (see below)
 * `cd $GOPATH`
 * `mkdir src/github.com/vayan/`
 * `cd src/github.com/vayan/`
 * `git clone git@github.com:vayan/11Meal.git`
 * `cd 11Meal`
 * `git checkout backend-master`
 * `cd $GOPATH`
 * Create a config file (see server.cfg.example)
 * `go install github.com/vayan/11Meal && ./bin/11Meal path/to/config/file.cfg`

 and bravo the project is running :)

# Dep :

`go get github.com/coopernurse/gorp`

`go get github.com/gorilla/mux`

`go get github.com/gorilla/schema`

`go get github.com/mattn/go-sqlite3`

`go get github.com/jimlawless/cfg`
