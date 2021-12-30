import json

import requests

from src.crawler.Adapter import Adapter


def url(page):
    return 'https://search-api.swiftype.com/api/v1/public/engines/search.json?engine_key=FhbyQrknVZZco8zdUNy5&per_page=10&page=' + str(
        page) + '&filters%5Bpage%5D%5Btype%5D=page-recipe-details&filters%5Bpage%5D%5Btags%5D=asian-food-network%3Acuisine%2Findonesian&sort_field%5Bpage%5D=published_at&sort_direction%5Bpage%5D=desc&filters%5Bpage%5D%5Blanguage%5D%5B%5D=en'


class AsianFoodNetworkAdapter(Adapter):
    def get_urls(self, page):
        json_text = requests.get(url(page)).text
        recipes = json.loads(json_text)["records"]["page"]
        urls = []
        for recipe in recipes:
            urls.append(recipe["url"])
        return urls

    def extractPage(self, url):
        pass

    def extractRecipe(self):
        pass

    def extractIngredient(self):
        pass

    def extractStep(self):
        pass

    def extractPhoto(self):
        pass

    def crawl(self, count):
        self.get_urls(count)


