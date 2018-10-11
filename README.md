# MoviesDB-APP


Min SDK Support: 24



The below points represents the technical brief about the built app.

Main Features: 
List of latest movies fetched from https://developers.themoviedb.org/3
Filter by date
Highlighted dates in filter calendar 
Clear filter
Endless scrolling
Move details screen with basic info along with link to the IMDB related web page.

Development Concept:
The application information design has been build based on SSOT (Single Source Of Truth) principle by making the local app. DB (Room) the single source of data for the application views.

Design Pattern: 
The application designed based on MVVM (Model View View Model) design pattern.

Used Architectural Components / Dependences 
Data Binding for binding UI to data source
Room DB for local caching
Lifecycle for Live Data / View Modeling 
RXJava2 / RXAndroid2
Retrofit2 for networking
Firebase-RemoteConfig for storing APIs base link & authorization key
GLIDE for image loading and caching
Timber for loggin


With more time, the below enhancements could be applied:

Apply constraint layout to all application views.
Add clear caching option in the main listing screen.
Doing some refactoring for generating the highlighted dates list upon clicking on filter option.
 


