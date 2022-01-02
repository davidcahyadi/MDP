from flask import Blueprint, make_response, jsonify, request

from src.constant.http_status_codes import HTTP_403_FORBIDDEN, HTTP_200_OK
from src.crawler.AllRecipeAdapter import AllRecipeAdapter
from src.crawler.AsianFoodNetworkAdapter import AsianFoodNetworkAdapter
from src.crawler.Crawler import Crawler
from src.helper.dictHelper import iterateModel
from src.models.database import db
from src.models.models import User, Review, Recipe, RecipeIngredient, Step, Photo

admin = Blueprint("admin", __name__, url_prefix="/api/v1/admin")


@admin.post("crawl/<crawl_id>")
def crawl_web(crawl_id):
    if int(crawl_id) == 1:
        return Crawler(AllRecipeAdapter()).crawl(1)
    if int(crawl_id) == 2:
        return Crawler(AsianFoodNetworkAdapter()).crawl(1)
    else:
        return jsonify({"error": {"crawl": "ID not Found"}}), HTTP_403_FORBIDDEN


@admin.get("users")
def get_users():
    page = int(request.args.get("page"))
    if page == -1:
        users = db.session.query(User).all()
    else:
        users = db.session.query(User).paginate(page=int(page),max_per_page=10).items
    return jsonify(iterateModel(users))


@admin.get("reviews")
def get_reviews():
    page = int(request.args.get("page"))
    if page == -1:
        reviews = db.session.query(Review).order_by(Review.created_at).all()
    else:
        reviews = db.session.query(Review).order_by(Review.created_at).paginate(page=int(page),max_per_page=10).items
    return jsonify(iterateModel(reviews))


@admin.get("recipes")
def get_recipes():
    page = int(request.args.get("page"))

    if page == -1:
        recipes = db.session.query(Recipe).order_by(Recipe.created_at).all()
    else:
        recipes = db.session.query(Recipe).order_by(Recipe.created_at)\
            .paginate(page=page,max_per_page=10).items
    return jsonify(iterateModel(recipes))


@admin.get("users/<id>")
def get_user_by_id(id):
    u = User.query.filter_by(id=id).first()
    db.session.commit()
    return jsonify(u.raw())


@admin.get("reviews/<id>")
def get_review_by_id(id):
    r = Review.query.filter_by(id=id).first()
    db.session.commit()
    return jsonify(r.raw())


@admin.post("delete/user/<id>")
def delete_user(id):
    User.query.filter_by(id=id).delete()
    db.session.commit()
    return jsonify({"message": "OK"}), HTTP_200_OK


@admin.post("delete/review/<id>")
def delete_review(id):
    Review.query.filter_by(id=id).delete()
    db.session.commit()
    return jsonify({"message": "OK"}), HTTP_200_OK


@admin.post("delete/recipe/<id>")
def delete_recipe(id):
    RecipeIngredient.query.filter_by(recipe_id=id).delete()
    Step.query.filter_by(recipe_id=id).delete()
    Photo.query.filter_by(recipe_id=id).delete()
    Recipe.query.filter_by(id=id).delete()
    db.session.commit()
    return jsonify({"message": "OK"}), HTTP_200_OK
