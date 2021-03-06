# foretell
Just foretell the weather in NYC 

## Release notes for version 36254f
* Spoof a User-Agent value in the HTTP request header to circumvent an HTTP Status Code 403 (that occurs without the spoofed User-Agent).
* Rename the Android package to fluently convey what the app does: `foretell.weather`

## Release notes for version 432fd5
* Fix a bug (in which HTTP status code 301 was received, but no image) caused by the URL not redirecting and NOAA enforcing HTTPS.
* Remove excess padding above and below image.

## Relase notes for version 30991b
* Simplify project structure and speed-up build time by removing gradle.
* Temporarily bypass gomobile bindings. 
* Remove use of web-views, which can erroneously force web-browsers to open.

## Release notes for version c058750
* Move the bar charts to just below the operating system's status bar.
* Add superflous wind and aviation bar charts, just because.

## Release notes for version faa3461
* Shows bar charts that represent the 24-hour weather forecast.
* Uses web services provided by the National Oceanographic and Atmospheric Administration of the United States to obtain charts to display
* Features an application name that:
  * Says what the application does.
  * Is likely at the top of application lists sorted alphabetically by name on most devices.
  * Has excessive use of diacritical marks on the unicode codepoints that comprise the name.

```
> Hallway tester: "Is it... interactive in any way?"
>> Author: "No. Neat, right?"
```

```
> Hallway tester 2: "That's awesome. Can you make it changeable by zip code?"
>> Author: "Nah. I'd have to add a toolbar, a menu button, app settings or cached data, etc. 
>> It'd easily triple the amount of code, or more."
> Hallway tester 2: "Yeah. As soon as a some kind of form for input gets added, 
> it gets complicated and also really tedious."
```
