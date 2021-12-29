from datetime import datetime

from bs4 import BeautifulSoup
import requests
from flask import jsonify

from src.constant.http_status_codes import HTTP_200_OK
from src.models.database import db
from src.models.models import Recipe, RecipeIngredient, Photo, Step

recipe_id = 0
recipe_ingredient_id = 0
photo_id = 0
steps_id = 0


def scrapAllRecipes(deep=1):
    global recipe_id, recipe_ingredient_id, photo_id, steps_id
    recipe_id = db.session.query(Recipe.id).count()
    recipe_ingredient_id = db.session.query(RecipeIngredient.id).count()
    photo_id = db.session.query(Photo.id).count()
    steps_id = db.session.query(Step.id).count()
    new_recipes = 0
    for i in range(1, deep + 1):
        html_text = requests.get('https://www.allrecipes.com/recipes/?pages=' + str(deep)).text
        soup = BeautifulSoup(html_text, 'lxml')
        recipes = soup.find_all("div", class_='component card card__category')

        for recipe in recipes:
            url = recipe.find("a", class_='card__titleLink').attrs["href"]
            if "/recipe/" in url:
                if crawlDetailRecipes(url):
                    new_recipes += 1
        print("finish crawl page-" + str(i))
    return jsonify({"message": "OK", "recipes": new_recipes}), HTTP_200_OK


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


def getRecipesDetail(recipe):
    print("Crawl Detail")
    # get title
    title = recipe.find("h1", class_="headline heading-content elementFont__display").text.replace('  ', '')
    print("TITLE : ", title)
    # get description
    description = recipe.find("div", class_="recipe-summary").find("p").text.replace("  ", "")

    # get rating
    ratings = recipe.find_all("li", class_="rating")

    total_count = 0
    total_rate = 0
    for i in range(5):
        rate_value = ratings[i].find("span", class_="rating-stars").text.replace(" ", "").replace("starvalues:", "")
        rate_count = ratings[i].find("span", class_="rating-count").text.replace(" ", "")
        total_rate += int(rate_value) * int(rate_count)
        total_count += int(rate_count)
    rating = total_rate / total_count

    # get duration
    prep = 0
    cook = 0
    portion = 0
    metas = recipe.find_all("div", class_="recipe-meta-item")
    for meta in metas:
        if "prep" in meta.text:
            prep = countTime(meta.find("div", class_="recipe-meta-item-body").text)
        elif "cook" in meta.text:
            cook = countTime(meta.find("div", class_="recipe-meta-item-body").text)
        elif "Servings:" in meta.text:
            portion = countTime(meta.find("div", class_="recipe-meta-item-body").text)
    total_duration = prep + cook

    # get photos
    photos = recipe.find("div", class_="primary-media-section primary-media-with-filmstrip")
    photos = photos.find_all("img")
    photos_url = []
    for photo in photos:
        photos_url.append(photo.attrs["src"])

    # get steps
    instructions = recipe.find_all("li", class_="subcontainer instructions-section-item")

    steps = []
    for step in instructions:
        steps.append({
            "title": step.find("span", class_="checkbox-list-text").text,
            "description": step.find("div", class_="paragraph").find("p").text,
        })

    # get ingredients
    ingredients_text = recipe.find_all("span", class_="ingredients-item-name")
    ingredients = []
    for ingredient in ingredients_text:
        ingredients.append(ingredient.text.replace("  ", ""))
    return {
        "title": title,
        "description": description,
        "rating": rating,
        "prep_duration": prep,
        "cook_duration": cook,
        "serve_portion": portion,
        "photos": photos_url,
        "steps": steps,
        "ingredients": ingredients,
        "like": total_count
    }


def crawlDetailRecipes(url):
    global recipe_id, recipe_ingredient_id, photo_id, steps_id

    recipe_id += 1
    print("URL : ", url)
    text = requests.get(url).text
    detail = BeautifulSoup(text, 'lxml')
    result = getRecipesDetail(detail)
    if db.session.query(Recipe).filter(Recipe.title == result["title"]).count() == 0:
        recipe = {"id": recipe_id,
                  "title": result["title"],
                  "user_id": None,
                  "rate": result["rating"],
                  "view": 0,
                  "like": result["like"],
                  "crawling_from": "All Recipes",
                  "cook_duration": result["cook_duration"],
                  "prep_duration": result["prep_duration"],
                  "serve_portion": result["serve_portion"],
                  "description": result["description"],
                  "created_at": datetime.now(),
                  "updated_at": datetime.now(),
                  "deleted_at": ""
                  }
        model = Recipe()
        model.create(recipe)
        db.session.add(model)
        db.session.commit()

        # create ingredient list
        for i in result["ingredients"]:
            recipe_ingredient_id += 1
            ingredient = {
                "id": recipe_ingredient_id,
                "name": i,
                "ingredient_id": None,
                "measurement_id": None,
                "amount": 0,
                "recipe_id": recipe_id,
                "created_at": datetime.now(),
                "updated_at": datetime.now(),
                "deleted_at": ""
            }
            model = RecipeIngredient()
            model.create(ingredient)
            db.session.add(model)
            db.session.commit()

        # create photos
        first = True
        for i in result["photos"]:
            photo_id += 1
            t = 0
            if not first:
                t = 1
            photo = {
                "id": photo_id,
                "recipe_id": recipe_id,
                "url": i,
                "type": t,
                "created_at": "",
                "deleted_at": ""
            }
            model = Photo()
            model.create(photo)
            db.session.add(model)
            db.session.commit()
            first = False

        # create ingredient list
        order = 0
        for s in result["steps"]:
            steps_id += 1
            order += 1
            print("Step : ", steps_id)
            print("Order : ", order)
            step = {
                "id": steps_id,
                "recipe_id": recipe_id,
                "order": order,
                "title": s["title"],
                "description": s["description"],
                "url": "",
                "duration": 0,
                "type_id": 0,
                "created_at": datetime.now(),
                "updated_at": datetime.now(),
                "deleted_at": ""
            }
            model = Step()
            model.create(step)
            db.session.add(model)
            db.session.commit()
        print("Insert to DB")
        return True
    return False


def crawl_all_recipes():
    return scrapAllRecipes(1)
