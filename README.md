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

Allows a personalInfo to be inserted into the H2 DB

Admin-only

### GET
_/v1/get-personalInfo/{personId}_

Allows one personalInfo to be selected based on ID.

Only search on ID.

Path Variable.

### DELETE

_/v1/delete-personalInfo/{personId}_

admin user only

Path Variable

## Database

### PersonalInfo
Database representation of a personalInfo.

### Address
1:Many relationship with PersonalInfo. One personalInfo can have many addresses.

### Club
Many:Many relationship with PersonalInfo. Each personalInfo can belong to many clubs and clubs contain multiple people.

## Bonus

### Swagger

### Unit Tests