from datetime import datetime
from sqlalchemy import ForeignKey
from src import db


class User(db.Model):
    __tablename__ = "users"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(80))
    email = db.Column(db.String(256), unique=True)
    password = db.Column(db.Text(), nullable=False)
    created_at = db.Column(db.DateTime, default=datetime.now())
    updated_at = db.Column(db.DateTime, onupdate=datetime.now())

    def __repr__(self):
        return f'User>>> {self.username}'


class Photo(db.Model):
    __tablename__ = "photos"
    id = db.Column(db.Integer, primary_key=True)
    recipe_id = db.Column(db.Integer(), ForeignKey("recipes.id"))
    url = db.Column(db.Text(), nullable=False)
    type = db.Column(db.Integer())  # 0 for introduction, 1 for steps
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)


class Recipe(db.Model):
    __tablename__ = "recipes"
    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(80))
    user_id = db.Column(db.Integer(), ForeignKey("users.id"))
    rate = db.Column(db.DECIMAL, nullable=True)
    view = db.Column(db.Integer(), default=0)
    like = db.Column(db.Integer(), default=0)
    duration = db.Column(db.Integer())  # in seconds
    description = db.Column(db.Text())
    created_at = db.Column(db.DateTime, default=datetime.now())
    updated_at = db.Column(db.DateTime, onupdate=datetime.now())
    deleted_at = db.Column(db.DateTime)


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


class IngredientType(db.Model):
    __tablename__ = "ingredient_types"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)


class Ingredient(db.Model):
    __tablename__ = "ingredients"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))
    icon_url = db.Column(db.Text())
    type_id = db.Column(db.Integer(), ForeignKey("ingredient_types.id"))
    created_at = db.Column(db.DateTime, default=datetime.now())
    deleted_at = db.Column(db.DateTime)


class Measurement(db.Model):
    __tablename__ = "measurements"
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255))


class Conversion(db.Model):
    __tablename__ = "conversions"
    id = db.Column(db.Integer, primary_key=True)
    from_measurement = db.Column(db.Integer(), ForeignKey("measurements.id"))
    to_measurement = db.Column(db.Integer(), ForeignKey("measurements.id"))
    amount = db.Column(db.DECIMAL)


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