from .auth_controller import auth
from .catalog_controller import catalog
from .recipe_controller import recipe
from .review_controller import review


# registering controller
# this file for list
# all controller


def init_controller(app):
    app.register_blueprint(auth)
    app.register_blueprint(catalog)
    app.register_blueprint(review)
    app.register_blueprint(recipe)