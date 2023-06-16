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
URL = 'https://localhost:8081/device/thermometer'
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


def info_left():
    start_time = time.time()
    while True:
        time.sleep(3)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'INFO',
            'message': f'Object moved left',
            'deviceType': 'SMART_CAM',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)
        
def info_right():
    start_time = time.time()
    while True:
        time.sleep(3)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'INFO',
            'message': f'Object moved right',
            'deviceType': 'SMART_CAM',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)
        
def info_still():
    start_time = time.time()
    while True:
        time.sleep(3)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'INFO',
            'message': f'Object is still',
            'deviceType': 'SMART_CAM',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


def warn_running():
    start_time = time.time()
    while True:
        time.sleep(6)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'WARN',
            'message': f'Object is running',
            'deviceType': 'SMART_CAM',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


def error():
    start_time = time.time()
    while True:
        time.sleep(12)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'ERROR',
            'message': f'Object is sneaking, acting suspicious',
            'deviceType': 'SMART_CAM',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


if __name__ == '__main__':
    if len(sys.argv) == 2:
        if int(sys.argv[1]) > 3600:
            DURATION = 3600
        else:
            DURATION = int(sys.argv[1])

    print(f"CAMERA WILL RUN FOR {DURATION} SECONDS")
    # Create threads for each function
    info_left_thread = threading.Thread(target=info_left)
    info_right_thread = threading.Thread(target=info_right)
    info_still_thread = threading.Thread(target=info_still)
    warn_thread = threading.Thread(target=warn_running)
    error_thread = threading.Thread(target=error)

    # Start the threads
    info_left_thread.start()
    info_right_thread.start()
    info_still_thread.start()
    warn_thread.start()
    error_thread.start()

    # Wait for all threads to complete
    info_left_thread.join()
    info_right_thread.join()
    info_still_thread.join()
    warn_thread.join()
    error_thread.join()

