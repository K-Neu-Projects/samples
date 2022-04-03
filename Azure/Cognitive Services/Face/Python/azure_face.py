# Copyright (c) Microsoft Corporation. All rights reserved.
# Licensed under the MIT License.

import json
from tkinter import image_names
import requests
import os
from pprint import pprint
from config import subscription_key, endpoint

'''
This sample makes a call to the Face API with a URL image query to detect faces and features in an image.
Face API: https://westus.dev.cognitive.microsoft.com/docs/services/563879b61984550e40cbbe8d/operations/563879b61984550f30395236
'''


# Request headers.
headers = {
    'Content-Type': 'application/octet-stream',
    'Ocp-Apim-Subscription-Key': subscription_key,
}

# Request parameters.
params = {
    'returnFaceId': 'true',
    'returnFaceLandmarks': 'false',
    'returnFaceAttributes': 'accessories,age,blur,emotion,exposure,facialhair,gender,glasses,hair,headpose,makeup,noise,occlusion,smile',
}

def call_face(image_path):

    with open(image_path, 'rb') as file:
        data = file.read()

    try:
        # Call API. 
        response = requests.post(endpoint, headers=headers, params=params, data=data)
        response.raise_for_status()

        print("\nJSON Response:\n")
        pprint(response.json())

        return response.json()

    except Exception as e:
        print('Error:')
        print(e)
