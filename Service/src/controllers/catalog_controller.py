from flask import Blueprint, request, jsonify, make_response
from sqlalchemy import desc
from src.models.models import User, Recipe
from src.models.database import db

catalog = Blueprint("catalog", __name__, url_prefix="/api/v1/catalog")


@catalog.get("/popular/<page>")
def catalog_popular(page):
    results = Recipe.query.order_by(desc(Recipe.view)).paginate(page=int(page),max_per_page=10).items
    db.session.commit()
    result = []
    for res in results:
        result.append(res.raw())
    return make_response(jsonify(result))


@catalog.get("/like/<page>")
def catalog_like(page):
    results = Recipe.query.order_by(desc(Recipe.like)).paginate(page=int(page), max_per_page=10).items
    db.session.commit()
    result = []
    for res in results:
        result.append(res.raw())
    return make_response(jsonify(result))


@catalog.get("/newest/<page>")
def catalog_newest(page):
    results = Recipe.query.order_by(desc(Recipe.created_at)).paginate(page=int(page), max_per_page=10).items
    db.session.commit()
    result = []
    for res in results:
        result.append(res.raw())
    return make_response(jsonify(result))


@catalog.get("/search")
def catalog_search():
    search = "%{}%".format(request.args.get("q"))
    fetch = Recipe.query.filter(Recipe.title.like(search)).all()
    db.session.commit()
    results = []
    for result in fetch:
        results.append(result.raw())
    return make_response(jsonify(results))


@catalog.post("/recommendation")
def catalog_recommendation():
    pass
