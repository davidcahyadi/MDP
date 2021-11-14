from flask import Blueprint, request, jsonify
from werkzeug.security import check_password_hash, generate_password_hash
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_201_CREATED

auth = Blueprint("auth", __name__, url_prefix="/api/v1/auth")


class RegisterForm(FlaskForm):
    name = StringField('name', validators=[DataRequired()])
    email = StringField('email', validators=[DataRequired(), email()])
    password = StringField('password', validators=[Length(min=4),EqualTo('confirm_password', message="Password must match")])
    confirm_password = StringField('password', validators=[Length(min=4)])


@auth.post("/register")
def register():
    form = RegisterForm()
    if form.validate_on_submit():
        return "Valid", HTTP_201_CREATED
    else:
        return jsonify({"error": form.errors}), HTTP_400_BAD_REQUEST


@auth.post("/login")
def login():
    return "User login"