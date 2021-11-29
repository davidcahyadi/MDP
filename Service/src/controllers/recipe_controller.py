from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.models.models import User, Recipe
from src.models.database import db
from src.services.security import validate_token, generate_token, token_required

recipe = Blueprint("recipe", __name__, url_prefix="/api/v1/recipe")


@recipe.get("/<id>/details")
def recipe_details(id):
    r = Recipe.query.filter(Recipe.id == id).first()
    db.session.commit()
    return jsonify(r.raw())


@recipe.get("/<id>/summary")
def recipe_summary(id):
    r = Recipe.query.filter(Recipe.id == id).first()
    db.session.commit()
    return jsonify(r.steps_raw())


@recipe.get("/<id>/ingredients")
def recipe_ingredients(id):
    r = Recipe.query.filter(Recipe.id == id).first()
    db.session.commit()
    return jsonify(r.ingredients_raw())


@recipe.get("/<id>/reviews")
def recipe_reviews(id):
    r = Recipe.query.filter(Recipe.id == id).first()
    db.session.commit()
    return jsonify(r.reviews_raw())
