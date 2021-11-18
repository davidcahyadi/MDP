from flask import Blueprint, request, jsonify
from wtforms import StringField
from wtforms.validators import DataRequired, email, EqualTo, Length
from flask_wtf import FlaskForm
from src.constant.http_status_codes import HTTP_400_BAD_REQUEST, HTTP_403_FORBIDDEN, HTTP_201_CREATED, HTTP_200_OK
from src.constant.secure_codes import ACCESS, REFRESH, EXPIRED, SUCCESS, INVALID
from src.models.models import User
from src.models.database import db
from src.services.security import validate_token, generate_token, token_required

auth = Blueprint("auth", __name__, url_prefix="/api/v1/auth")


class RegisterForm(FlaskForm):
    name = StringField('name', validators=[DataRequired()])
    email = StringField('email', validators=[DataRequired(), email()])
    password = StringField('password',
                           validators=[Length(min=4), EqualTo('confirm_password', message="Password must match")])
    confirm_password = StringField('password', validators=[Length(min=4)])


@auth.post("/register")
def register():
    form = RegisterForm()
    if form.validate_on_submit():
        email = form.email.data
        password = form.password.data
        name = form.name.data

        user = User(name, email, password)
        db.session.add(user)
        db.session.commit()
        return "success", HTTP_200_OK
    else:
        return jsonify({"error": form.errors}), HTTP_400_BAD_REQUEST


class LoginForm(FlaskForm):
    email = StringField('email', validators=[DataRequired(), email()])
    password = StringField('password', validators=[DataRequired()])


@auth.post("/login")
def login():
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter_by(email=form.email.data).first()
        if user:
            if user.verify_password(form.password.data):
                return jsonify({"refresh": generate_token(user.id, REFRESH, 30 * 24 * 3600),
                                "access": generate_token(user.id, ACCESS, 300)
                                }), HTTP_200_OK
            else:
                return jsonify({"error": {"login": "Wrong password"}}), HTTP_403_FORBIDDEN
        else:
            return jsonify({"error": {"login": "User not found"}}), HTTP_403_FORBIDDEN
    else:
        return jsonify({"error": form.errors}), HTTP_400_BAD_REQUEST


@auth.post("/token/refresh")
def issued_refresh_token():
    print("Auth: ", request.headers["Auth"])
    result = validate_token(request.authorization)
    if result["code"] == SUCCESS:
        if result["mode"] == REFRESH:
            return jsonify({"refresh": generate_token(result["sub"], REFRESH, 30 * 24 * 3600)}), HTTP_200_OK
        else:
            return jsonify({"error": {"token": "wrong mode"}}), HTTP_403_FORBIDDEN
    else:
        return jsonify({"error": {"token": "invalid" if result["code"] == INVALID else "expired"}}), HTTP_403_FORBIDDEN


@auth.post("/token/access")
@token_required
def issued_access_token():
    print(request.authorization)
    result = validate_token(request.authorization)
    if result["code"] == SUCCESS:
        if result["mode"] == ACCESS:
            return jsonify({"access": generate_token(result["sub"], REFRESH, 300)}), HTTP_200_OK
        else:
            return jsonify({"error": {"token": "wrong mode"}}), HTTP_403_FORBIDDEN
    else:
        return jsonify({"error": {"token": "invalid" if result["code"] == INVALID else "expired"}}), HTTP_403_FORBIDDEN
