import datetime
from functools import wraps

import jwt
import os

from flask import request, jsonify

from src.constant.http_status_codes import HTTP_403_FORBIDDEN
from src.constant.secure_codes import EXPIRED, INVALID, SUCCESS, ACCESS


def generate_token(user_id, mode, expired):
    try:
        payload = {
            'exp': datetime.datetime.utcnow() + datetime.timedelta(seconds=expired),
            'mode': mode,
            'iat': datetime.datetime.utcnow(),
            'sub': user_id
        }
        return jwt.encode(payload, os.environ.get("SECRET_KEY"))
    except Exception as e:
        return e


def validate_token(token):
    try:
        payload = jwt.decode(token, os.environ.get("SECRET_KEY"),algorithms="HS256")
        return {"sub": payload["sub"], "mode": payload["mode"], "code": SUCCESS}
    except jwt.ExpiredSignatureError:
        return {"code": EXPIRED}
    except jwt.InvalidTokenError:
        return {"code": INVALID}


def token_required(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        token = None

        if 'x-api-key' in request.headers:
            token = request.headers["x-api-key"]

        if not token:
            return jsonify({"error": {"token": "token is missing"}}), HTTP_403_FORBIDDEN

        result = validate_token(token)
        if result["code"] != SUCCESS or result["mode"] != ACCESS:
            return jsonify({"error": {"token": "invalid" if result["code"] == INVALID else "expired"}}), HTTP_403_FORBIDDEN

        return f(result["sub"], *args, **kwargs)

    return decorator


def admin_gate(f):
    @wraps(f)
    def decorator(*args, **kwargs):
        sub = args[0]

        if sub == 0:
            return f(sub, *args, **kwargs)
        else:
            return jsonify({"error": {"auth": "Not Authorized"}}), HTTP_403_FORBIDDEN
    return decorator
