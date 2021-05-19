# AndroidFinalProject

**REQUIREMENT:** Android Studio IDE

This was a group project divided into four parts: 

•	Developing software in a group environment, including using Github to merge code into 1 project. 
•	Dividing workload to meet deadlines. 
•	Designing modular software that allows for that division of work.

My Part (**Name:** Wilker Fernandes de Sousa **Github:** fern0097)
**Covid-19 case data**

•	The user can type in a Country to query, along with a date to query. The server will return the number of cases by province/state, which should be put in a list. The user can save the country’s results by date to the database for later viewing.
•	Use this URL to fetch Covid-19 case data: https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z . Change the word “Canada” to another country, and the dates from= and to=
•	There should be a menu to show all of the saved dates to the device. Selecting on a saved result should show the number of cases by province. There should be a button to delete a saved result from the database. 
•	Use SharedPreferences to save the last query for the next time you run the application.

My Teammate Part (**Name:** Rodrigo C. M. Tavares **Github:** rodtavares-ac):
**TicketMaster event search**

•	The user can search for upcoming events near a given city. The user can enter the city name, and a search radius for the events. The server will return a list of events scheduled in that city. Your program should show the list of the names of the events. Clicking on an event name should show the starting date, the price range of tickets, the URL from ticketmaster, and the promotional image. The user should also be able to save the event’s details in a database of saved events. 
•	The SharedPreferences should save the name of the last city that was searched. It should display that city name the next time the application is launched.
•	There should be an option to view the list of saved events. Clicking on the name of an event should show the same information as from the server. However this time there should be a button to delete the city from the list of favourites.
•	You should sign up for an API key from this website: https://developer-acct.ticketmaster.com/user/register. The API looks like: https://app.ticketmaster.com/discovery/v2/events.json?apikey=XXXXXX&city=YYYYY&radius=100
•	You would replace the XXXX with your API key that you signed up for, city is the city where the events will be taking place, and the radius is how far from the city the event is taking place to be included. 

My Teammate Part (**Name:** Adriano Reckziegel **Github:** reck0014):
**The Audio Database api**

•	The user can search for musical artists using the api: https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=XXXX
 but replace XXX with the artist that the user enters.
•	The results from the server is a list of albums which should be put in a list. If the user selects one of the albums, get the album details by calling https://theaudiodb.com/api/v1/json/1/track.php?m=2159699, but replace the album ID with what the idAlbum of what the user selected. This will return a list of songs on the album which you should show a details page. This details page should have a button for saving the album information in the database. Each song name should be on a button that if a user clicks, the application should launch a web browser to a google search for the artist name and song title using the URL: http://www.google.com/search?q=YYY+ARTIST+NAME   but replace YYY with the artist name and artist name.
•	There should be a button that lets the user see the list of saved album names. Clicking on a saved album should show the list of songs, and each song should be on a button that launches a google search for that song. There should be a delete button instead of a save button.

My Teammate Part (**Name:** Ariane Nogueira **Github:** arianenogueira):
**Recipe Search page**

•	The user can enter the name of a recipe to search. The server returns a list of recipes that match the search terms. You should just show the Titles of the recipe in a list. If the user selects one of the recipes, there should be a details page which shows the Title, ingredients and URL to that recipe. Clicking on that URL should launch a browser that loads that URL. There should also be a Save button to save the recipe details on the device. 
•	There should be a “favourites” button that shows a list of saved recipes. Selecting a title from the list of saved recipes should show the saved details. This saved details page should have a delete button that removes it from the saved recipe database.
•	This API doesn’t need a key to access it. It is located here: http://www.recipepuppy.com/api/?i=onions,garlic&q=omelet&p=3
•	The. The format is: q=XXXXX, which is the search term. i=YYYY is the ingredients that the recipe must include. There is also a format=ZZZZ, which will be either XML, or JSON depending on what format you want the information.
•	The SharedPreferences should save the last recipe that was searched and then show it the next time the application is launched.
