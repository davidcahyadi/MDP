from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.helper.dictHelper import iterateModel
from src.models.models import Review, Recipe
from src.services.security import validate_token, generate_token, token_required

my = Blueprint("my", __name__, url_prefix="/api/v1/my")


@my.get("/reviews")
@token_required
def my_reviews(user_id):
    reviews = Review.query.filter(Review.user_id == user_id).all()
    return iterateModel(reviews)


@my.get("/recipes")
@token_required
def my_recipes(user_id):
    recipes = Recipe.query.filter(Recipe.user_id == user_id).all()
    return iterateModel(recipes)


@my.get("/recipe/save")
@token_required
def save_recipe(user_id):
    pass

