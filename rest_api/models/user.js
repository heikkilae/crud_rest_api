var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UserSchema = new Schema({
    email: {
        type: String,
        require: 'Email is used to login'
    },
    password: {
        type: String,
        require: 'Password is required'
    },
    forename: {
        type: String,
        require: 'First name is required'
    },
    surname: {
        type: String,
        require: 'Last name is required'
    },
    role: {
        type: String,
        default: 'client'
    },
    loggedIn: {
        type: Boolean,
        default: false
    },
    identityNumber: {
        type: String,
        require: 'Identity number is required'
    },
    address: {
        type: String,
    },
    zipCode: {
        type: Number
    },
    phone: {
        type: String
    },
    reservations: [{ 
        type: Schema.Types.ObjectId, ref: 'Reservation'
    }]
})

module.exports = mongoose.model('User', UserSchema);