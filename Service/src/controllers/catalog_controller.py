from flask import Blueprint, request, jsonify, make_response
from sqlalchemy import desc
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.models.models import User, Recipe
from src.models.database import db
from src.services.security import validate_token, generate_token, token_required

catalog = Blueprint("catalog", __name__, url_prefix="/api/v1/catalog")


@catalog.get("/popular/<page>")
def catalog_popular(page):
    results = Recipe.query.order_by(desc(Recipe.view)).paginate(page=int(page),max_per_page=10).items
    result = []
    for res in results:
        result.append(res.raw())
    return make_response(jsonify(result))


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
