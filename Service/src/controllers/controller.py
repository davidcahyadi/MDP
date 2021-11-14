from .auth import auth

# registering controller
# this file for list
# all controller


def init_controller(app):
    app.register_blueprint(auth)