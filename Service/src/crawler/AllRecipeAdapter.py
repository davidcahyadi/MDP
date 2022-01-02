from datetime import datetime

import requests
from bs4 import BeautifulSoup

from src.crawler.Adapter import Adapter
from src.models.database import db
from src.models.models import Recipe, Photo, Step, RecipeIngredient


def countTime(str):
    durations = str.replace("  ", "").split(" ")
    minute = 0
    if "hr" in durations[1]:
        minute += int(durations[0]) * 60
        if len(durations) > 3 and "min" in durations[3]:
            minute += int(durations[2])
    elif "min" in durations[1]:
        minute += int(durations[0])

    return minute


class AllRecipeAdapter(Adapter):
    url = 'https://www.allrecipes.com/recipes/?pages='
    name = "All Recipes"
    recipe_page = None

    def __init__(self):
        self.recipe_id = db.session.query(Recipe.id).count()
        self.recipe_ingredient_id = db.session.query(RecipeIngredient.id).count()
        self.photo_id = db.session.query(Photo.id).count()
        self.steps_id = db.session.query(Step.id).count()

    def get_urls(self, page):
        html_text = requests.get(self.url + str(page)).text
        soup = BeautifulSoup(html_text, 'lxml')
        recipes = soup.find_all("div", class_='component card card__category')
        urls = []
        for recipe in recipes:
            url = recipe.find("a", class_='card__titleLink').attrs["href"]
            if "/recipe/" in url:
                urls.append(url)
        return urls

    def crawl(self, count):
        crawl_result = []
        for i in range(count):
            urls = self.get_urls(i + 1)
            urls = urls[:5]
            for url in urls:
                crawl_result.append(self.extractPage(url))
        return crawl_result

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
        rating = self.get_ratings()
        duration = self.get_durations()
        return {
            "id": self.recipe_id,
            "title": self.get_title(),
            "user_id": None,
            "rate": rating["rating"],
            "view": 0,
            "like": rating["total_count"],
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
        return self.recipe_page \
            .find("h1", class_="headline heading-content elementFont__display") \
            .text.replace('  ', '')

    def get_description(self):
        return self.recipe_page \
            .find("div", class_="recipe-summary") \
            .find("p").text.replace("  ", "")

    def get_ratings(self):
        # get rating
        ratings = self.recipe_page.find_all("li", class_="rating")

        total_count = 0
        total_rate = 0
        for i in range(5):
            rate_value = ratings[i].find("span", class_="rating-stars").text.replace(" ", "").replace("starvalues:", "")
            rate_count = ratings[i].find("span", class_="rating-count").text.replace(" ", "")
            total_rate += int(rate_value) * int(rate_count)
            total_count += int(rate_count)
        rating = total_rate / total_count
        return {"rating": rating, "total_count": total_count, "total_rate": total_rate}

    def get_durations(self):
        # get duration
        prep = cook = portion = 0
        metas = self.recipe_page.find_all("div", class_="recipe-meta-item")
        for meta in metas:
            if "prep" in meta.text:
                prep = countTime(meta.find("div", class_="recipe-meta-item-body").text)
            elif "cook" in meta.text:
                cook = countTime(meta.find("div", class_="recipe-meta-item-body").text)
            elif "Servings:" in meta.text:
                portion = countTime(meta.find("div", class_="recipe-meta-item-body").text)
        return {"prep": prep, "cook": cook, "serve": portion}

    def extractIngredient(self):
        ingredients_text = self.recipe_page.find_all("span", class_="ingredients-item-name")
        ingredients = []
        for ingredient in ingredients_text:
            ingredients.append({
                "id": self.recipe_ingredient_id,
                "name": ingredient.text.replace("  ", ""),
                "ingredient_id": None,
                "measurement_id": None,
                "amount": 0,
                "recipe_id": self.recipe_id,
                "created_at": datetime.now(),
                "updated_at": datetime.now(),
                "deleted_at": None
            })
            self.recipe_ingredient_id += 1
        return ingredients

    def extractStep(self):
        instructions = self.recipe_page.find_all("li", class_="subcontainer instructions-section-item")

        steps = []
        order = 0
        for step in instructions:
            steps.append({
                "id": self.steps_id,
                "recipe_id": self.recipe_id,
                "order": order,
                "title": step.find("span", class_="checkbox-list-text").text,
                "description": step.find("div", class_="paragraph").find("p").text,
                "url": "",
                "duration": 0,
                "type_id": 0,
                "created_at": datetime.now(),
                "updated_at": datetime.now(),
                "deleted_at": None
            })
            self.steps_id += 1
            order += 1
        return steps

    def extractPhoto(self):
        # get photos
        photos_urls = self.recipe_page.find("div", class_="primary-media-section primary-media-with-filmstrip")
        photos_urls = photos_urls.find_all("img")
        photos = []
        photo_order = 0
        for photo in photos_urls:
            photo_order += 1
            if photo_order == 1:
                t = 1
            else:
                t = 0
            photos.append({
                "id": self.photo_id,
                "recipe_id": self.recipe_id,
                "url": photo.attrs["src"],
                "type": t,
                "created_at": datetime.now(),
                "deleted_at": None
            })
            self.photo_id += 1
        return photos
