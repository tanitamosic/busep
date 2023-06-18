import time

import requests
import json
import random
from datetime import datetime
from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import padding
import warnings
# Disable insecure request warnings
warnings.filterwarnings('ignore', category=requests.packages.urllib3.exceptions.InsecureRequestWarning)
DATETIME_FORMAT = "%Y-%m-%dT%H:%M:%S"
URL = 'https://localhost:8081/device/camera'
key = open('private_device_key.pem', 'rb')
PRIVATE_KEY = serialization.load_pem_private_key(key.read(), password=None)


def convert_to_iso(date: datetime):
    return date.strftime(DATETIME_FORMAT)


def sign_message(message_str, message_json):
    signature = PRIVATE_KEY.sign(
        message_str.encode('utf-8'),
        padding.PKCS1v15(),
        hashes.SHA256()
    )
    message_json['signature'] = signature.hex()
    payload = json.dumps(message_json, separators=(",", ":"))
    return payload


def send_message(payload_json):
    payload_str = json.dumps(payload_json, separators=(",", ":"))
    signed_payload = sign_message(payload_str, payload_json)
    headers = {
        'Content-Type': 'application/json'
    }
    response = requests.request('POST', URL, headers=headers, data=signed_payload, verify=False)
    if response.status_code != 200:
        print('An error has occurred.')
    else:
        print(f'type: {payload_json["logType"]}; message: {payload_json["message"]}')


def error():
    payload_json = {
        'deviceId': 5,
        'logType': 'ERROR',
        'message': f'WE GOT A BURGLAR',
        'deviceType': 'SMART_CAM',
        'timestamp': convert_to_iso(datetime.now())
    }
    send_message(payload_json)
    time.sleep(0.8)


if __name__ == '__main__':
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
    error()
