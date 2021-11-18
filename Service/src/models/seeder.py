from src.models.models import model_list
import pandas as pd
from src.models.database import db


class Seeder:
    def __init__(self, filepath):
        self.filename = filepath
        self.name = self.filename.split("\\")[-1].replace(".csv", "")
        self.model = model_list[self.name]()

    def up(self):
        df = pd.read_csv(self.filename)
        for index, record in df.iterrows():
            self.model.create(record)
            db.session.add(self.model)
        db.session.commit()

    def down(self):
        db.session.query(model_list[self.name]).delete()
        db.session.commit()
        print("Drop record in table "+ self.name)
