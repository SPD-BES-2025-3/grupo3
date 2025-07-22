from mongoengine import connect

def init_db():
    connect(
        db="mongodb",
        host="localhost",
        port=27017
    )
