# API Documentation

for first-time hackers
or just some notes to make it easier to remember


## Installation

Install Node
Install & configure MongoDB
```
Linux:
https://docs.mongodb.com/manual/tutorial/install-mongodb-on-debian/

Windows:
https://nodejs.org/en/download/
https://www.mongodb.com/download-center#community
https://code.msdn.microsoft.com/Mongo-Database-setup-on-6963f46f
https://docs.mongodb.com/tutorials/install-mongodb-on-windows/
```

Clone project files
```
git clone url of this repo
```

Open cmd (win) or terminal (linux)
Head to api folder under the project
Install any packages that it depends on.
```
npm install
```

Change the MongoDB address in server.js to whatever is yours
```
mongoose.connect('mongodb://localhost/Userdb');
```

And finally you are ready to run your API
```
npm run start
```

## Testing API

Lets test it via Postman:
Download & install Postman desktop client
```
https://www.getpostman.com/
```

Enter method as GET 
..and address of your API
After that hit 'Send'

On enter you jsut see "[]"
because database is empty
```
method: GET
address: localhost:3000/users/

result: []
```

To populate database use POST on the same address
Click body and select **"x-www-form-urlencoded"**
Then enter required fields as **key** and the corresponding **value**
```
method: POST
address: localhost:3000/users/
name: tester
password: test1234
forename: Matti
surname: Meikalainen
identityNumber: 199768-054R

result: {
    "role": "client",
    "loggedIn": false,
    "_id": "*********************",
    "password": "test1234",
    "forename": "Matti",
    "surname": "Meikalainen",
    "identityNumber": "199768-083G",
    "__v": 0
}
```

## Authors

* **Antti Heikkila**# crud_rest_api
