from flask import Blueprint, request, jsonify
from sqlalchemy import and_

from src.constant.http_status_codes import HTTP_200_OK
from src.helper.dictHelper import iterateModel
from src.models.database import db
from src.models.models import Review, Recipe, Photo, Step, RecipeIngredient, User, Bookmark
from src.services.security import token_required

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
                cook_duration=req["cook_duration"], prep_duration=req["prep_duration"],
                serve_portion=req["serve_portion"])

    for photo_json in req["photos"]:
        photo = Photo().make(photo_json)
        recipe.photos.append(photo)

    order = 0
    for step_json in req["steps"]:
        order += 1
        if step_json["type_id"] == 0:
            recipe.steps.append(Step().makeText(order, step_json["title"], step_json["description"]))
        elif step_json["type_id"] == 1:
            recipe.steps.append(Step().makePhoto(order, step_json["title"], step_json["description"],
                                                 step_json["url"]))
        elif step_json["type_id"] == 2:
            recipe.steps.append(Step().makeTimer(order, step_json["title"], step_json["description"],
                                                 step_json["duration"]))

    for ingredient_json in req["ingredients"]:
        recipe.ingredients.append(RecipeIngredient().make(ingredient_json["name"], ingredient_json["ingredient_id"],
                                                          ingredient_json["measurement_id"], ingredient_json["amount"]))

    db.session.add(recipe)
    db.session.commit()
    return jsonify("OK"), HTTP_200_OK


@my.post("/bookmark/add")
@token_required
def add_bookmark(user_id):

    recipe_id = request.args.get("recipe_id")
    print(recipe_id)
    if db.session.query(Bookmark).filter_by(recipe_id=recipe_id, user_id=user_id).count() == 0:
        bookmark = Bookmark()
        bookmark.make(user_id, recipe_id)
        db.session.add(bookmark)
        db.session.commit()
        return jsonify({"message": "OK"}), HTTP_200_OK
    else:
        return jsonify({"message": "Recipe in bookmark"}), HTTP_200_OK


@my.post("/bookmark/remove")
@token_required
def rm_bookmark(user_id):
    recipe_id = request.args.get("recipe_id")
    Bookmark.query.filter_by(user_id=user_id, recipe_id=recipe_id).delete()
    db.session.commit()
    return jsonify({"message": "OK"}), HTTP_200_OK


@my.post("/bookmark/all")
@token_required
def get_bookmark(user_id):
    return jsonify(iterateModel(db.session.query(Recipe)
                                .filter(Recipe.id == Bookmark.recipe_id, Bookmark.user_id == user_id)
                                .all()))


@my.get("/bookmark/check")
@token_required
def check_bookmark(user_id):
    recipe_id = request.args.get("recipe_id")
    bookmark = Bookmark.query.filter(Bookmark.user_id == user_id, Bookmark.recipe_id == recipe_id).first()
    if bookmark is None:
        # False artinya tidak ada bookmark
        return {"status": False}, HTTP_200_OK
    else:
        return {"status": True}, HTTP_200_OK


@my.get("/profile")
@token_required
def profile(user_id):
    user = db.session.query(User).filter_by(id=user_id).first()
    return jsonify(user.raw()), HTTP_200_OK
