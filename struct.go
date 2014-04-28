package main

type Reservation struct {
	Id          int
	Is_finished bool
}

type User struct {
	Id         int
	First_name string
	Last_name  string
	Email      string
	Phone      string
	Login      string
}

type Order struct {
	Id int
}

type Meal struct {
	Id    int
	Name  string
	Price int
}

type Table struct {
	Id      int
	Is_free bool
	Seats   int
}

type Resto struct {
	Id       int
	Name     string
	Address  string
	Position string
}

func (r Table) setID(id int) Global {
	r.Id = id
	return r
}

func (r Table) getID() int {
	return r.Id
}

func (r Resto) setID(id int) Global {
	r.Id = id
	return r
}

func (r Resto) getID() int {
	return r.Id
}
