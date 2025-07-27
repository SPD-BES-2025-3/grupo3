from flask import Flask
from config import init_db 
from controllers.consulta_controller import consulta_bp
from controllers.prontuario_controller import prontuario_bp

app = Flask(__name__)
init_db()

app.register_blueprint(consulta_bp)
app.register_blueprint(prontuario_bp)

if __name__ == '__main__':

    app.run(debug=True)
