from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.models.models import User, Review
from src.models.database import db
from src.services.security import validate_token, generate_token, token_required

review = Blueprint("review", __name__, url_prefix="/api/v1/reviews")


@review.get("/add")
@token_required
def add_review(user_id):
    recipe_id = request.args.get("recipe")
    rate = int(request.form.get("rate"))
    description = request.form.get("description")
    r = Review().make(user_id=user_id, recipe_id=recipe_id, rate=rate, description=description)
    db.session.add(r)
    db.session.commit()
    return jsonify("OK"), HTTP_200_OK


@review.get("/<id>/replies")
def review_replies():
    pass


@review.get("/<id>/replies/add")
def replies_add():
    pass


