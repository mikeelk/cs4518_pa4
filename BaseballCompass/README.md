Names: Justin Fletcher and Mike Elkinson

# Our App
Our app is a baseball compass which queries a baseball API to get a list of major league games today and displays how close the user's location is to the various baseball stadiums with games today.
Please note the API only queries spring training games right now since those are the games happening. So all of the stadiums are in either Florida or Arizona since that is where spring training games happen.
Our app assist the user to find the nearest baseball stadium with a game happening and direct them to that stadium. The intended idea is if you are in the same city and walkable to a stadium the app will give you heading towards the stadium. 

# Features
* Uses Internet permissions to query the MLB API
* Uses location permissions to get user's latitude and longitude. Please make sure to first use the Google Maps app to get the current location so the location will not be null in our app
* Uses the accelerometer and the gyroscope sensor to determine the phone's heading to display correct heading towards nearest stadium
* Uses RoomDatabase to store latitude and longitude of stadiums so it doesn't need to be re-queried every time the user starts the app
* Uses WorkManager to push a notification to the user every 16 minutes if they are near a stadium
* ViewModel Architecture with model data classes, a viewmodel class with a UI state, and a view layer with UI components and screens
* Bottom bar navigation to three different screens: a home screen with the main compass feature, a how it works screen, a privacy by design screen, and a settings screen


For adaptive layout, it should still be aligned when you change the orientation
It should not break
Fill up as much room as possible
Will use a sensible phone size to test it
Don't have to worry about tablets
Can say in readme file what size of phone we used to test it

Note that I use Medium Phone with 1080 x 2400 resolution in pixels with API 36.1

# Future Work