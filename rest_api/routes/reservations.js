module.exports = function(app) {
    var reservation = require('../controllers/reservation');

    // Reservation routes
    app.route('/reservations')
        .get(reservation.getAll)
        .post(reservation.create);
		  
    app.route('/reservations/:reservationId')
        .get(reservation.get)
        .put(reservation.update)
        .delete(reservation.remove);	
};
