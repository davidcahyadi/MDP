import pathlib
import time
from glob import glob
import click
import pandas as pd

from src.crawler.AsianFoodNetworkAdapter import AsianFoodNetworkAdapter
from src.models.database import db
from src.models.seeder import Seeder
from src.models.models import model_list, model_order
from flask import Blueprint, Flask

command = Blueprint('db', __name__)
test = Blueprint('test',__name__)


def register_command(app: Flask):
    app.register_blueprint(command)
    app.register_blueprint(test)


@command.cli.command('migrate')
@click.argument("param", required=False)
def migrate(param):
    if param is not None and param == "fresh":
        db.drop_all()
    db.create_all()
    print("Database migration done")


@command.cli.command('seed')
@click.argument("tables", required=False)
def seed(tables):
    if tables is not None:
        tables = tables.split("|")
    else:
        tables = []
    files = glob(str(pathlib.Path().resolve()) + "/load_data/*.csv")

    seeders = []
    for file in files:
        table_name = file.split("\\")[-1].replace(".csv","")
        table_name = table_name.split("-")[1]
        if table_name in tables or len(tables) == 0:
            seeders.append(Seeder(file))

    start_time = time.time()
    size = len(seeders)
    for i in range(size):
        idx = size - i - 1
        seeders[idx].down()

    for i in range(size):
        seeders[i].up()
    print("Finish Seeding Tables in " + str(time.time() - start_time) + "s")


@command.cli.command('dump')
@click.argument("tables", required=False)
def dump(tables):
    if tables is not None:
        tables = tables.split("|")
    else:
        tables = []
    for table_name in db.engine.table_names():
        if table_name in tables or len(tables) == 0:
            col = []
            for column_name in model_list[table_name].__table__.columns:
                col.append(str(column_name).split(".")[1])

            data = []
            for record in model_list[table_name].query.all():
                data.append(record.raw())

            df = pd.DataFrame(data, columns=col)
            df = df.set_index("id")
            index = model_order.index(table_name)
            df.to_csv(str(pathlib.Path().resolve()) + "/dump/"+str(index)+"-"+table_name + ".csv")

@test.cli.command('crawler')
@click.argument("resource")
def crawl(resource):
    if resource == "AFN":
        AsianFoodNetworkAdapter().crawl(1)