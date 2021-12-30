from flask import Blueprint, make_response, jsonify

from src.constant.http_status_codes import HTTP_403_FORBIDDEN, HTTP_200_OK
from src.crawler.AllRecipeAdapter import AllRecipeAdapter
from src.crawler.Crawler import Crawler
from src.crawler.allrecipes import crawl_all_recipes
from src.helper.dictHelper import iterateModel
from src.models.database import db
from src.models.models import User, Review, Recipe
from src.services.security import token_required

admin = Blueprint("admin", __name__, url_prefix="/api/v1/admin")


@admin.post("crawl/<crawl_id>")
# TODO: add authorize for admin
def crawl_web(crawl_id):
    if int(crawl_id) == 1:
        return Crawler(AllRecipeAdapter()).crawl(1)
    else:
        return jsonify({"error": {"crawl": "ID not Found"}}), HTTP_403_FORBIDDEN


@admin.get("users")
def get_users():
    users = db.session.query(User).all()
    return jsonify(iterateModel(users))


@admin.get("reviews")
def get_reviews():
    reviews = db.session.query(Review).order_by(Review.created_at).all()
    return jsonify(iterateModel(reviews))


@admin.get("recipes")
def get_recipes():
    recipes = db.session.query(Recipe).order_by(Recipe.created_at).all()
    return jsonify(iterateModel(recipes))


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
    Recipe.query.filter_by(id=id).delete()
    db.session.commit()
    return jsonify({"message": "OK"}), HTTP_200_OK
