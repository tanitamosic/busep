import requests
import json
import time
import threading
import random
from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import padding


URL = 'https://localhost:8081/device'
DURATION = 10


def sign_message(message_str, message_json):
    signature = private_key.sign(
        message_str.encode('utf-8'),
        padding.PKCS1v15(),
        hashes.SHA256()
    )
    message_json['signature'] = signature.hex()
    payload = json.dumps(message_json)
    return payload


def send_message(payload_json):
    payload_str = json.dumps(payload_json)
    signed_payload = sign_message(payload_str, payload_json)
    headers = {
        'Content-Type': 'application/json'
    }
    response = requests.request('POST', URL + '/thermometer', headers=headers, data=signed_payload)
    if response.status_code != 200:
        print('An error has occurred.')


def info():
    start_time = time.time()
    while True:
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'INFO',
            'message': f'{random.randint(17, 23)}',
            'deviceType': 'SMART_TEMP'
        }
        send_message(payload_json)
        time.sleep(0.5)  # OR time.sleep(random.randint(0, 10))


def warn():
    start_time = time.time()
    while True:
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'WARN',
            'message': f'{random.choice([random.randint(9, 16), random.randint(24, 30)])}',
            'deviceType': 'SMART_TEMP'
        }
        send_message(payload_json)
        time.sleep(1)  # OR time.sleep(random.randint(0, 10))


def error():
    start_time = time.time()
    while True:
        elapsed_time = time.time() - start_time
        if elapsed_time >= DURATION:
            break
        payload_json = {
            'deviceId': 1,
            'logType': 'ERROR',
            'message': f'{random.choice([random.randint(0, 8), random.randint(30, 40)])}',
            'deviceType': 'SMART_TEMP'
        }
        send_message(payload_json)
        time.sleep(2)  # OR time.sleep(random.randint(0, 10))


if __name__ == '__main__':
    with open('private_device_key.pem', 'rb') as key_file:
        private_key = serialization.load_pem_private_key(
            key_file.read(),
            password=None
        )
    while True:
        # Create threads for each function
        info_thread = threading.Thread(target=info)
        warn_thread = threading.Thread(target=warn)
        error_thread = threading.Thread(target=error)

        # Start the threads
        info_thread.start()
        warn_thread.start()
        error_thread.start()

        # Wait for all threads to complete
        info_thread.join()
        warn_thread.join()
        error_thread.join()

