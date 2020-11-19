from django.http import JsonResponse, HttpResponse
from django.db import IntegrityError
from django.core.exceptions import ObjectDoesNotExist

from UltraNote.models import User
from UltraNote.status_code import StatusCode
import json

def get(request, id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    user = User.object.get(id = id)
    response_data["content"] = {
        "id": user.id,
        "email": user.email,
        "name": user.name,
        "gender": user.gender
    }

    return JsonResponse(response_data)

def register(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_content = json.loads(request.body.decode("utf-8"))

        email = req_content["email"]
        password = req_content["password"]
        name = req_content["name"]
        gender = req_content["gender"]

        User.object.create(
            email = email,
            password = password,
            name = name,
            gender = gender
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except IntegrityError:
        response_data["statusCode"] = StatusCode.ALREADY_REGISTERED

    return JsonResponse(response_data)

def login(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_content = json.loads(request.boby.decode("utf-8"))

        email = req_content["email"]
        password = req_content["password"]

        user = User.object.get(email=email, password=password)
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.VALIDATION_ERR

    return JsonResponse(response_data)