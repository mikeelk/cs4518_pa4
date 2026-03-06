Names: Justin Fletcher and Mike Elkinson

# Our App
Our app is a baseball compass which queries a baseball API to get a list of major league games today and displays how close the user's location is to the various baseball stadiums with games today.
Please note the API only queries spring training games right now since those are the games happening. So all of the stadiums are in either Florida or Arizona since that is where spring training games happen.
Our app assist the user to find the nearest baseball stadium with a game happening and directs them to that stadium. The intended idea is if you are in the same city and walkable to a stadium the app will give you heading towards the stadium. 
Please note that we tested this app on a Medium Phone (1080 x 2400 resolution in pixels) with API 36.1.

# Features
* Uses Internet permissions to query the MLB API
* Uses location permissions to get user's latitude and longitude. Please make sure to first use the Google Maps app to get the current location so the location will not be null in our app
* Uses the accelerometer and the gyroscope sensor to determine the phone's heading to display correct heading towards nearest stadium
* Uses RoomDatabase to store latitude and longitude of stadiums so it doesn't need to be re-queried every time the user starts the app
* Uses WorkManager to push a notification to the user every 15 minutes if they are near a stadium
* ViewModel Architecture with model data classes, a viewmodel class with a UI state, and a view layer with UI components and screens
* Bottom bar navigation to three different screens: a home screen with the main compass feature, a how it works screen, a privacy by design screen, and a settings screen
* Custom theme including custom colors and fonts
* Adaptive layout by using LazyColumns to ensure all information is rendered in any phone orientation

# A Note On Notifications
When the app loads it sends out a notification. The intended behavior would be to send out a notification every day at 12 PM and 6 PM local time, but we did not program this in because it would not be feasible to test. 
Instead we use WorkManager to check every 15 minutes in the background to send a notification. 15 minutes is the minimum time for a periodic work manager in order to save battery life. 
Rather than have the tester have the app open for 15 minutes we added a button in the settings which triggers the work manager. 

# Future Work
* Make animated compass
* Add support for regular season stadiums
* Only send out notification if the user is within 10 miles of a stadium
* Consider game start time (so don't list stadium if game has already started or is already over when user opens app)

# References
* https://developer.android.com/develop/ui/compose/text/fonts
* https://developer.android.com/develop/ui/compose/graphics/images/loading
* https://developer.android.com/develop/background-work/background-tasks/persistent/getting-started/define-work
* CityExplorer Project
* PersistentShake Project
* Weather App Project
* Lecture 16 Slides
* Lecture 18 Slides