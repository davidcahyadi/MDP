import json
from datetime import datetime

import requests
from bs4 import BeautifulSoup
from sqlalchemy import func

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
        self.recipe_id = db.session.query(func.max(Recipe.id)).scalar()
        self.recipe_ingredient_id = db.session.query(func.max(RecipeIngredient.id)).scalar()
        self.photo_id = db.session.query(func.max(Photo.id)).scalar()
        self.steps_id = db.session.query(func.max(Step.id)).scalar()

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

    def extractRecipe(self):
        duration = self.get_duration()
        return {
            "id": self.recipe_id,
            "title": self.get_title(),
            "user_id": None,
            "rate": 0,
            "view": 0,
            "like": 0,
            "crawling_from": self.name,
            "cook_duration": duration["cook"],
            "prep_duration": duration["prep"],
            "serve_portion": duration["serve"],
            "description": self.get_description(),
            "created_at": datetime.now(),
            "updated_at": datetime.now(),
            "deleted_at": None
        }

    def get_title(self):
        return self.recipe_page.find("h2", class_="cmp-title__text").text

    def get_duration(self):
        cols = self.recipe_page.find_all("div", class_="row m-recipe-info__table")[1]
        l = cols.find_all("li")
        prep = l[4].find("strong").text.replace(" min", "")
        cook = l[5].find("strong").text.replace(" min", "")
        serve = self.recipe_page.find("li", class_="m-recipeDetailList__item").find("strong").text

        numbers = []
        for i in range(len(serve)):
            if serve[i].isdigit():
                numbers.append(serve[i])
        return {"prep": int(prep), "cook": int(cook), "serve": int(numbers[0])}

    def get_description(self):
        return self.recipe_page.find("div", class_="m-content__copy -full").find("span").text

    def extractIngredient(self):
        ingredients = self.recipe_page.find_all("li", class_="m-recipeDetailList__item")

        ingredients_text = []
        for i in ingredients:
            p = i.find_all("p")
            if p is not None:
                for text in p:
                    ingredients_text.append(text)
        ingredients = []
        for ingredient in ingredients_text:
            if "for" not in ingredient.text and ingredient.text != '\xa0':
                self.recipe_ingredient_id += 1
                ingredients.append({
                    "id": self.recipe_ingredient_id,
                    "name": ingredient.text.replace("  ", "").replace('\xa0', ''),
                    "ingredient_id": None,
                    "measurement_id": None,
                    "amount": 0,
                    "recipe_id": self.recipe_id,
                    "created_at": datetime.now(),
                    "updated_at": datetime.now(),
                    "deleted_at": None
                })


        return ingredients

    def extractStep(self):
        instructions = self.recipe_page.find("section",class_="o-recipe-steps").find_all("p")

        steps = []
        order = 0
        for step in instructions:
            self.steps_id += 1
            steps.append({
                "id": self.steps_id,
                "recipe_id": self.recipe_id,
                "order": order,
                "title": "-",
                "description": step.text,
                "url": "",
                "duration": 0,
                "type_id": 0,
                "created_at": datetime.now(),
                "updated_at": datetime.now(),
                "deleted_at": None
            })

            order += 1
        return steps

    def extractPhoto(self):
        images = self.recipe_page.find_all("div",class_="cmp-image")
        photos = []
        photo_order = 0
        for image in images:
            image = image.find("img").attrs["src"]
            photo_order += 1
            if photo_order == 1:
                t = 1
            else:
                t = 0
            self.photo_id += 1
            photos.append({
                "id": self.photo_id,
                "recipe_id": self.recipe_id,
                "url": image,
                "type": t,
                "created_at": datetime.now(),
                "deleted_at": None
            })

        return photos

    def crawl(self, count):
        crawl_result = []
        for i in range(count):
            urls = self.get_urls(i + 1)
            for url in urls:
                crawl_result.append(self.extractPage(url))
        return crawl_result
