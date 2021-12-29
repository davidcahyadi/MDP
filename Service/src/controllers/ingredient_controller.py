from flask import Blueprint, request, jsonify, make_response
from sqlalchemy import desc

from src.helper.dictHelper import iterateModel
from src.models.models import User, Recipe, Ingredient
from src.models.database import db

ingredient = Blueprint("ingredient", __name__, url_prefix="/api/v1/ingredient")


@ingredient.get("/all")
def get_all_ingredients():
    return jsonify(iterateModel(db.session.query(Ingredient).all()))
