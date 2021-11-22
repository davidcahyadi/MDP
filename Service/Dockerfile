FROM python:3.9-alpine
RUN echo "@testing http://dl-cdn.alpinelinux.org/alpine/edge/testing" >> /etc/apk/repositories
RUN apk add --update --no-cache py3-numpy py3-pandas@testing mariadb-dev
ENV PYTHONPATH=/usr/lib/python3.8/site-packages
COPY requirements.txt /app
RUN pip install -r requirements.txt
COPY . /app
WORKDIR /app
CMD ["flask", "run"]

