var mongoose = require('mongoose'),
    User = mongoose.model('User');

exports.getAll = function(req, res) {
    User.find({}).
    populate('reservations').
    exec(function(err, users) {
        if (err) {
			res.json({ error: true, data: err });
			return
		}   
        res.json({ error: false, data: users});
    });
};

exports.create = function(req, res) {
   var user = new User(req.body);
   user.save(function(err, user) {
        if (err) {
			res.json({ error: true, data: err });
			return
		}   
        res.json({ error: false, data: user });
   })
};

exports.get = function(req, res) {
    User.findById({_id: req.params.userId}).
    populate('reservations').
    exec(function(err, user) {
        if (err) {
			res.json({ error: true, data: err });
			return
		}   
        res.json({ error: false, data: user });
    });
};

exports.update = function(req, res) {
    User.findOneAndUpdate({_id: req.params.userId}, req.body, {new: true})
     .populate('reservations').exec(function(err, user) {
        if (err) {
			res.json({ error: true, data: err });
			return
		}   
        res.json({ error: false, data: user });
    });
};

exports.remove = function(req, res) {
    User.remove({_id: req.params.userId}, function(err, user) {
        if (err) {
			res.json({ error: true, data: err });
			return
		}   
        res.json({ error: false, data: 'User successfully removed' });
    });
};

exports.login = function(req, res) {
     User.findOneAndUpdate({email: req.body.email, password: req.body.password}, {loggedIn: true}, {new: true}).
    populate('reservations').
    exec( function(err, user) {
        if (err) {
			res.json({ error: true, data: err });
			return
		}   
		if(user)
			res.json({ error: false, data: user});
		else
			res.json({ error: true, data: 'User not found.'});
    });
};

exports.logout = function(req, res) {
     User.findOneAndUpdate({_id: req.params.userId}, {loggedIn: false}, {new: true}).
    populate('reservations').
    exec( function(err, user) {
        if (err) {
			res.json({ error: true, data: err });
			return
		}   
		if(user)
			res.json({ error: false, data: 'Successfully logged out.'});
		else
			res.json({ error: true, data: 'User not found.'});
    });
};
