import os
from mongoengine import connect

def init_db():
    mongo_uri = os.getenv("MONGODB_URI", "mongodb://admin:1234@localhost:27017/medical_db?authSource=admin")
    connect(host=mongo_uri)

   
