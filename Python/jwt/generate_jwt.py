import jwt
import time
import os

# Set the secret key
secret_key = open("private_key.pem", "r").read()

# Set the key id to use
kid = ""

# Get the current time
iat = int(time.time())

header = {
    "kid": kid,
    "alg": "RS256"
}

# Set the payload (data to be encoded)
payload = {
    "iss": "dev_python_script",
    "iat": iat,
    "exp": iat + 60*60*24*7*52
}

# Encode the payload with the secret key and return the JWT
jwt_token = jwt.encode(payload, secret_key, algorithm='RS256', headers=header)

# Print the JWT
print(jwt_token)
