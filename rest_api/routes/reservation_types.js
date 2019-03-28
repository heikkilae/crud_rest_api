module.exports = function(app) {
    var reservation_types = require('../controllers/reservation_types');

    // Reservation routes
    app.route('/reservations_types')
        .get(reservation_types.getAll)
        .post(reservation_types.create);

    app.route('/reservations_types/:reservationId')
        .get(reservation_types.get)
        .put(reservation_types.update)
        .delete(reservation_types.remove);
};