module.exports = function(app) {
    var user = require('../controllers/user');

    // User routes
    app.route('/users')
        .get(user.getAll)
        .post(user.create);
		  
    app.route('/users/:userId')
        .get(user.get)
        .put(user.update)
        .delete(user.remove);
		
	app.route('/login')
        .post(user.login);	
		
	app.route('/logout/:userId')
        .post(user.logout);	
};