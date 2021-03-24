# helth
Health tracker app Backend written in Kotlin, with Spring-Boot.

# Endpoint Definition
/user/[username] - Contains user specific data
<br>

/user/[username]/activity - Listing of all the user's activities
<br>
/user/[username]/activity/[activityID] - Specific information about a single activity
<br>

/user/[username]/challenge - Listing of all the user's challenges: active, completed, and expired
/user/[username]/challenge/[challengeID] - Specific information about a single user challenge
