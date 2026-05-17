# CineSK Backend — Contexto Completo do Projeto

> Documento mestre para onboarding de IAs e desenvolvedores.  
> Atualizado em: 2026-05-17

---

## 1. Visão Geral

**CineSK** é uma REST API desenvolvida em **Java 21 + Spring Boot 3.x** que serve como backend de uma plataforma de streaming e venda de **filmes independentes brasileiros**.

- Diretores de cinema publicam seus filmes e recebem por vendas/aluguéis.
- Clientes compram ou alugam filmes com pagamento via PIX, Boleto ou Cartão.
- Moderadores administram usuários e moderam o conteúdo publicado.
- O público pode navegar, buscar filmes e denunciar conteúdo sem autenticação.

**Base URL:** `http://<host>:8080/api`

---

## 2. Stack de Tecnologias

| Camada | Tecnologia |
|--------|------------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.x |
| Segurança | Spring Security + JWT (stateless) |
| ORM | Spring Data JPA + Hibernate |
| Migrations | Flyway |
| Storage | Cloudflare R2 (`R2Service`) |
| Pagamentos | Asaas — PIX, Boleto, Cartão |
| Email | Spring Mail (SMTP Gmail) |
| Docs | SpringDoc OpenAPI (Swagger UI `/api/swagger-ui.html`) |
| Build | Maven (`mvnw`) |
| Container | Docker + docker-compose |

---

## 3. Estrutura de Pacotes

```
com.cine.sk.cinesk/
├── controller/
│   ├── AuthController.java
│   ├── GenreController.java
│   ├── MovieController.java
│   ├── TransactionController.java
│   ├── UserController.java
│   └── WebHookController.java
├── domain/
│   ├── auth/          → AuthService, Role (enum), DTOs de auth
│   ├── email/         → EmailService, EmailRequest
│   ├── file/          → R2Service, AwsService, FileManagerService, File, StorageType
│   ├── movie/         → Movie, MovieService, MovieRepository, EnhancedMovieResponse
│   │   ├── category/  → Category, CategoryRepository
│   │   ├── genre/     → Genre, GenreService, GenreDTO
│   │   └── report/    → MovieReport, MovieReportRepository, ReportRequest
│   ├── transaction/   → Transaction, TransactionService, DTOs
│   │   ├── payment/   → PaymentDTO, PaymentMethodEnum, OrderStatusEnum
│   │   ├── rental/    → lógica de aluguel
│   │   └── webhook/   → WebhookService, WebhookRequestDTO
│   └── user/          → User, UserRepository, UserStatus (enum)
│       ├── dto/       → UserResponse, UpdateUserDTO, CustomerRegisterDTO, MovieDirectorRegisterDTO, TransactionDTO
│       └── service/   → UserService, CustomUserDetailsService
├── infrastructure/
│   ├── cors/          → CorsConfig, CorsProperties
│   ├── exception/     → GlobalExceptionHandler, ErrorResponse
│   ├── jwt/           → JwtAuthenticationFilter
│   ├── security/      → SecurityConfig, CustomAuthenticationEntryPoint
│   └── swagger/       → SwaggerConfig
└── util/
    ├── ApiUtil.java
    ├── DateUtil.java
    └── JwtUtil.java
```

---

## 4. Enums

| Enum | Valores |
|------|---------|
| `Role` | `CUSTOMER`, `MOVIE_DIRECTOR`, `MODERATOR` |
| `UserStatus` | `ACTIVE`, `INACTIVE` |
| `MovieType` | `YOUTUBE`, `VIMEO_PRO`, `VIMEO_FREE` |
| `ModerationStatus` | `PUBLISHED`, `UNDER_REVIEW`, `REMOVED` |
| `PaymentMethodEnum` | `CREDIT_CARD`, `BOLETO`, `PIX` |
| `OrderStatusEnum` | `PENDING`, `PAID`, `CANCELED`, `FAILED` |
| `TransactionStatus` | `ACTIVE`, `EXPIRED`, `CANCELLED`, `REFUNDED` |

---

## 5. Endpoints Completos

### 5.1 Auth — `/auth`

| Método | Path | Auth | Roles | Descrição |
|--------|------|------|-------|-----------|
| POST | `/auth/login` | Público | — | Login, retorna JWT |
| POST | `/auth/register/customer` | Público | — | Registro de Customer |
| POST | `/auth/register/director` | Público | — | Registro simplificado de Director |
| POST | `/auth/register/moderator` | Bearer | `MOVIE_DIRECTOR`, `MODERATOR` | Registro de Moderator |
| POST | `/auth/change-password` | Bearer | Qualquer | Altera senha |
| GET | `/auth/me` | Bearer | Qualquer | Dados do usuário autenticado |

#### Body: `POST /auth/login`
```json
{ "email": "string", "password": "string" }
```
#### Body: `POST /auth/register/director`
```json
{ "email": "string", "password": "string", "name": "string", "avatar": "string?" }
```
#### Body: `POST /auth/register/customer`
```json
{
  "email": "string", "password": "string", "name": "string",
  "cpf": "string", "phone": "string", "postalCode": "string",
  "address": "string", "addressNumber": "string", "complement": "string",
  "province": "string", "roles": ["CUSTOMER"], "birthDate": "yyyy-MM-dd",
  "incomeValue": 0, "avatar": "string?"
}
```

---

### 5.2 Movies — `/enhanced-films`

| Método | Path | Auth | Roles | Descrição |
|--------|------|------|-------|-----------|
| GET | `/enhanced-films` | Público | — | Listagem paginada com busca geral |
| GET | `/enhanced-films/filter` | Público | — | Listagem com filtros avançados |
| GET | `/enhanced-films/{id}` | Público | — | Detalhe por ID |
| GET | `/enhanced-films/slug/{slug}` | Público | — | Detalhe por slug |
| GET | `/enhanced-films/me` | Bearer | `MOVIE_DIRECTOR` | Filmes do diretor autenticado |
| POST | `/enhanced-films` | Bearer | `MOVIE_DIRECTOR`, `MODERATOR` | Criar filme (multipart) |
| PUT | `/enhanced-films/{id}` | Bearer | `MOVIE_DIRECTOR`, `MODERATOR` | Atualizar filme (multipart) |
| POST | `/enhanced-films/poster` | Bearer | `MOVIE_DIRECTOR`, `MODERATOR` | Upload do poster |
| POST | `/enhanced-films/banner` | Bearer | `MOVIE_DIRECTOR`, `MODERATOR` | Upload do banner |
| DELETE | `/enhanced-films/{id}` | Bearer | `MOVIE_DIRECTOR`, `MODERATOR` | Soft delete do filme |
| POST | `/enhanced-films/{id}/report` | Público | — | Denunciar filme |

#### Query Params: `GET /enhanced-films`
| Param | Tipo | Descrição |
|-------|------|-----------|
| `searchTerm` | String? | Busca geral |
| `page` | Integer | Página (0-indexed) |
| `size` | Integer | Itens por página |
| `sort` | String | Ex: `title,asc` |

#### Query Params: `GET /enhanced-films/filter`
| Param | Tipo | Descrição |
|-------|------|-----------|
| `title` | String? | Filtro por título |
| `description` | String? | Filtro por sinopse |
| `director` | String? | Filtro por diretor |
| `genre` | String? | Filtro por gênero |
| `category` | String? | Filtro por categoria |
| `cast` | String? | Filtro por elenco |
| `page` | Integer | Default: 0 |
| `size` | Integer | Default: 10 |
| `sort` | String | Default: `title` |

#### Body: `POST /enhanced-films` (multipart/form-data)
| Part | Tipo | Obrigatório |
|------|------|-------------|
| `dto` | JSON (EnhancedMovieResponse) | ✅ |
| `poster` | File | ❌ |
| `fileBanner` | File | ❌ |

#### Objeto `EnhancedMovieResponse`
```json
{
  "id": 1,
  "title": "string",
  "director": "string",
  "year": 2024,
  "category": "string",
  "price": 19.90,
  "genres": [{ "id": 1, "name": "Drama" }],
  "duration": "1h 45m",
  "poster": "https://...",
  "banner": "https://...",
  "background": "https://...",
  "movieUrl": "https://...",
  "trailerUrl": "https://...",
  "synopsis": "string",
  "slug": "titulo-do-filme",
  "cast": ["Ator 1", "Ator 2"],
  "isAdultConfirmed": false,
  "producerDeadline": "2025-12-31",
  "movieType": "YOUTUBE",
  "moderationStatus": "UNDER_REVIEW",
  "format": "string"
}
```

> ⚠️ **Nota:** `movieUrl` retorna `null` em listagens (endpoint `/enhanced-films` e `/filter`).  
> O campo só é preenchido em endpoints de detalhe (`/{id}`, `/slug/{slug}`, `/me`).

#### Body: `POST /enhanced-films/{id}/report`
```json
{ "reporterEmail": "string", "reason": "string" }
```

---

### 5.3 Users — `/users`

| Método | Path | Auth | Roles | Descrição |
|--------|------|------|-------|-----------|
| GET | `/users/{id}` | Bearer | Qualquer | Buscar usuário por ID |
| GET | `/users/all` | Bearer | `MODERATOR` | Listar todos os usuários |
| PUT | `/users/{id}` | Bearer | Qualquer | Atualizar parcialmente usuário |
| DELETE | `/users/{id}` | Bearer | Qualquer | Remover usuário |

#### Body: `PUT /users/{id}`
```json
{
  "name": "string?",
  "email": "string?",
  "avatar": "string?",
  "roles": ["CUSTOMER"],
  "status": "ACTIVE | INACTIVE"
}
```

#### Objeto `UserResponse`
```json
{
  "id": 45,
  "name": "string",
  "email": "string",
  "status": "ACTIVE",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-22T14:15:00",
  "cpf": "string",
  "phone": "string",
  "postalCode": "string",
  "address": "string",
  "addressNumber": "string",
  "complement": "string",
  "province": "string",
  "roles": ["CUSTOMER"],
  "transactions": [],
  "totalAmount": 0.00,
  "totalMovie": 0,
  "walletId": "string"
}
```

---

### 5.4 Genres — `/genres`

| Método | Path | Auth | Roles | Descrição |
|--------|------|------|-------|-----------|
| GET | `/genres` | Público | — | Listar todos os gêneros |
| DELETE | `/genres/{id}` | Bearer | `MOVIE_DIRECTOR`, `MODERATOR` | Remover gênero |

---

### 5.5 Transactions — `/transactions`

| Método | Path | Auth | Roles | Descrição |
|--------|------|------|-------|-----------|
| POST | `/transactions` | Bearer | Qualquer | Criar transação real (Asaas) |
| POST | `/transactions/mock` | Bearer | Qualquer | Criar transação mock |
| GET | `/transactions/{id}` | Bearer | Qualquer | Buscar transação por ID |
| GET | `/transactions/me` | Bearer | Qualquer | Transações do usuário autenticado |
| GET | `/transactions/user/{id}` | Bearer | Qualquer | Transações de um usuário |
| GET | `/transactions/summary` | Bearer | Qualquer | Resumo geral de vendas |
| GET | `/transactions/summary/by-movie` | Bearer | Qualquer | Vendas agrupadas por filme (autenticado) |
| GET | `/transactions/director/summary/{id}` | Bearer | `MODERATOR` | Resumo de vendas de um diretor |
| GET | `/transactions/director/summary/by-movie/{id}` | Bearer | `MODERATOR` | Vendas por filme de um diretor |

#### Body: `POST /transactions`
```json
{
  "movieId": 1,
  "payment": {
    "method": "PIX | BOLETO | CREDIT_CARD",
    "creditCardHolder": "string?",
    "creditCardNumber": "string?",
    "creditCardExpiration": "string?",
    "creditCardSecurityCode": "string?"
  }
}
```

#### Objeto `TransactionResponse`
```json
{
  "id": 1,
  "transactionId": "pay_abc123",
  "movieId": 1,
  "amount": 19.90,
  "date": "2024-01-20",
  "status": "PENDING",
  "pix": { "encodedImage": "base64...", "payload": "00020126..." }
}
```

#### Objeto `SalesTransactionSuDTO`
```json
{ "totalMovie": 42, "totalAmount": 15890.50, "totalUser": 128 }
```

---

### 5.6 Webhook — `/asaas/webhook`

| Método | Path | Auth | Descrição |
|--------|------|------|-----------|
| POST | `/asaas/webhook` | Público | Recebe notificações do gateway Asaas |

#### Body
```json
{ "event": "PAYMENT_CONFIRMED", "payment": { "id": "pay_abc123" } }
```
#### Resposta
```json
{ "received": true }
```

---

## 6. Regras de Negócio

| # | Regra | Camada |
|---|-------|--------|
| R1 | `isAdultConfirmed = true` é proibido em create e update de filmes | `MovieService` |
| R2 | Director precisa de `walletId` configurado para publicar | `MovieService.create()` |
| R3 | Slug único gerado automaticamente do título | `MovieService` |
| R4 | Filme novo entra como `UNDER_REVIEW` por padrão | `MovieService.create()` |
| R5 | Termos proibidos bloqueiam título e sinopse (config via env) | `MovieService.checkProhibitedTerms()` |
| R6 | Filmes são soft-deleted (`active = false`) | `MovieService.delete()` |
| R7 | Listagens retornam apenas filmes com `active = true` | `MovieRepository` |
| R8 | `movieUrl` é omitido em listagens públicas paginadas | `MovieService.toDTOMainPage()` |

---

## 7. Segurança

### Matriz de Acesso

| Endpoint | Método | Acesso |
|----------|--------|--------|
| `/auth/login`, `/auth/register/customer`, `/auth/register/director` | POST | Público |
| `/auth/register/moderator` | POST | `MOVIE_DIRECTOR`, `MODERATOR` |
| `/enhanced-films/**` | GET | Público |
| `/enhanced-films/*/report` | POST | Público |
| `/enhanced-films/**` | POST, PUT | `MOVIE_DIRECTOR`, `MODERATOR` |
| `/genres/**` | GET | Público |
| `/genres/**` | DELETE | `MOVIE_DIRECTOR`, `MODERATOR` |
| `/transactions/director/**` | GET | `MODERATOR` |
| `/users/all` | GET | `MODERATOR` |
| `/asaas/webhook/**` | POST | Público |
| Qualquer outro | * | Autenticado (Bearer) |

---

## 8. Princípios Inegociáveis

1. **Specs Before Code** — toda feature começa com spec TOON em `.github/specs/`.
2. **DTO ≠ Entity** — `EnhancedMovieResponse` e demais DTOs nunca são persistidos.
3. **Service é dono da lógica** — Controller apenas delega; Repository apenas consulta.
4. **JWT Stateless** — sem sessão HTTP; `Authorization: Bearer <token>` em todo request autenticado.
5. **Conteúdo adulto proibido** — `isAdultConfirmed = true` lança exceção imediatamente.

---

## 9. Estrutura SDD

```
.github/
├── copilot-instructions.md    ← Contexto denso para o Copilot (carregado automaticamente)
├── agents/
│   ├── planning.agent.md      ← Criação de specs a partir de requisitos
│   ├── architecture.agent.md  ← Contratos técnicos e interfaces
│   ├── implementation.agent.md← Geração de código de produção
│   └── review.agent.md        ← Validação contra spec e padrões
├── skills/
│   ├── feature-spec/SKILL.md  ← Gera spec TOON
│   ├── migration-generator/SKILL.md ← Gera SQL de migration
│   └── security-rule/SKILL.md ← Adiciona regras ao SecurityConfig
└── specs/                     ← Specs TOON versionadas por módulo
.sdd/
├── context.md                 ← Chave-valor do projeto
├── business-context.md        ← Domínio, atores, fluxos de negócio
├── technical-context.md       ← Stack, padrões, anti-patterns
├── domain-vocabulary.md       ← Glossário ubíquo
├── definitions/
│   ├── dor.yaml               ← Definition of Ready
│   └── dod.yaml               ← Definition of Done
└── templates/
    ├── feature-spec.toon.yaml ← Template de spec TOON
    ├── epic.md                ← Template de epic
    └── story.md               ← Template de story
```
