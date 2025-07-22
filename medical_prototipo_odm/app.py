from flask import Flask
from config import init_db
from views.user_routes import user_bp

app = Flask(__name__)
init_db()
app.register_blueprint(user_bp)

if __name__ == '__main__':
    app.run(debug=True)
