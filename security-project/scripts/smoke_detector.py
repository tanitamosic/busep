import requests
import json
import time
import threading
import random
import sys
from datetime import datetime
from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import padding
import warnings
# Disable insecure request warnings
warnings.filterwarnings('ignore', category=requests.packages.urllib3.exceptions.InsecureRequestWarning)
DATETIME_FORMAT = "%Y-%m-%dT%H:%M:%S"
URL = 'https://localhost:8081/device/smoke_detector'
DURATION = 30
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


def info():
    start_time = time.time()
    while True:
        time.sleep(3)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 3,
            'logType': 'INFO',
            'message': f'Air is clean',
            'deviceType': 'SMART_SMOKE',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


def warn_cigars():
    start_time = time.time()
    while True:
        time.sleep(3)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 3,
            'logType': 'WARN',
            'message': f'There is cigar smoke',
            'deviceType': 'SMART_SMOKE',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)

def warn_furnace():
    start_time = time.time()
    while True:
        time.sleep(3)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 3,
            'logType': 'WARN',
            'message': f'Furnace is smoking',
            'deviceType': 'SMART_SMOKE',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)

def warn_oven():
    start_time = time.time()
    while True:
        time.sleep(3)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 3,
            'logType': 'WARN',
            'message': f'Oven is smoking',
            'deviceType': 'SMART_SMOKE',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


def error():
    start_time = time.time()
    while True:
        time.sleep(6)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 3,
            'logType': 'ERROR',
            'message': f'FIRE FIRE FIRE ! ! !',
            'deviceType': 'SMART_SMOKE',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


if __name__ == '__main__':
    if len(sys.argv) == 2:
        if int(sys.argv[1]) > 3600:
            DURATION = 3600
        else:
            DURATION = int(sys.argv[1])

    print(f"SMOKE DETECTOR WILL RUN FOR {DURATION} SECONDS")
    # Create threads for each function
    info_thread = threading.Thread(target=info)
    warn_cigars_thread = threading.Thread(target=warn_cigars)
    warn_furnace_thread = threading.Thread(target=warn_furnace)
    warn_oven_thread = threading.Thread(target=warn_oven)
    error_thread = threading.Thread(target=error)

    # Start the threads
    info_thread.start()
    warn_cigars_thread.start()
    warn_furnace_thread.start()
    warn_oven_thread.start()
    error_thread.start()

    # Wait for all threads to complete
    info_thread.join()
    warn_cigars_thread.join()
    warn_furnace_thread.join()
    warn_oven_thread.join()
    error_thread.join()

