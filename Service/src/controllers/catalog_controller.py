from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.models.models import User
from src.models.database import db
from src.services.security import validate_token, generate_token, token_required

catalog = Blueprint("catalog", __name__, url_prefix="/api/v1/catalog")


@catalog.get("/popular")
def catalog_popular():
    pass


@catalog.get("/like")
def catalog_like():
    pass


@catalog.get("/newest")
def catalog_newest():
    pass


@catalog.get("/search")
def catalog_search():
    pass


@catalog.post("/recommendation")
def catalog_recommendation():
    pass
