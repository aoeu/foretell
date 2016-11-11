package noaa

import (
	"errors"
	"fmt"
	"net/url"
	"strconv"
)

// http://forecast.weather.gov/MapClick.php?lat=40.7&lon=-73.95&unit=0&lg=english&FcstType=graphical

// http://forecast.weather.gov/meteograms/Plotter.php?lat=40.6447&lon=-73.9472&wfo=OKX&zcode=NYZ075&gset=20&gdiff=10&unit=0&tinfo=EY5&ahour=0&pcmd=11010111111110100000000000000000000000000000000000000000000&lg=en&indu=1!1!1!&dd=&bw=&hrspan=24&pqpfhr=6&psnwhr=6

type latitude float64

func newLatitude(degrees float64) (latitude, error) {
	if degrees > 90 || degrees < -90 {
		return latitude(0.0), errors.New(fmt.Sprintf("Invalid value for degrees of latitude: %v", degrees))
	}
	return latitude(degrees), nil
}

func (l latitude) String() string {
	return strconv.FormatFloat(float64(l), 'f', 3, 64)
}

type longitude float64

func newLongitude(degrees float64) (longitude, error) {
	if degrees > 180 || degrees < -180 {
		return longitude(0.0), errors.New(fmt.Sprintf("Invalid value for degrees of longitude: %v", degrees))
	}
	return longitude(degrees), nil
}

func (l longitude) String() string {
	return strconv.FormatFloat(float64(l), 'f', 3, 64)
}

type coordinate struct {
	latitude
	longitude
}

func (c coordinate) MeteogramURL() (*url.URL, error) {
	v, err := url.ParseQuery("")
	if err != nil {
		return &url.URL{}, err
	}

	v.Set("gset", "20")
	v.Set("gdiff", "10")
	v.Set("unit", "0")
	v.Set("ahour", "0")
	v.Set("tinfo", "EY5")
	v.Set("pcmd", "11011111111110111") // TODO(aoeu): The final three high bits represent "show visibility, ceiling height, and fog...."
	v.Set("lg", "en")
	v.Set("hrspan", "24")

	v.Set("lat", c.latitude.String())
	v.Set("lon", c.longitude.String())
	v.Set("wfo", "OKX")
	v.Set("zcode", "NYZ075")

	return url.Parse("http://forecast.weather.gov/meteograms/Plotter.php?" + v.Encode())
}

func (c coordinate) meteogramConfiguratorURL() (*url.URL, error) {
	v, err := url.ParseQuery("unit=0&lg=english&FcstType=graphical")
	if err != nil {
		return &url.URL{}, err
	}
	v.Set("lat", c.latitude.String())
	v.Set("lon", c.longitude.String())
	return url.Parse("http://forecast.weather.gov/MapClick.php?" + v.Encode())
}

var (
	Bushwick = coordinate{
		latitude(40.7),
		longitude(-73.95),
	}

	NewYorkCity = coordinate{
		latitude(40.704),
		longitude(-73.946),
	}
)

func NYCMeteogramURL() string {
	u, err := NewYorkCity.MeteogramURL()
	if err != nil {
		panic(err)
	}
	return u.String()
}