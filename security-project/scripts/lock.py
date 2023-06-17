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
# URL = 'https://localhost:8081/device/lock'
URL = 'http://localhost:8081/device/lock'
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


def info_locked():
    start_time = time.time()
    while True:
        time.sleep(5)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 4,
            'logType': 'INFO',
            'message': 'Lock is locked and still',
            'deviceType': 'SMART_LOCK',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


def info_unlocked():
    start_time = time.time()
    while True:
        time.sleep(5)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 4,
            'logType': 'INFO',
            'message': f'Lock is unlocked and still',
            'deviceType': 'SMART_LOCK',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


def warn_failed_unlock():
    start_time = time.time()
    while True:
        time.sleep(10)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 4,
            'logType': 'WARN',
            'message': f'Attempt to unlock failed',
            'deviceType': 'SMART_LOCK',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


def error_breakin():
    start_time = time.time()
    while True:
        time.sleep(15)  # OR time.sleep(random.randint(0, 10))
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 4,
            'logType': 'ERROR',
            'message': f'Someone broke in!',
            'deviceType': 'SMART_LOCK',
            'timestamp': convert_to_iso(datetime.now())
        }
        send_message(payload_json)


if __name__ == '__main__':
    if len(sys.argv) == 2:
        if int(sys.argv[1]) > 3600:
            DURATION = 3600
        else:
            DURATION = int(sys.argv[1])

    print(f"LOCK WILL RUN FOR {DURATION} SECONDS")
    # Create threads for each function
    info_locked_thread = threading.Thread(target=info_locked)
    info_unlocked_thread = threading.Thread(target=info_unlocked)
    warn_thread = threading.Thread(target=warn_failed_unlock)
    error_thread = threading.Thread(target=error_breakin)

    # Start the threads
    info_locked_thread.start()
    info_unlocked_thread.start()
    warn_thread.start()
    error_thread.start()

    # Wait for all threads to complete
    info_locked_thread.join()
    info_unlocked_thread.join()
    warn_thread.join()
    error_thread.join()

