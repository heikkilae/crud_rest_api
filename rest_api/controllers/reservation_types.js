var mongoose = require('mongoose'),
    ReservationTypes = mongoose.model('ReservationTypes');

exports.getAll = function(req, res) {
    ReservationTypes.find({},function(err, reservationtypes) {
        if (err)
            res.send(err);
        res.json({ error: false, data: reservationtypes});
    });
};

exports.create = function(req, res) {
   var reservationtypes = new ReservationTypes(req.body);
   reservationtypes.save(function(err, reservationtypes) {
       if (err)
            res.send(err);
        res.json({ error: false, data: reservationtypes });
   })
};

exports.get = function(req, res) {
    ReservationTypes.findById({_id: req.params.reservationId},function(err, reservationtypes) {
        if (err)
            res.send(err);
        res.json({ error: false, data: reservationtypes });
    });
};

exports.update = function(req, res) {
    ReservationTypes.findOneAndUpdate({_id: req.params.reservationId}, req.body, 
        {new: true}, function(err, reservationtypes) {
        if (err)
            res.send(err);
        res.json({ error: false, data: reservationtypes });
    });
};

exports.remove = function(req, res) {
    ReservationTypes.remove({_id: req.params.reservationId}, function(err, reservationtypes) {
        if (err)
            res.send(err);
        res.json({ error: false, data: 'Reservartion type successfully removed' });
    });
};
