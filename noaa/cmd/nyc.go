package main

import (
	"fmt"

	"github.com/aoeu/noaa"
)

func main() {
	u, err := noaa.NewYorkCity.MeteogramURL()
	if err != nil {
		panic(err)
	}
	fmt.Println(u.String())
}