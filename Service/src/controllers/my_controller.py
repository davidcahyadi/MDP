from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.helper.dictHelper import iterateModel
from src.models.database import db
from src.models.models import Review, Recipe, Photo, Step, Ingredient, RecipeIngredient
from src.services.security import validate_token, generate_token, token_required

my = Blueprint("my", __name__, url_prefix="/api/v1/my")


@my.get("/reviews")
@token_required
def my_reviews(user_id):
    reviews = Review.query.filter(Review.user_id == user_id, Review.review_id.is_not(None)).all()
    return jsonify(iterateModel(reviews))


@my.get("/recipes")
@token_required
def my_recipes(user_id):
    recipes = Recipe.query.filter(Recipe.user_id == user_id).all()
    return jsonify(iterateModel(recipes))


@my.post("/recipe/save")
@token_required
def save_recipe(user_id):
    req = request.json
    recipe = Recipe()
    recipe.make(user_id=user_id, title=req["title"], description=req["description"],
                # TODO: ganti kalau sudah ada cook duration dan prep duration
                cook_duration=10, prep_duration=5, serve_portion=3)

    for photo_json in req["photos"]:
        photo = Photo().make(photo_json)
        recipe.photos.append(photo)

    for step_json in req["steps"]:
        if step_json["type_id"] == 0:
            recipe.steps.append(Step().makeText(step_json["order"], step_json["title"], step_json["description"]))
        elif step_json["type_id"] == 1:
            recipe.steps.append(Step().makePhoto(step_json["order"], step_json["title"], step_json["description"],
                                                 step_json["url"]))
        elif step_json["type_id"] == 2:
            recipe.steps.append(Step().makeTimer(step_json["order"], step_json["title"], step_json["description"],
                                                 step_json["duration"]))

    for ingredient_json in req["ingredients"]:
        recipe.ingredients.append(RecipeIngredient().make(ingredient_json["name"], ingredient_json["ingredient_id"],
                                                          ingredient_json["measurement_id"], ingredient_json["amount"]))

    db.session.add(recipe)
    db.session.commit()

    return "OK"
