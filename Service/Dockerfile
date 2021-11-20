FROM python:3.9.7-slim-bullseye
WORKDIR /app
ADD requirements.txt /app
RUN apt-get update && \
    apt-get install -y gcc python3-dev libmariadb3 libmariadb-dev --no-install-recommends && \
    apt-get clean autoclean && \
    apt-get autoremove --yes && \
    rm -rf /var/lib/{apt,dpkg,cache,log}/
RUN pip install mariadb numpy pandas
RUN pip install -r requirements.txt
COPY . /app
CMD ["flask", "run"]