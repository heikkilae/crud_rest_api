var mongoose = require('mongoose'),
    Reservation = mongoose.model('Reservation');

exports.getAll = function(req, res) {
    Reservation.find({}).
    populate('user', 'id').
    exec(function(err, reservations) {
        if (err)
            res.json({ error: true, data: err });
        res.json({ error: false, data: reservations});
    });
};

exports.create = function(req, res) {
	Reservation.addReservation(req, res, function(err, reservation){
		if(err)
		{
			res.json({ error: true, data: err});
		} else if(reservation) {
			res.json({ error: false, data: reservation});
		} else {
			res.json({ error: false, data: "Reservation not found"});
		}			
	});
};

exports.get = function(req, res) {
    Reservation.findById({_id: req.params.reservationId}, function(err, reservation) {
        if (err) {
            res.json({ error: true, data: err });
		} else if(reservation) {
			res.json({ error: false, data: reservation });
		} else {
			res.json({ error: false, data: "Reservation not found"});
		}
    });
};

exports.update = function(req, res) {
    Reservation.findOneAndUpdate({_id: req.params.reservationId}, req.body, {new: true}, function(err, reservation) {
        if (err) {
            res.json({ error: true, data: err });
		} else if (reservation) {
			res.json({ error: false, data: reservation });
		} else {
			res.json({ error: false, data: "Reservation not found" });
		}
    });
};

exports.remove = function(req, res) {
    Reservation.remove({_id: req.params.reservationId}, function(err, reservation) {
        if (err)
            res.json({ error: true, data: err });
		else
			res.json({ error: false, data: 'Reservation successfully removed' });
    });
};