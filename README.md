Scott McKenrick
REST
This Application contains 2 RESTful controllers.

PersonManagement
POST
/v1/post-person

Allows a person to be inserted into the H2 DB

Permission: Write

GET
/v1/get-person/{personId}

Allows one person to be selected based on ID.

Permission: Read Write

DELETE
/v1/delete-person/{personId}

Deletes a person based on ID.

Permission: Write

Authentication
POST
/v1/auth/token

Request a token for a specific user.

Role: User

Endpoint Security
JWT
Only accessible for User Role

Basic
Only accessible for Admin Role

Database
PersonalInfo
Database representation of a person.

Address
1:Many relationship with PersonalInfo. One person can have many addresses.

Club
Many:Many relationship with PersonalInfo. Each person can belong to many clubs and clubs contain multiple people.
