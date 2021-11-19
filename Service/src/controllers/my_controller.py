from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.models.models import User
from src.models.database import db
from src.services.security import validate_token, generate_token, token_required

my = Blueprint("my", __name__, url_prefix="/api/v1/my")


@my.get("/reviews")
def my_reviews():
    pass


@my.get("/recipes")
def my_recipes():
    pass


@my.get("/recipe/save")
def save_recipe():
    pass

