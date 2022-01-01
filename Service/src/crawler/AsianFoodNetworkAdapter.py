import json

import requests
from bs4 import BeautifulSoup

from src.crawler.Adapter import Adapter
from src.models.database import db
from src.models.models import Recipe, RecipeIngredient, Photo, Step


def url(page):
    return 'https://search-api.swiftype.com/api/v1/public/engines/search.json?engine_key=FhbyQrknVZZco8zdUNy5&per_page=10&page=' + str(
        page) + '&filters%5Bpage%5D%5Btype%5D=page-recipe-details&filters%5Bpage%5D%5Btags%5D=asian-food-network%3Acuisine%2Findonesian&sort_field%5Bpage%5D=published_at&sort_direction%5Bpage%5D=desc&filters%5Bpage%5D%5Blanguage%5D%5B%5D=en'


class AsianFoodNetworkAdapter(Adapter):
    name = "Asian Food Network"
    recipe_page = None

    def __init__(self):
        self.recipe_id = db.session.query(Recipe.id).count()
        self.recipe_ingredient_id = db.session.query(RecipeIngredient.id).count()
        self.photo_id = db.session.query(Photo.id).count()
        self.steps_id = db.session.query(Step.id).count()

    def get_urls(self, page):
        json_text = requests.get(url(page)).text
        recipes = json.loads(json_text)["records"]["page"]
        urls = []
        for recipe in recipes:
            urls.append(recipe["url"])
        return urls

    def extractPage(self, url):
        self.recipe_id += 1
        print("Crawled Recipe URL : ", url)
        html_text = requests.get(url).text
        self.recipe_page = BeautifulSoup(html_text, 'lxml')
        result = {
            "recipe": self.extractRecipe(),
            "ingredients": self.extractIngredient(),
            "steps": self.extractStep(),
            "photos": self.extractPhoto()
        }

        return result
        pass

    def extractRecipe(self):
        pass
        # return {
        #     "id": self.recipe_id,
        #     "title": self.get_title(),
        #     "user_id": None,
        #     "rate": rating["rating"],
        #     "view": 0,
        #     "like": rating["total_count"],
        #     "crawling_from": self.name,
        #     "cook_duration": duration["cook"],
        #     "prep_duration": duration["prep"],
        #     "serve_portion": duration["serve"],
        #     "description": self.get_description(),
        #     "created_at": datetime.now(),
        #     "updated_at": datetime.now(),
        #     "deleted_at": None
        # }

    def extractIngredient(self):
        pass

    def extractStep(self):
        pass

    def extractPhoto(self):
        pass

    def crawl(self, count):
        urls = self.get_urls(count)
        for url in urls:
            self.extractPage(url)


