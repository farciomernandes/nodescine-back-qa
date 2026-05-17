# technical-context.md — CineSK

## Stack Detalhada

| Componente | Versão / Detalhe |
|------------|-----------------|
| Java | 21 |
| Spring Boot | 3.x |
| Spring Security | JWT stateless, `BCryptPasswordEncoder` |
| Spring Data JPA | Hibernate, `AbstractEntity` base |
| Flyway | Migrations em `src/main/resources/db/migration/` |
| Cloudflare R2 | `R2Service` — upload de poster/banner |
| Asaas | Gateway de pagamento PIX / Boleto / Cartão |
| Spring Mail | SMTP Gmail, retry configurável |
| SpringDoc OpenAPI | Swagger UI em `/api/swagger-ui.html` |
| Maven | Build, `mvnw` wrapper |
| Docker | `Dockerfile` + `docker-compose.yml` |

---

## Estrutura de Pacotes

```
com.cine.sk.cinesk/
├── controller/              ← Apenas @RestController, delega para Service
├── domain/
│   ├── auth/                ← AuthService, Role, DTOs de auth
│   ├── email/               ← EmailService, EmailRequest
│   ├── file/                ← R2Service, AwsService, FileManagerService
│   ├── movie/               ← Movie, MovieService, MovieRepository, EnhancedMovieResponse
│   │   ├── category/        ← Category, CategoryRepository
│   │   ├── genre/           ← Genre, GenreService, GenreDTO
│   │   └── report/          ← MovieReport, MovieReportRepository, ReportRequest
│   ├── transaction/         ← Transaction, TransactionService, DTOs
│   │   ├── payment/         ← PaymentDTO, PaymentMethodEnum, OrderStatusEnum
│   │   ├── rental/          ← Lógica de aluguel
│   │   └── webhook/         ← WebhookService, WebhookRequestDTO
│   └── user/                ← User, UserRepository, UserStatus
│       ├── dto/             ← UserResponse, UpdateUserDTO, CustomerRegisterDTO, etc.
│       └── service/         ← UserService, CustomUserDetailsService
├── infrastructure/
│   ├── cors/                ← CorsConfig, CorsProperties
│   ├── exception/           ← GlobalExceptionHandler, ErrorResponse
│   ├── jwt/                 ← JwtAuthenticationFilter
│   ├── security/            ← SecurityConfig, CustomAuthenticationEntryPoint
│   └── swagger/             ← SwaggerConfig
└── util/
    ├── ApiUtil.java
    ├── DateUtil.java
    └── JwtUtil.java
```

---

## Convenções de Nomenclatura

| Artefato | Padrão | Exemplo |
|----------|--------|---------|
| Entity | `<Nome>.java` sem sufixo | `Movie.java` |
| Repository | `<Nome>Repository.java` | `MovieRepository.java` |
| Service | `<Nome>Service.java` | `MovieService.java` |
| Controller | `<Nome>Controller.java` | `MovieController.java` |
| Response DTO | `<Nome>Response.java` ou `record` | `TransactionResponse.java` |
| Request DTO | `<Nome>DTO.java` ou `<Nome>Request.java` | `CreateTransactionDTO.java` |
| Enum | `<Nome>Enum.java` ou `<Nome>.java` | `PaymentMethodEnum.java`, `Role.java` |
| Migration | `V<yyyyMMddHHmmss>__<desc>.sql` | `V20260425150042__seed_data.sql` |

---

## Configuração por Ambiente (application.properties)

| Propriedade | Env Var | Default |
|-------------|---------|---------|
| `server.port` | `SERVER_PORT` | `8080` |
| `server.servlet.context-path` | `SERVER_SERVLET_CONTEXT_PATH` | `/api` |
| `spring.datasource.url` | `DATABASE_HOST` | — |
| `spring.flyway.enabled` | `SPRING_FLYWAY_ENABLED` | `false` |
| `app.prohibited-terms` | `APP_PROHIBITED_TERMS` | `""` |
| `email.retry.max-attempts` | `EMAIL_RETRY_MAX_ATTEMPTS` | `3` |

---

## Padrões de Segurança

- **JWT:** gerado e validado via `JwtUtil`, filtrado por `JwtAuthenticationFilter`
- **Stateless:** `SessionCreationPolicy.STATELESS`
- **CORS:** configurado via `CorsProperties` (lista de origens permitidas)
- **Roles:** `hasAnyAuthority(Role.<ROLE>.name())` no `SecurityConfig`
- **401:** Tratado por `CustomAuthenticationEntryPoint`

---

## Anti-patterns Proibidos

| Anti-pattern | Por quê |
|--------------|---------|
| `@Autowired` | Usar `@RequiredArgsConstructor` + `final` |
| Entity no ResponseEntity | Sempre retornar DTO |
| Lógica de negócio no Controller | Viola separação de responsabilidades |
| `System.out.println` | Usar SLF4J (`@Slf4j`) |
| DDL sem migration Flyway | Schema não rastreável |
| Persistir DTO diretamente | Viola princípio DTO ≠ Entity |

---

## Integração com R2 (Cloudflare Storage)

- Upload via `R2Service.upload(file, tipo, id, nome)` retorna `File` com `.getUri()`
- Tipos: `"poster"`, `"banner"`
- URLs ficam salvas nos campos `Movie.poster` e `Movie.banner`

---

## Integração com Asaas (Pagamentos)

- Criação via `TransactionService.create(CreateTransactionDTO)`
- Webhook recebido em `POST /asaas/webhook`
- Processamento assíncrono via `WebhookService.processWebhookPayloadAsync(event, paymentId)`
- Métodos suportados: `PIX`, `BOLETO`, `CREDIT_CARD`
