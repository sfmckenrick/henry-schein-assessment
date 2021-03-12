# Scott McKenrick

## Spring Boot Application

### Authentication

* JWT

    * public user
    * Readonly

* Basic Auth
    * Read and Write
    * Admin user

### POST

_/v1/post-person_

Allows a person to be inserted into the H2 DB

Admin-only

### GET
_/v1/get-person/{personId}_

Allows one person to be selected based on ID.

Only search on ID.

Path Variable.

### DELETE

_/v1/delete-person/{personId}_

admin user only

Path Variable

## Database

### PersonalInfo
Database representation of a person.

### Address
1:Many relationship with PersonalInfo. One person can have many addresses.

### Club
Many:Many relationship with PersonalInfo. Each person can belong to many clubs and clubs contain multiple people.

## Bonus

### Swagger

### Unit Tests