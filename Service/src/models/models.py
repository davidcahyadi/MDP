from datetime import datetime
from sqlalchemy import ForeignKey

from src.helper.dictHelper import iterateModel
from src.models.database import db
from werkzeug.security import generate_password_hash, check_password_hash


class User(db.Model):
    __tablename__ = "users"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(80))
    email = db.Column(db.String(256), unique=True)
    password = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime, default=datetime.now())
    updated_at = db.Column(db.DateTime, onupdate=datetime.now())

    def make(self,name,email,password):
        self.email = email
        self.name = name
        self.password = generate_password_hash(password)
        return self

    def verify_password(self, password):
        return check_password_hash(self.password, password)

    def raw(self):
        return {
            "id": self.id,
            "name": self.name,
            "email": self.email,
            "password": self.password,
            "created_at": self.created_at,
            "updated_at": self.updated_at
        }

    def create(self,record):
        self.id = record["id"]
        self.name = record["name"]
        self.email = record["email"]
        self.password = record["password"]
        self.created_at = record["created_at"]
        self.updated_at = record["updated_at"]


class Photo(db.Model):
    __tablename__ = "photos"
    id = db.Column(db.Integer, primary_key=True)
    recipe_id = db.Column(db.Integer(), ForeignKey("recipes.id"))
    url = db.Column(db.Text(), nullable=False)
    type = db.Column(db.Integer())  # 0 for introduction, 1 for steps
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)

    def raw(self):
        return {
            "id": self.id,
            "recipe_id": self.recipe_id,
            "url": self.url,
            "type": self.type,
            "created_at": self.created_at,
            "deleted_at": self.deleted_at
        }

    def create(self, record):
        self.id = record["id"]
        self.recipe_id = record["recipe_id"]
        self.url = record["url"]
        self.type = record["type"]
        self.created_at = record["created_at"]
        self.deleted_at = record["deleted_at"]


class Recipe(db.Model):
    __tablename__ = "recipes"
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(80))
    user_id = db.Column(db.Integer(), ForeignKey("users.id"))
    rate = db.Column(db.DECIMAL, nullable=True)
    view = db.Column(db.Integer(), default=0)
    like = db.Column(db.Integer(), default=0)
    cook_duration = db.Column(db.Integer(), default=0)  # in minutes
    prep_duration = db.Column(db.Integer(), default=0)  # in minutes
    serve_portion = db.Column(db.Integer(), default=0)
    description = db.Column(db.Text())
    created_at = db.Column(db.DateTime, default=datetime.now())
    updated_at = db.Column(db.DateTime, onupdate=datetime.now())
    deleted_at = db.Column(db.DateTime)
    # one to many relations
    _steps = db.relationship("Step", backref='step')
    _ingredients = db.relationship("RecipeIngredient", backref='ingredient')
    _reviews = db.relationship("Review", backref="review")
    _photos = db.relationship("Photo", backref="photo")

    def raw(self):
        return {
            "id": self.id,
            "title": self.title,
            "user_id": self.user_id,
            "rate": self.rate,
            "view": self.view,
            "like": self.like,
            "cook_duration": self.cook_duration,
            "prep_duration": self.prep_duration,
            "serve_portion": self.serve_portion,
            "description": self.description,
            "created_at": self.created_at,
            "updated_at": self.updated_at,
            "deleted_at": self.deleted_at,
        }

    def create(self, record):
        self.id = record["id"]
        self.title = record["title"]
        self.user_id = record["user_id"]
        self.rate = record["rate"]
        self.view = record["view"]
        self.like = record["like"]
        self.cook_duration = record["cook_duration"]
        self.prep_duration = record["prep_duration"]
        self.serve_portion = record["serve_portion"]
        self.description = record["description"]
        self.created_at = record["created_at"]
        self.updated_at = record["updated_at"]
        self.deleted_at = record["deleted_at"]

    def steps(self):
        return iterateModel(self._steps)

    def ingredients(self):
        return iterateModel(self._ingredients)

    def reviews(self):
        return iterateModel(self._reviews)

    def photos(self):
        return iterateModel(self._photos)


class Review(db.Model):
    __tablename__ = "reviews"
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer(), ForeignKey("users.id"))
    recipe_id = db.Column(db.Integer(), ForeignKey("recipes.id"))
    rate = db.Column(db.DECIMAL, nullable=True)
    description = db.Column(db.Text())
    review_id = db.Column(db.Integer(), nullable=True)
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)

    def make(self,user_id,recipe_id,rate,description):
        self.user_id = user_id
        self.recipe_id = recipe_id
        self.rate = rate
        self.description = description
        self.review_id = None
        return self

    def make_reply(self,user_id,recipe_id,review_id,description):
        self.user_id = user_id
        self.recipe_id = recipe_id
        self.review_id = review_id
        self.description = description
        self.rate = None
        return self

    def raw(self):
        return {
            "id": self.id,
            "user_id": self.user_id,
            "rate": self.rate,
            "description": self.description,
            "review_id": self.review_id,
            "created_at": self.created_at,
            "deleted_at": self.deleted_at,
        }

    def create(self, record):
        self.id = record["id"]
        self.user_id = record["user_id"]
        self.recipe_id = record["recipe_id"]
        self.rate = record["rate"]
        self.description = record["description"]
        self.review_id = record["review_id"]
        self.created_at = record["created_at"]
        self.deleted_at = record["deleted_at"]


class IngredientType(db.Model):
    __tablename__ = "ingredient_types"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)

    def raw(self):
        return {
            "id": self.id,
            "name": self.name,
            "created_at": self.created_at,
            "deleted_at": self.deleted_at,
        }

    def create(self, record):
        self.id = record["id"]
        self.name = record["name"]
        self.created_at = record["created_at"]
        self.deleted_at = record["deleted_at"]


class Ingredient(db.Model):
    __tablename__ = "ingredients"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))
    icon_url = db.Column(db.Text())
    type_id = db.Column(db.Integer(), ForeignKey("ingredient_types.id"))
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)

    def raw(self):
        return {
            "id": self.id,
            "name": self.name,
            "icon_url": self.icon_url,
            "type_id": self.type_id,
            "created_at": self.created_at,
            "deleted_at": self.deleted_at,
        }

    def create(self, record):
        self.id = record["id"]
        self.name = record["name"]
        self.icon_url = record["icon_url"]
        self.type_id = record["type_id"]
        self.created_at = record["created_at"]
        self.deleted_at = record["deleted_at"]


class Measurement(db.Model):
    __tablename__ = "measurements"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))
    abbr = db.Column(db.String(3))

    def create(self, record):
        self.id = record["id"]
        self.name = record["name"]
        self.abbr = record["abbr"]

    def raw(self):
        return {
            "id": int(self.id),
            "name": self.name,
            "abbr": self.abbr,
        }


class Conversion(db.Model):
    __tablename__ = "conversions"
    id = db.Column(db.Integer, primary_key=True)
    from_measurement = db.Column(db.Integer(), ForeignKey("measurements.id"))
    to_measurement = db.Column(db.Integer(), ForeignKey("measurements.id"))
    amount = db.Column(db.Float())

    def create(self, record):
        self.id = record["id"]
        self.from_measurement = record["from_measurement"]
        self.to_measurement = record["to_measurement"]
        self.amount = record["amount"]

    def raw(self):
        return {
            "id": int(self.id),
            "from_measurement": self.from_measurement,
            "to_measurement": self.to_measurement,
            "amount": self.amount,
        }


class Step(db.Model):
    __tablename__ = "steps"
    id = db.Column(db.Integer, primary_key=True)
    recipe_id = db.Column(db.Integer(), ForeignKey("recipes.id"))
    order = db.Column(db.Integer)
    title = db.Column(db.String(255))
    description = db.Column(db.Text(), )
    url = db.Column(db.Text(), nullable=True)
    duration = db.Column(db.Integer(), nullable=True)  # in seconds
    type_id = db.Column(db.Integer())  # 0 = text, 1 = photo, 2 = timer
    created_at = db.Column(db.DateTime, default=datetime.now())
    updated_at = db.Column(db.DateTime, onupdate=datetime.now())
    deleted_at = db.Column(db.DateTime)

    def create(self, record):
        self.id = record["id"]
        self.recipe_id = record["recipe_id"]
        self.order = record["order"]
        self.title = record["title"]
        self.description = record["description"]
        self.url = record["url"]
        self.duration = record["duration"]
        self.type_id = record["type_id"]
        self.created_at = record["created_at"]
        self.updated_at = record["updated_at"]
        self.deleted_at = record["deleted_at"]

    def raw(self):
        return {
            "id": self.id,
            "recipe_id": self.recipe_id,
            "order": self.order,
            "title": self.title,
            "description": self.description,
            "url": self.url,
            "duration": self.duration,
            "type_id": self.type_id,
            "created_at": self.created_at,
            "updated_at": self.updated_at,
            "deleted_at": self.deleted_at,
        }


class RecipeIngredient(db.Model):
    __tablename__ = "recipe_ingredients"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))
    ingredient_id = db.Column(db.Integer(), ForeignKey("ingredients.id"))
    measurement_id = db.Column(db.Integer(), ForeignKey("measurements.id"))
    amount = db.Column(db.DECIMAL)
    recipe_id = db.Column(db.Integer(), ForeignKey("recipes.id"))
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)

    def raw(self):
        return {
            "id": self.id,
            "name": self.name,
            "ingredient_id": self.ingredient_id,
            "measurement_id": self.measurement_id,
            "amount": self.amount,
            "recipe_id": self.recipe_id,
            "created_at": self.created_at,
            "deleted_at": self.deleted_at,
        }

    def create(self, record):
        self.id = record["id"]
        self.name = record["name"]
        self.ingredient_id = record["ingredient_id"]
        self.measurement_id = record["measurement_id"]
        self.amount = record["amount"]
        self.recipe_id = record["recipe_id"]
        self.created_at = record["created_at"]
        self.deleted_at = record["deleted_at"]


model_list = {
    "conversions": Conversion,
    "ingredient_types": IngredientType,
    "ingredients": Ingredient,
    "measurements": Measurement,
    "photos": Photo,
    "recipe_ingredients": RecipeIngredient,
    "recipes": Recipe,
    "reviews": Review,
    "steps": Step,
    "users": User
}

model_order = [
    "users",
    "ingredient_types",
    "ingredients",
    "measurements",
    "conversions",
    "recipes",
    "photos",
    "recipe_ingredients",
    "steps",
    "reviews"
]
