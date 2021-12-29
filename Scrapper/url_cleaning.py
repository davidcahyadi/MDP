recipe_url = open("all_recipes_url.txt", "r")
recipes = recipe_url.read().split("\n")

recipe_url.close()
recipe_url = open("all_recipes_url.txt", "w")

for url in recipes:
    if "/recipe/" in url:
        recipe_url.write(url+"\n")

recipe_url.close()
