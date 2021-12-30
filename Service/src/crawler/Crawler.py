from flask import jsonify

from src.constant.http_status_codes import HTTP_200_OK
from src.models.database import db
from src.models.models import Recipe, Photo, Step, RecipeIngredient


class Crawler:
    adapter = None
    new_recipe = 0
    results = None

    def __init__(self, adapter):
        self.adapter = adapter

    def crawl(self, count):
        self.results = self.adapter.crawl(count)
        self.add_to_database()
        return jsonify({"message": "OK", "newRecipes": self.new_recipe}), HTTP_200_OK

    def add_to_database(self):
        for recipe in self.results:
            if db.session.query(Recipe).filter_by(id=recipe["recipe"]["id"]).count() == 0:
                print("Insert TO DB : ", recipe["recipe"]["title"])
                self.new_recipe += 1
                model = Recipe()
                model.create(recipe["recipe"])
                db.session.add(model)

                for ingredient in recipe["ingredients"]:
                    model = RecipeIngredient()
                    model.create(ingredient)
                    db.session.add(model)

                for step in recipe["steps"]:
                    model = Step()
                    model.create(step)
                    db.session.add(model)

                for photo in recipe["photos"]:
                    model = Photo()
                    model.create(photo)
                    db.session.add(model)

        db.session.commit()
