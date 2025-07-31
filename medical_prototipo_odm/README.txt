Passo a passo para a execução do backend e frontend desse código(Linux-Ubuntu)

1) Criar o seu ambiente virtual Python:

- O usuário deve primeiramente realizar a criação do seu ambiente virtual -> python3 -m venv venv
- Depois disso o usuário deve entrar no ambiente -> source venv/bin/activate

2) Depois disso o usuário, já dentro do ambiente virtual, deve realizar a instalação do arquivo de requisitos do código ->  pip install -r requirements.txt

3) O usuário deve se direcionar até a pasta em questão onde se encontra o código e executar o comando para a contrução do Banco de Dados não relacional -> docker-compose up --build

4) Depois disso o usuário deve instalar esses pacotes via prompt de comando: sudo apt install libxcb-xinerama0 libxcb-icccm4 libxcb-image0 libxcb-keysyms1 libx
cb-render-util0 libxcb-shape0 libxcb-sync1 libxcb-util1 libxcb-xfixes0 libxcb-xkb1 libxkbcommon-x11-0 libfontconfig1

5) Por fim deve-se executar o comando  em outro terminal para ter acesso ao frontend -> python3 main_frontend.py
