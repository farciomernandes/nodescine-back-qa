# context.md — CineSK

## Chave-Valor do Projeto

| Chave | Valor |
|-------|-------|
| **project_name** | CineSK Backend |
| **domain** | Streaming / venda de filmes independentes |
| **type** | REST API |
| **language** | Java 21 |
| **framework** | Spring Boot 3.x |
| **base_url** | `http://<host>:8080/api` |
| **auth_type** | JWT Bearer Token (stateless) |
| **storage** | Cloudflare R2 |
| **payment_gateway** | Asaas (PIX, Boleto, Cartão) |
| **email_provider** | SMTP Gmail |
| **migrations** | Flyway |
| **main_package** | `com.cine.sk.cinesk` |
| **main_class** | `SkApplication` |
| **build_tool** | Maven (`mvnw`) |
| **containerization** | Docker + docker-compose |
| **swagger_url** | `/api/swagger-ui.html` |
| **default_page_size** | `10` |
| **soft_delete_field** | `Movie.active` |
| **default_moderation** | `UNDER_REVIEW` (ao criar filme) |
| **adult_content** | **PROIBIDO** (`isAdultConfirmed = true` lança exceção) |
| **prohibited_terms_config** | `app.prohibited-terms` (CSV, env var) |
| **repo** | `farciomernandes/nodescine-back-qa` |
| **branch_default** | `main` |
| **last_updated** | 2026-05-17 |
