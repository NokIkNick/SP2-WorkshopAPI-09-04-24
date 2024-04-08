# SP2-WorkshopAPI-09-04-24

## AUTHORS:

- Patrick Fabrin | email: cph-pf73@cphbuisiness.dk
  
- Christian Høj | email: cph-ch633@cphbusiness.dk

- Nicolai Theis Rolin | email: cph-nr140@cphbusiness.dk

- Anders Jensen | email: cph-aj539@cphbusiness.dk

- Nicklas Waldemar Seier Winther | email: cph-nw89@cphbusiness.dk

## Description:
A custom made API, created to be implemented into applications that wishes to manage events and workshops. The API comes with built in security, in the form of secured routing, encrypted user data and JWT tokens.

## EER DIAGRAM:
<img src="/SP2-WorkshopAPI/src/main/java/groupone/images/event.png" width="600" alt="EER Diagram">


## Technical Requirements:

    JPA
    JPQL
    Maven
    JDK 17^
    JUnit 5
    Docker
    PostgresSQL 42.7.3^
    pgAdmin
    Lombok
    Javalin 5.5.0^
    Jackson-databind 2.15.0^
    Jackson-datatype 2.13.0^
    Hibernate 6.2.4^
    RestAssured 5.3.2^
    Hamcrest-library 1.3^
    Nimbus-jose-jwt 9.0.1^
    jbcrypt 0.4^


## Security Template Used:
[NokIkNick - RESTSECURITY - TEMPLATE](https://github.com/NokIkNick/RESTSECURITY)

## API DOCUMENTATION:
### Scroll to the right to see the rest of the table:

<table>
<tr>
<th>HTTP Method</th>
<th>REST Ressource</th>
<th>Response / Request Payload</th>
<th>Comment</th>
<th>Role</th>
</tr>
<tr>
<td>GET</td>
<td>/api</td>
<td>{"Message" : "Connected Successfully"}</td>
<td>Notifies user  of the establised connection</td>
<td>No Role</td>
</tr>
</tr>
<tr>
<td>GET</td>
<td>/events</td>
<td>
    
```json
[{
        "id": 1,
        "title": "SchoolHacks",
        "description": "A chance to hack your way to more learning",
        "price": 0,
        "imageUrl": "https://www.workshop.schoolhacks.eu/logo.png",
        "createdAt": [
            2024,
            4,
            8
        ],
        "updatedAt": [
            2024,
            4,
            8
        ],
        "deletedAt": null,
        "users": [

        ],
        "locations": [{
            "id": 1,
            "street": "Firskovvej 18",
            "eventSpecifications": {
                "id": 1,
                "date": "2024-04-08",
                "time": "12:11:02.460",
                "duration": 30.0,
                "instructorName": "Jörg Oertel",
                "instructorEmail": "jorg@cphbusiness.dk",
                "status": "UPCOMING",
                "category": "WORKSHOP",
                "capacity": 50
            },
            "zipcode": {
                "city": "Lyngby",
                "zip": 2800
            }
        }]
    },
    ...
]
```

</td>
<td>Fetches all upcoming events, with their respective information.</td>
<td>STUDENT, INSTRUCTOR, ADMIN</td>
</tr>

<td>GET</td>
<td>/events/category/{category}</td>
<td>
    
```json
[{
    "id": 2,
    "title": "Skolefest",
    "description": "Årets skolefest",
    "price": 149.95,
    "imageUrl": "https://www.getCoolLogo.dk/logo.png",
    "createdAt": [
        2024,
        4,
        8
    ],
    "updatedAt": [
        2024,
        4,
        8
    ],
    "deletedAt": null,
    "users": [{
        "email": "Gennevieve@student.com",
        "password": "$2a$10$iNQZlnMmOZSQzhmMe22xcuJcy7CBJEnnoDkBwNPwq3fwLzCXoFc2y",
        "phone": 2312454566,
        "name": Gennevie Johnson,
        "roles": [
            "STUDENT"
        ]
    }],
    "locations": [{
        "id": 2,
        "street": "Firskovsvej 18",
        "eventSpecifications": {
            "id": 2,
            "date": "2024-04-08",
            "time": "14:00:26.525",
            "duration": 30.0,
            "instructorName": "CPHBusiness",
            "instructorEmail": "cph@cphbusiness.dk",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 400
        },
        "zipcode": {
            "city": "Lyngby",
            "zip": 2800
        }
    }]
    ...
}]
```

</td>
<td>Fetches all events, with their respective information, by the category. 

<h5> Parameters:</h5>

> "category": A string input representing a category enum.

</td>
<td>STUDENT, INSTRUCTOR, ADMIN</td>
</tr>

</tr>

<td>GET</td>
<td>/events/instructing</td>
<td>
    
```json
[{
        "eventName": "Velkomstfest",
        "id": 1,
        "street": "Fredensvej 19",
        "eventSpecifications": {
            "id": 1,
            "date": "2024-04-08",
            "time": "21:42:25.735",
            "duration": 30.0,
            "instructorName": "Den vilde",
            "instructorEmail": "test@instructor.com",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 120
        },
        "zipcode": {
            "city": "Hørsholm",
            "zip": 2970
        }
    }
    ...
]
```

</td>
<td>Fetches all events, with their respective information, by the instructor email from the valid token. 

</td>
<td>INSTRUCTOR</td>
</tr>

</tr>

<td>GET</td>
<td>/events/status/{status}</td>
<td>
    
```json
[{
    "id": 1,
    "title": "testEvent",
    "description": "test med test på",
    "price": 99999.0,
    "imageUrl": "pictureUrl",
    "createdAt": [
        2024,
        4,
        8
    ],
    "updatedAt": [
        2024,
        4,
        8
    ],
    "deletedAt": null,
    "users": [{
        "email": "test@student.com",
        "password": "$2a$10$GHTvfo2hB9wLwg0ATHzUgelC9/93CurqawDpVrCMNcUUehW90KIZK",
        "phone": 456765353,
        "name": null,
        "roles": [
            "STUDENT"
        ]
    }],
    "locations": [{
        "id": 1,
        "street": "Fredensvej 19",
        "eventSpecifications": {
            "id": 1,
            "date": "2024-04-08",
            "time": "21:42:24.501",
            "duration": 30.0,
            "instructorName": "Den vilde",
            "instructorEmail": "test@instructor.com",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 30
        },
        "zipcode": {
            "city": "Hørsholm",
            "zip": 2970
        }
    }]
    ...
}]
```

</td>
<td>Fetches all events, with their respective information, by the status parameter. 

<h5> Parameters:</h5>

> "status": String input representing a status enum.

</td>
<td>STUDENT, INSTRUCTOR, ADMIN</td>
</tr>

</tr>

<td>GET</td>
<td>/events/instructing</td>
<td>
    
```json
[{
        "eventName": "Velkomstfest",
        "id": 1,
        "street": "Fredensvej 19",
        "eventSpecifications": {
            "id": 1,
            "date": "2024-04-08",
            "time": "21:42:25.735",
            "duration": 30.0,
            "instructorName": "Den vilde",
            "instructorEmail": "test@instructor.com",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 120
        },
        "zipcode": {
            "city": "Hørsholm",
            "zip": 2970
        }
    }
    ...
]
```

</td>
<td>Fetches all events, with their respective information, by the instructor email from the valid token. 

</td>
<td>INSTRUCTOR</td>
</tr>

</tr>

<td>GET</td>
<td>/events/{id}</td>
<td>
    
```json
{
    "id": 2,
    "title": "testEvent2",
    "description": "test med test på",
    "price": 99999.0,
    "imageUrl": "pictureUrl",
    "createdAt": [
        2024,
        4,
        8
    ],
    "updatedAt": [
        2024,
        4,
        8
    ],
    "deletedAt": null,
    "users": [{
            "email": "test@instructor.com",
            "password": "$2a$10$lHVkUn6pkT9ChSOZcYhlxOFOKI9NLoPT8Lo.9YJ8O76ZEPTt9SOA6",
            "phone": 456765353,
            "name": null,
            "roles": [
                "INSTRUCTOR"
            ]
        },
        {
            "email": "test@student.com",
            "password": "$2a$10$GHTvfo2hB9wLwg0ATHzUgelC9/93CurqawDpVrCMNcUUehW90KIZK",
            "phone": 456765353,
            "name": null,
            "roles": [
                "STUDENT"
            ]
        }
    ],
    "locations": [{
        "id": 2,
        "street": "Thyrasvej 4",
        "eventSpecifications": {
            "id": 2,
            "date": "2024-04-08",
            "time": "21:42:25.428",
            "duration": 30.0,
            "instructorName": "Den vilde",
            "instructorEmail": "test@instructor.com",
            "status": "UPCOMING",
            "category": "WORKSHOP",
            "capacity": 30
        },
        "zipcode": {
            "city": "Hørsholm",
            "zip": 2970
        }
    }]
}
```

</td>
<td>Fetches the event, and all it's attached information, by the parameter id. 

<h5> Parameters:</h5>

> "id": String input representing the id of an event, in the form of an integer.

</td>
<td>STUDENT, INSTRUCTOR, ADMIN</td>
</tr>

</tr>

<td>GET</td>
<td>/events/{id}/users</td>
<td>
    
```json
[{
    "email": "test@student.com",
    "password": "$2a$10$GHTvfo2hB9wLwg0ATHzUgelC9/93CurqawDpVrCMNcUUehW90KIZK",
    "phone": 456765353,
    "name": "PatrickStudent",
    "roles": [
        "STUDENT"
    ]
    ...
}]
```

</td>
<td>Fetches the users, and all their attached information, from a specific event, by the parameter id. 

<h5> Parameters:</h5>

> "id": String input representing the id of an event, in the form of an integer.

</td>
<td>INSTRUCTOR</td>
</tr>

</tr>

<td>POST</td>
<td>/events/create</td>
<td>

<h4>Response:</h4>

```json
{
    "id": 3,
    "title": "newTestEvent",
    "description": "This is a new test event!",
    "price": 100.0,
    "imageUrl": "www.moretexturl.com",
    "createdAt": [
        2024,
        4,
        3
    ],
    "updatedAt": [
        2024,
        4,
        8
    ],
    "deletedAt": null,
    "users": [

    ],
    "locations": [{
        "id": 3,
        "street": "test-street-wahoo",
        "eventSpecifications": {
            "id": 3,
            "date": "2024-05-05",
            "time": "16:30",
            "duration": 2.0,
            "instructorName": "Peter Maker",
            "instructorEmail": "Peter@Maker.dk",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 1200
        },
        "zipcode": {
            "city": null,
            "zip": 2970
        }
    }, ]
}
```

<h4>Request Payload:</h4>

```json
{
    "title": "newTestEvent",
    "description": "This is a new test event!",
    "price": 100.0,
    "createdAt": "2024-04-03",
    "updatedAt": "2024-04-03",
    "imageUrl": "www.moretexturl.com",
    "locations": [{
        "street": "test-street-wahoo",
        "eventSpec": {
            "date": "2024-05-05",
            "time": "16:30",
            "duration": 2,
            "instructorName": "Peter Maker",
            "instructorEmail": "Peter@Maker.dk",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 1200
        },
        "zipcodes": {
            "zip": 2700
        }
    }, ]
}
```

</td>
<td>Creates an event based on the request payloads body.


</td>
<td>INSTRUCTOR</td>
</tr>

</tr>

<td>PUT</td>
<td>/events/update/{id}</td>
<td>

<h4>Response:</h4>

```json
{
    "id": 1,
    "title": "something new",
    "description": "test med test på",
    "price": 99999.0,
    "imageUrl": "pictureUrl",
    "createdAt": [
        2024,
        4,
        8
    ],
    "updatedAt": [
        2024,
        4,
        8
    ],
    "deletedAt": null,
    "users": [{
        "email": "test@student.com",
        "password": "$2a$10$GHTvfo2hB9wLwg0ATHzUgelC9/93CurqawDpVrCMNcUUehW90KIZK",
        "phone": 456765353,
        "name": null,
        "roles": [
            "STUDENT"
        ]
    }],
    "locations": [{
        "id": 1,
        "street": "new Street",
        "eventSpecifications": {
            "id": 1,
            "date": "2024-04-08",
            "time": "21:42:25.066",
            "duration": 30.0,
            "instructorName": "Den vilde",
            "instructorEmail": "test@instructor.com",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 30
        },
        "zipcode": {
            "city": "Brønshøj",
            "zip": 2700
        }
    }]
}
```

<h4>Request Payload:</h4>

```json
{
    "title": "something new",
    "locations": [{
        "street": "new Street",
        "zipcode": {
            "zip": 2700
        }
    }]
}
```

</td>
<td>Updates an event based on the request payload body, and the parameter id

<h5> Parameters:</h5>

> "id": String input representing the id of an event, in the form of an integer.


</td>
<td>INSTRUCTOR</td>
</tr>

</tr>

<td>PUT</td>
<td>/events/{id}/{address}/{zip}/cancel</td>
<td>

```json
{
    "Message": "Successfully cancelled event: testEvent2 at all locations. Date of deletion is: 2024-04-08"
}
```


</td>
<td>Updates an event's locations to 'cancelled' based on the parameter id, address and zip.

<h5> Parameters:</h5>

> "id": String input representing the id of an event, in the form of an integer.

> "address": String input representing the street of a specific location where the event is being held.

> "zip": String input representing the zip of the specific location where the event is being held, in the form of an integer.


</td>
<td>INSTRUCTOR</td>
</tr>


</tr>

<td>POST</td>
<td>/auth/login</td>
<td>

<h4>Response:</h4>

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJHcm91cCBvbmUiLCJzdWIiOiJwYXRyaWNrX3RoZV9hd2Vzb21lMl8xMjM0NTY3ODkxMEAxMjM0NTY3ODkxMC5kayIsImV4cCI6MTcxMjYxNzQ0NSwicm9sZXMiOiJJTlNUUlVDVE9SIiwidXNlcm5hbWUiOiJwYXRyaWNrX3RoZV9hd2Vzb21lMl8xMjM0NTY3ODkxMEAxMjM0NTY3ODkxMC5kayJ9.-jIOrwyFw4NsVn1mtqwf3kafDg4xcN-_tdg_LxQXXz0",
    "username": "patrick_the_awesome2_12345678910@12345678910.dk"
}
```

<h4>Request Payload:</h4>

```json
{
"email":"patrick_the_awesome2_12345678910@12345678910.dk",
"password":"123456789"

}
```

</td>
<td>Logins the user and generates a JWT Token.


</td>
<td>ANYONE</td>
</tr>

</tr>

<td>POST</td>
<td>/auth/register</td>
<td>

<h4>Response:</h4>

```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJHcm91cCBvbmUiLCJzdWIiOiJwYXRyaWNrX3RoZV9hd2Vzb21lMl8xMjM0NTY3ODkxMEAxMjM0NTY3ODkxMC5kayIsImV4cCI6MTcxMjYxNzQ0NSwicm9sZXMiOiJJTlNUUlVDVE9SIiwidXNlcm5hbWUiOiJwYXRyaWNrX3RoZV9hd2Vzb21lMl8xMjM0NTY3ODkxMEAxMjM0NTY3ODkxMC5kayJ9.-jIOrwyFw4NsVn1mtqwf3kafDg4xcN-_tdg_LxQXXz0",
    "username": "patrick_the_awesomest_person_12345678910@12345678910.dk"
}
```

<h4>Request Payload:</h4>

```json
{
"email":"patrick_the_awesomest_person_12345678910@12345678910.dk",
"password":"123456789",
"phone":"88888888",
"name":"random_test"
}
```

</td>
<td>Registers a user based on the request payload body and generates a JWT Token.


</td>
<td>ANYONE</td>
</tr>

</tr>

<td>POST</td>
<td>/auth/request/password/reset?email</td>
<td>

```json
{
    "Message":"A password request has been requested. If you haven't requested a reset, you can safely ignore this message. You wouldn't receive any request for the next 24 hours, unless the link is used. http://servername7008/api/auth/reset/password/=guid=randomUniqueIDuserEmail. Click here to reset your password"
}
```

</td>
<td>Requests a password reset with the user email, and send the request to the email.

<h5> Parameters:</h5>

> "email": Query param String input representing the email of a user.


</td>
<td>ANYONE</td>
</tr>

</tr>

<td>POST</td>
<td>/auth/reset/password?email?guid</td>
<td>

```json
{
    "Message":"Your password has been changed to this temporary one. Your temporary password: generatedpassword. If you haven't requested a reset, your email has been compromised. Change your emails password and request a new password change."
}
```

</td>
<td>Reset the password and generate a temporary password.

<h5> Parameters:</h5>

> "email": Query param String input representing the email of a user.

> "guid": Query param String input representing the randomUniqueId of a request.


</td>
<td>ANYONE</td>
</tr>


</tr>

<td>PUT</td>
<td>/student/toevent/{id}</td>
<td>

```json
[{
    "id": 2,
    "title": "testEvent",
    "description": "test med test på",
    "price": 99999.0,
    "imageUrl": "pictureUrl",
    "createdAt": [
        2024,
        4,
        9
    ],
    "updatedAt": [
        2024,
        4,
        9
    ],
    "deletedAt": null,
    "users": [{
        "email": "test@student.com",
        "password": "$2a$10$CtafU5OWyDicIPPP5JpT9eh163r67IT2v7DCyQH2J0iAqcY8fGJjK",
        "phone": 456765353,
        "name": "PatrickStudent",
        "roles": [
            "STUDENT"
        ]
    }],
    "locations": [{
        "id": 2,
        "street": "Fredensvej 19",
        "eventSpecifications": {
            "id": 2,
            "date": "2024-04-09",
            "time": "01:25:17.063",
            "duration": 30.0,
            "instructorName": "Den vilde",
            "instructorEmail": "denVilde@hej.com",
            "status": "UPCOMING",
            "category": "EVENT",
            "capacity": 30
        },
        "zipcode": {
            "city": "Hørsholm",
            "zip": 2970
        }
    }]
    ...
}, ]
```

</td>
<td>Registers a user for the specific event specified by the id.

<h5> Parameters:</h5>

> "id": Path parameter String input representing the id of an event that the user wants to be registered for, represented as an integer.


</td>
<td>STUDENT</td>
</tr>

</tr>

<td>PUT</td>
<td>/student/remove_event/{id}</td>
<td>

```json
{
    "id": 2,
    "title": "testEvent2",
    "description": "test med test på",
    "price": 99999.0,
    "imageUrl": "pictureUrl",
    "createdAt": [
        2024,
        4,
        9
    ],
    "updatedAt": [
        2024,
        4,
        9
    ],
    "deletedAt": null,
    "users": [{
        "email": "test@instructor.com",
        "password": "$2a$10$rQA7ab8Tu4bAPXP/edvd/urTOnXhPl8XtEk2WlOa0JHoPGbKzRwoG",
        "phone": 456765353,
        "name": null,
        "roles": [
            "INSTRUCTOR"
        ]
    }],
    "locations": [{
        "id": 2,
        "street": "Thyrasvej 4",
        "eventSpecifications": {
            "id": 2,
            "date": "2024-04-09",
            "time": "01:25:15.728",
            "duration": 30.0,
            "instructorName": "Den vilde",
            "instructorEmail": "test@instructor.com",
            "status": "UPCOMING",
            "category": "WORKSHOP",
            "capacity": 30
        },
        "zipcode": {
            "city": "Hørsholm",
            "zip": 2970
        }
    }]
}
```

</td>
<td>Removes an event from the user, the event removed is specified by the id.

<h5> Parameters:</h5>

> "id": Path parameter String input representing the id of an event that the user wants to be removed from, represented as an integer.


</td>
<td>STUDENT</td>
</tr>

</tr>

<td>GET</td>
<td>/admin/get_all_events</td>
<td>

```json
[{
        "id": 1,
        "title": "testEvent",
        "description": "test med test på",
        "price": 99999.0,
        "imageUrl": "pictureUrl",
        "createdAt": [
            2024,
            4,
            9
        ],
        "updatedAt": [
            2024,
            4,
            9
        ],
        "deletedAt": null,
        "users": [{
            "email": "test@student.com",
            "password": "$2a$10$A3SPwdiobEmf7Jsv0CQYE.zk8eIGku5J5S7p3v3Zr/UVjklzmmpE.",
            "phone": 456765353,
            "name": null,
            "roles": [
                "STUDENT"
            ]
        }],
        "locations": [{
            "id": 1,
            "street": "Fredensvej 19",
            "eventSpecifications": {
                "id": 1,
                "date": "2024-04-09",
                "time": "01:25:15.228",
                "duration": 30.0,
                "instructorName": "Den vilde",
                "instructorEmail": "test@instructor.com",
                "status": "UPCOMING",
                "category": "EVENT",
                "capacity": 30
            },
            "zipcode": {
                "city": "Hørsholm",
                "zip": 2970
            }
        }]
    },
    {
        "id": 2,
        "title": "testEvent2",
        "description": "test med test på",
        "price": 99999.0,
        "imageUrl": "pictureUrl",
        "createdAt": [
            2024,
            4,
            9
        ],
        "updatedAt": [
            2024,
            4,
            9
        ],
        "deletedAt": null,
        "users": [{
                "email": "test@instructor.com",
                "password": "$2a$10$rQA7ab8Tu4bAPXP/edvd/urTOnXhPl8XtEk2WlOa0JHoPGbKzRwoG",
                "phone": 456765353,
                "name": null,
                "roles": [
                    "INSTRUCTOR"
                ]
            },
            {
                "email": "test@student.com",
                "password": "$2a$10$A3SPwdiobEmf7Jsv0CQYE.zk8eIGku5J5S7p3v3Zr/UVjklzmmpE.",
                "phone": 456765353,
                "name": null,
                "roles": [
                    "STUDENT"
                ]
            }
        ],
        "locations": [{
            "id": 2,
            "street": "Thyrasvej 4",
            "eventSpecifications": {
                "id": 2,
                "date": "2024-04-09",
                "time": "01:25:15.228",
                "duration": 30.0,
                "instructorName": "Den vilde",
                "instructorEmail": "test@instructor.com",
                "status": "UPCOMING",
                "category": "WORKSHOP",
                "capacity": 30
            },
            "zipcode": {
                "city": "Hørsholm",
                "zip": 2970
            }
        }]
    }
    ...
]
```

</td>
<td>Fetches all events.


</td>
<td>ADMIN</td>
</tr>

</tr>

<td>GET</td>
<td>/admin/get_all_users</td>
<td>

```json
[{
        "email": "test@student.com",
        "password": "$2a$10$CtafU5OWyDicIPPP5JpT9eh163r67IT2v7DCyQH2J0iAqcY8fGJjK",
        "phone": 456765353,
        "name": "PatrickStudent",
        "roles": [
            "STUDENT"
        ]
    },
    {
        "email": "test@admin.com",
        "password": "$2a$10$uRMV17YLgyu6Dq0dipmgouY66fp.RiDpFQ7Mdca083BnRWsvdUK7S",
        "phone": 456765353,
        "name": "PatrickAdmin",
        "roles": [
            "ADMIN"
        ]
    },
    {
        "email": "test@instructor.com",
        "password": "$2a$10$1TmlUo7BAPKeRC1avMt.YuVqNa5udOvi0aLNbsy2VJNdcayUeDYtu",
        "phone": 456765353,
        "name": "PatrickInstructor",
        "roles": [
            "INSTRUCTOR"
        ]
    }
    ...
]
```

</td>
<td>Fetches all users.


</td>
<td>ADMIN</td>
</tr>


</table>
