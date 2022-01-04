from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.helper.dictHelper import iterateModel
from src.models.models import User, Review, Recipe
from src.models.database import db
from src.services.security import validate_token, generate_token, token_required

review = Blueprint("review", __name__, url_prefix="/api/v1/review")


@review.post("/add")
@token_required
def add_review(user_id):
    recipe_id = request.args.get("recipe")
    rate = int(request.form.get("rate"))
    description = request.form.get("description")
    r = Review().make(user_id=user_id, recipe_id=recipe_id, rate=rate, description=description)
    recipe = Recipe.query.filter(Recipe.id == recipe_id).first()
    recipe.rate = (recipe.rate*recipe.like + rate) / (recipe.like+1)
    db.session.add(r)
    db.session.commit()
    return jsonify({"message": "OK"}), HTTP_200_OK


@review.post("/replies/add")
@token_required
def replies_add(user_id):
    review_id = request.form.get("review_id")
    description = request.form.get("description")
    r = Review().make(user_id=user_id, review_id=review_id, rate=None, description=description)
    db.session.add(r)
    db.session.commit()
    return jsonify({"message": "OK"}), HTTP_200_OK


@review.get("/<id>/replies")
def review_replies(id):
    replies = Review.query.filter(Review.review_id == id).all()
    db.session.commit()
    return jsonify(iterateModel(replies))
