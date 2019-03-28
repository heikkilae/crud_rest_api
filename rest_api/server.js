var express = require('express'),
    app = express(),
    port = process.env.PORT = 3000,

// Requires
mongoose = require('mongoose');
bodyParser = require('body-parser');

// Models
User = require('./models/user')
Reservations = require('./models/reservation')
ReservationTypes = require('./models/reservation_types')

// mongoose instance connetion
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost/DBName');

// body-parser
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// routes
var routes = require('./routes/users');
routes(app); // register user routes to the app
var routes = require('./routes/reservations');
routes(app); // register reservation routes to the app
var routes = require('./routes/reservation_types');
routes(app); // register reservation routes to the app

app.use(function(req, res) {
    res.status(404).send({url: req.originalUrl + ' not found'})
  });
  
// Start server
app.listen(port)
console.log('Server started!')