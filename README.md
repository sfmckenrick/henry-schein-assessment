# Scott McKenrick
Take-home assessment for Sr. SW Engineer for Henry Schein

## REST
This Application contains 2 RESTful controllers.

### PersonManagement
#### POST
_/v1/post-person_

Allows a person to be inserted into the H2 DB

Permission: Write

#### GET
_/v1/get-person/{personId}_

Allows one person to be selected based on ID.

Permission: Read Write

#### DELETE
_/v1/delete-person/{personId}_

Deletes a person based on ID.

Permission: Write

### Authentication
#### POST
_/v1/auth/token_

Request a token for a specific user.

Role: User

## Endpoint Security
### JWT
Only accessible for User Role

### Basic
Only accessible for Admin Role

## Database
### PersonalInfo
Database representation of a person.

### Address
1:Many relationship with PersonalInfo. One person can have many addresses.

### Club
Many:Many relationship with PersonalInfo. Each person can belong to many clubs and clubs contain multiple people.
