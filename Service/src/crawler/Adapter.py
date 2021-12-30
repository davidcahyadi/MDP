from abc import ABCMeta, abstractmethod


class Adapter:
    __metaclass__ = ABCMeta

    @abstractmethod
    def extractPage(self,url):
        pass

    @abstractmethod
    def extractRecipe(self):
        pass

    @abstractmethod
    def extractIngredient(self):
        pass

    @abstractmethod
    def extractStep(self):
        pass

    @abstractmethod
    def extractPhoto(self):
        pass

    @abstractmethod
    def crawl(self,count):
        pass

    @abstractmethod
    def get_urls(self,page):
        pass