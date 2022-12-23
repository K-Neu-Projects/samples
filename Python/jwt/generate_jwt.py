import jwt
import time

# Set the secret key
secret_key = ""

# Set the key id to use
kid = ""

# Get the current time
iat = int(time.time())

# Set the payload (data to be encoded)
payload = {
    "kid": kid,
    "iss": "dev_python_script",
    "iat": iat,
    "exp": iat + 60*60*24*7*52
}

# Encode the payload with the secret key and return the JWT
jwt_token = jwt.encode(payload, secret_key, algorithm='HS256')

# Print the JWT
print(jwt_token)
