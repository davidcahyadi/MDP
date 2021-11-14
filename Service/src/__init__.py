import os
from flask import Flask
from src.models.database import init_db
from src.controllers.controller import init_controller
from flask_sqlalchemy import SQLAlchemy


db: SQLAlchemy


def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True)
    app.debug = True
    if test_config is None:
        app.config.from_mapping(
            SECRET_KEY=os.environ.get("SECRET_KEY"),
            WTF_CSRF_ENABLED=False
        )
    else:
        app.config.from_mapping(test_config)

    # initiate database
    global db
    db = init_db(app)

    # register blueprint (controllers)
    init_controller(app)

    return app