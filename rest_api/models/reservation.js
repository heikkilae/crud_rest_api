var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var ReservationSchema = new Schema({
    date: {
        type: Date,
        default: Date.now
    },
    type: {
        type: String,
        default: "Unknown"
    },
    duration: {
        type: Number
    },
    state: {
        type: String,
        default: 'Pending'
    },
    user: { 
        type: mongoose.Schema.Types.ObjectId, 
        ref: 'User'
    },
    turnNumber: {
        type: Number,
        default: 0
    }
})

// Custom Class Methods
ReservationSchema.statics.addReservation = function addReservation(req, res, callback) {
    if(!req.body.userId) {
        return callback(false, "User id not given");
	}
    else 
    {
		// Get user by given id
		User.findById({_id: req.body.userId}, function(err, user) {  
			if(err) 		
                return callback(err.toString());
			if(!user) {
				return callback(false, "User not found");
			} else {
                var Reservation = mongoose.model('Reservation', ReservationSchema)
                var reservation = new Reservation(req.body);
                Reservation.findLastReservation(function (lastReservation) {
                    // Raise turn number if needed
                    if(lastReservation)
                        reservation.turnNumber = lastReservation.turnNumber+1;

                    reservation.save(function(err, reservation) {
                        // Create new reservation
                        if (err)
                            return callback(err.toString());
                        
                        else if(reservation) {							
                            // Add reservation to user
                            user.reservations.push(reservation);
                            user.save();

                            return callback(false, reservation);
                        }
                    })
                
                })
            }
		})
	}
}

ReservationSchema.statics.findLastReservation = function (callback) {
    this.findOne()
      .sort({turnNumber: -1})
      .exec(function(err, reservation) {
        if(err)
            return callback(0)
        else return callback(reservation)
      });
  }

module.exports = mongoose.model('Reservation', ReservationSchema);