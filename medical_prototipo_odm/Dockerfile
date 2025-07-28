# Use uma imagem base oficial do Python
FROM python:3.11-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Instala dependências do sistema necessárias para o MongoDB e outras bibliotecas
RUN apt-get update && apt-get install -y \
    gcc \
    g++ \
    && rm -rf /var/lib/apt/lists/*

# Copia o arquivo de requirements primeiro para aproveitar o cache do Docker
# Como requirements.txt está na raiz do projeto
COPY requirements.txt .

# Instala as dependências Python
RUN pip install --no-cache-dir -r requirements.txt

# Copia todo o código da aplicação
# Isso inclui a pasta src/ e outros arquivos na raiz
COPY . .

# Expõe a porta que o Flask vai usar
EXPOSE 5000

# Define variáveis de ambiente
ENV FLASK_APP=src/app.py
ENV FLASK_ENV=production
ENV PYTHONPATH=/app:/app/src

# Comando para executar a aplicação
# Como app.py está dentro de src/, precisamos especificar o caminho
CMD ["python", "src/app.py"]

