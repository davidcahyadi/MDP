from bs4 import BeautifulSoup
import requests


def getRecipesDetail(recipe):
    # stars = recipe.find_all("span", class_="rating-star")
    # star_count = 0
    # for s in stars:
    #     if 'g fill="#000"' not in s:
    #         star_count += 1
    #
    # rating_count = recipe.find("span", class_="card__ratingCount")
    # if rating_count is None:
    #     rating_count = 0
    # else:
    #     rating_count = rating_count.text.replace(" ", "")

    return recipe.find("a", class_='card__titleLink').attrs["href"]
    #     "title": recipe.find("h3", class_="card__title").text.replace('  ', ''),
    #     "rating_count": rating_count,
    #     "rating": star_count
    # }


def scrapAllRecipes(deep=1):
    url_file = open("all_recipes_url.txt", "w")
    for i in range(1,deep+1):
        html_text = requests.get('https://www.allrecipes.com/recipes/?pages=' + str(deep)).text
        soup = BeautifulSoup(html_text, 'lxml')
        recipes = soup.find_all("div", class_='component card card__category')

        for recipe in recipes:
            url = getRecipesDetail(recipe)
            if "recipe" in url:
                url_file.write(url+"\n")
        print("finish crawl page-"+str(i))
    url_file.close()


scrapAllRecipes(25)
