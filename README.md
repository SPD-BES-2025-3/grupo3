# grupo3
Repositório do grupo 3


primeiro, cheque as versões do java e maven para se casar com o que temos atualmente

```
Java = 21
Maven = 3.8.7
```
Configuração:

Baixando os pacotes necessários
```
sudo apt update
sudo apt install openjdk-17-jdk-headless
sudo apt install maven
```

Criação das pastas:

```
mkdir medical-prototipo
cd medical-prototipo
#Após isso insira o seguinte comando


mvn archetype:generate \
-DgroupId=br.group3 \
-DartifactId=medical-prototipo \
-DarchetypeArtifactId=maven-archetype-quickstart \
-DarchetypeVersion=1.4 \
-DinteractiveMode=false


```








