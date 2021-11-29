import os
from flask_sqlalchemy import SQLAlchemy
db = SQLAlchemy()


def init_db(app):
    global db
    # constant
    host = os.environ.get("DB_HOST")
    username = os.environ.get("DB_USERNAME")
    password = os.environ.get("DB_PASSWORD")
    db = os.environ.get("DB_DATABASE")

    # setting up
    app.config["SQLALCHEMY_DATABASE_URI"] = f'mariadb+mariadbconnector://{username}:{password}@{host}/{db}'
    # app.config["SQLALCHEMY_ECHO"] = True
    app.config["SQLALCHEMY_TRACK_MODIFICATIONS"] = False
    app.config["SQLALCHEMY_ENGINE_OPTIONS"] = {
        "pool_size": 400
    }
    db = SQLAlchemy(app)


