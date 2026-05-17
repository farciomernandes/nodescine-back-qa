# CineSK — Copilot Instructions

## Projeto
**Nome:** CineSK Backend  
**Domínio:** Plataforma de streaming/venda de filmes independentes  
**Tipo:** REST API — Spring Boot (Java 21)  
**Base URL:** `http://<host>:8080/api`

## Stack
| Camada | Tecnologia |
|--------|------------|
| Linguagem | Java 21 |
| Framework | Spring Boot 3.x |
| Segurança | Spring Security + JWT (stateless) |
| ORM | Spring Data JPA + Hibernate |
| Migrations | Flyway |
| Storage | Cloudflare R2 (via `R2Service`) |
| Pagamentos | Asaas (PIX, Boleto, Cartão) |
| Email | Spring Mail (SMTP) |
| Docs | SpringDoc OpenAPI (Swagger) |
| Build | Maven |
| Container | Docker + docker-compose |

## Princípios Inegociáveis
1. **Specs Before Code** — toda feature deve ter spec TOON antes de implementação.
2. **DTO nunca é Entidade** — `EnhancedMovieResponse` e demais DTOs nunca são persistidos diretamente.
3. **Service é a única camada com lógica de negócio** — Controllers apenas delegam; Repositories apenas consultam.
4. **JWT Stateless** — sem sessão HTTP; toda autenticação via `Authorization: Bearer <token>`.
5. **Conteúdo adulto proibido** — `isAdultConfirmed = true` lança exceção em `create` e `update` de filmes.

## Vocabulário de Domínio (resumo)
| Termo | Classe Java | Significado |
|-------|-------------|-------------|
| Movie | `Movie` / `EnhancedMovieResponse` | Filme cadastrado na plataforma |
| Director | `User` com role `MOVIE_DIRECTOR` | Criador de filmes |
| Customer | `User` com role `CUSTOMER` | Comprador/assinante |
| Moderator | `User` com role `MODERATOR` | Admin da plataforma |
| Transaction | `Transaction` | Compra ou aluguel de um filme |
| Report | `MovieReport` | Denúncia de conteúdo inadequado |
| Slug | campo `slug` em `Movie` | URL amigável gerada do título |
| ModerationStatus | `ModerationStatus` | Estado de revisão do filme |
| WalletId | `User.walletId` | ID da carteira Asaas do diretor |

## Padrões Arquiteturais
- Estrutura de pacotes: `controller → service → repository → entity`
- DTOs de entrada/saída ficam em `domain/<módulo>/dto/` ou na raiz do módulo
- Entidades JPA ficam em `domain/<módulo>/`
- Infra transversal (JWT, CORS, Security, Swagger) em `infrastructure/`
- Utilitários em `util/`
- Enums no mesmo pacote da entidade principal que os usa

## Regras de Segurança (resumo)
| Endpoint | Acesso |
|----------|--------|
| `GET /enhanced-films/**` | Público |
| `POST /enhanced-films/**/report` | Público |
| `POST/PUT /enhanced-films/**` | `MOVIE_DIRECTOR` ou `MODERATOR` |
| `GET /users/all` | `MODERATOR` |
| `GET /transactions/director/**` | `MODERATOR` |
| `POST /auth/register/moderator` | `MOVIE_DIRECTOR` ou `MODERATOR` |
| Todo o resto | Autenticado (qualquer role) |

> Ver `.sdd/technical-context.md` para detalhes completos.  
> Ver `.sdd/business-context.md` para regras de negócio.  
> Ver `.sdd/domain-vocabulary.md` para glossário completo.
