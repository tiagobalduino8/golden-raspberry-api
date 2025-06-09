# Golden Raspberry API

API para análise de intervalos entre prêmios consecutivos de produtores de filmes.

## Pré-requisitos
- Java 21+
- Maven 3.9.6+

## Instalação e Execução

```bash
# 1. Clone o repositório
git clone https://github.com/tiagobalduino8/golden-raspberry-api.git
cd golden-raspberry-api

# 2. Construa o projeto
mvn clean package

# 3. Execute a aplicação
java -jar target/quarkus-app/quarkus-run.jar


A aplicação estará disponível em: http://localhost:8080

# Endpoints
Importar dados do CSV
curl -X POST http://localhost:8080/movies/import
Resposta: "Filmes importados com sucesso"

Obter intervalos dos produtores
curl http://localhost:8080/movies/producers-intervals
Exemplo de resposta:

json
{
  "min": [
    {"producer": "Joel Silver", "interval": 1, "previousWin": 1990, "followingWin": 1991}
  ],
  "max": [
    {"producer": "Matthew Vaughn", "interval": 13, "previousWin": 2002, "followingWin": 2015}
  ]
}
# Banco de Dados (H2)
Console: http://localhost:8080/h2-console

Credenciais:

text
JDBC URL: jdbc:h2:mem:goldenRaspberry
User Name: sa
Password: <vazio>
Os dados são armazenados em memória (perdidos ao reiniciar a aplicação)

