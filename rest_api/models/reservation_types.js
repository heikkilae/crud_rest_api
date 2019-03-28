var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var ReservationTypesSchema = new Schema({
    reservation_type: {
        type: String,
        default: "Unknown"
    }
})

module.exports = mongoose.model('ReservationTypes', ReservationTypesSchema);