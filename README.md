# Card API (Desafio Hyperativa) — Java + Spring Boot + MySQL

API para **cadastrar** e **consultar** números de cartão (PAN) com **autenticação JWT**, armazenamento seguro (**SHA-256**) e importação via **arquivo TXT** (layout colunar/fixed-width), pensando em escalabilidade (processamento streaming).

## Requisitos
- Java 17
- Docker + Docker Compose

## Como rodar
1) Crie um arquivo `.env` na raiz do projeto:
```env
DB_NAME=cardapi
DB_USER=cardapi
DB_PASS=cardapi

# Gere um segredo Base64 (exemplo abaixo) e cole aqui
JWT_SECRET_BASE64=REPLACE_ME_BASE64_SECRET

# Opcional (default 60)
JWT_EXP_MINUTES=60
```

2) Suba a stack:
```bash
docker compose down -v
docker compose up --build
```

A API sobe em `http://localhost:8080`.

## Endpoints

### Saúde (público)
- `GET /api/v1/health` → `{"status":"ok"}`
- `GET /actuator` / `GET /actuator/health`

### Autenticação (público)
- `POST /api/v1/auth/login`

Exemplo:
```bash
curl -s -X POST http://localhost:8080/api/v1/auth/login       -H "Content-Type: application/json"       -d '{"username":"admin","password":"admin123"}'
```

### Cards (protegido por JWT)
Todos os endpoints abaixo exigem:
`Authorization: Bearer <token>`

#### Inserir 1 cartão
- `POST /api/v1/cards`
- Body: `{ "pan": "4456897919999999" }`
- Retorno: `{ "id": "<uuid>", "duplicate": false }`
  - Se já existir, retorna `duplicate=true` e o `id` existente.

Exemplo:
```bash
TOKEN="COLE_AQUI"

curl -i -X POST http://localhost:8080/api/v1/cards       -H "Content-Type: application/json"       -H "Authorization: Bearer $TOKEN"       -d '{"pan":"4456897919999999"}'
```

#### Consultar se existe
- `GET /api/v1/cards/lookup?pan=...`
- Retorno: `{ "exists": true, "id": "<uuid>" }`

Exemplo:
```bash
curl -i "http://localhost:8080/api/v1/cards/lookup?pan=4456897919999999"       -H "Authorization: Bearer $TOKEN"
```

#### Importar arquivo TXT (streaming)
- `POST /api/v1/cards/import` (multipart/form-data)
- Campo: `file`
- Retorno inclui contadores: `received`, `inserted`, `duplicate`, `invalid` + `importId`.

Exemplo:
```bash
curl -i -X POST http://localhost:8080/api/v1/cards/import       -H "Authorization: Bearer $TOKEN"       -F "file=@./sample.txt"
```

## Segurança dos dados
- O PAN **não é armazenado em texto puro**.
- A tabela `cards` guarda apenas `pan_hash` (SHA-256) e um `id` (UUID).
- No import, o processamento é feito **linha a linha**, sem carregar todo o arquivo em memória.

## Logging obrigatório (requisições + retornos)
A API registra logs **sem vazar dados sensíveis**:
- `requestId`, `method`, `path` (sem query string), `status`, `durationMs`, `principal`
- **Não** loga token JWT
- **Não** loga PAN (nem query string do lookup)

Além disso, a API devolve o header `X-Request-Id` em todas as respostas.

## Observações sobre o layout do TXT
O import segue o layout colunar informado:
- Header: `DESAFIO-HYPERATIVA...` (lote em [38–45], qtd em [46–51])
- Linhas de cartão: iniciam com `C` (PAN em [08–26])
- Footer: `LOTE....` (lote em [01–08], qtd em [09–14])

Linhas inválidas são contabilizadas em `invalid`.
