from bs4 import BeautifulSoup
import requests
import pandas as pd


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


def scrapAllRecipes():
    url_file = open("all_recipes_url_example.txt", "r")
    recipes = url_file.read().split("\n")
    url_file.close()
    recipe_table = []
    recipe_ingredient_table = []
    photo_table = []
    steps_table = []

    recipe_id = 0
    recipe_ingredient_id = 0
    photo_id = 0
    steps_id = 0

    for url in recipes:
        recipe_id += 1
        print("generate data for recipes number : " + str(recipe_id))
        text = requests.get(url).text
        detail = BeautifulSoup(text, 'lxml')
        result = getRecipesDetail(detail)
        recipe = {"id": recipe_id,
                  "title": result["title"],
                  "user_id": 2,
                  "rate": result["rating"],
                  "view": 0,
                  "like": result["like"],
                  "cook_duration": result["cook_duration"],
                  "cook_preparation": result["prep_duration"],
                  "serve_portion": result["serve_portion"],
                  "description": result["description"],
                  "created_at": "",
                  "updated_at": "",
                  "deleted_at": ""
                  }
        recipe_table.append(recipe)

        # create ingredient list
        for i in result["ingredients"]:
            recipe_ingredient_id += 1
            ingredient = {
                "id": recipe_ingredient_id,
                "name": i,
                "ingredient_id": 0,
                "measurement_id": 0,
                "amount": 0,
                "recipe_id": recipe_id,
                "created_at": "",
                "updated_at": "",
                "deleted_at": ""
            }
            recipe_ingredient_table.append(ingredient)

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
            photo_table.append(photo)
            first = False

        # create ingredient list
        order = 0
        for s in result["steps"]:
            steps_id += 1
            order += 1
            step = {
                "id": steps_id,
                "recipe_id": recipe_id,
                "order": order,
                "title": s["title"],
                "description": s["description"],
                "url": "",
                "duration": 0,
                "type_id": 0,
                "created_at": "",
                "updated_at": "",
                "deleted_at": ""
            }
            steps_table.append(step)

    recipe_table = pd.DataFrame(recipe_table).set_index("id")
    recipe_table.to_csv("./result/recipes.csv")
    recipe_ingredient_table = pd.DataFrame(recipe_ingredient_table).set_index("id")
    recipe_ingredient_table.to_csv("./result/recipe_ingredients.csv")
    photo_table = pd.DataFrame(photo_table).set_index("id")
    photo_table.to_csv("./result/photos.csv")
    steps_table = pd.DataFrame(steps_table).set_index("id")
    steps_table.to_csv("./result/steps.csv")


scrapAllRecipes()
