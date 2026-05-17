# domain-vocabulary.md — CineSK

> Glossário ubíquo. Toda IA e todo dev deve usar **exatamente** estes termos.
> Nunca inventar sinônimos.

---

## Entidades de Domínio

| Termo | Classe Java | Tabela DB | Definição |
|-------|-------------|-----------|-----------|
| **Movie** | `Movie` | `movie` | Filme independente cadastrado na plataforma. Sempre retornado via `EnhancedMovieResponse`. |
| **Format** | `Format` | `movie_format` | Formato do filme (Curta, Média, Longa, Telefilme, Série, Minissérie). |
| **User** | `User` | `user` | Qualquer pessoa cadastrada. Diferenciada por `roles`. |
| **Director** | `User` com `roles = [MOVIE_DIRECTOR]` | `user` | Cria e publica filmes. Precisa de `walletId`. |
| **Customer** | `User` com `roles = [CUSTOMER]` | `user` | Compra ou aluga filmes. |
| **Moderator** | `User` com `roles = [MODERATOR]` | `user` | Administra plataforma, modera filmes, acessa dados de vendas. |
| **Transaction** | `Transaction` | `transaction` | Registro de compra ou aluguel de um filme por um Customer. |
| **Report** | `MovieReport` | `movie_report` | Denúncia de conteúdo inadequado em um filme. Pode ser feita por qualquer pessoa (pública). |
| **Genre** | `Genre` | `genre` | Gênero cinematográfico (Ação, Drama, etc.). Associado a filmes via `movie_genres`. |
| **Category** | `Category` | `category` | Categoria do filme (ex: Curta, Longa, Documentário). |

---

## DTOs Principais

| Termo | Classe Java | Sentido | Descrição |
|-------|-------------|---------|-----------|
| **EnhancedMovieResponse** | `EnhancedMovieResponse` | Saída | DTO completo de filme. Usado em respostas de listagem e detalhe. **Nunca persistido.** |
| **CreateTransactionDTO** | `CreateTransactionDTO` | Entrada | Payload para criar uma transação: `movieId` + `PaymentDTO`. |
| **TransactionResponse** | `TransactionResponse` | Saída | Resultado de uma transação criada, incluindo dados PIX se aplicável. |
| **TransactionDTO** | `TransactionDTO` (record) | Saída | Visão da transação com dados do filme e status de expiração. |
| **UserResponse** | `UserResponse` (record) | Saída | Perfil completo do usuário, incluindo transações e totais. |
| **CustomerRegisterDTO** | `CustomerRegisterDTO` | Entrada | Registro completo de Customer (CPF, endereço, nascimento, etc.). |
| **MovieDirectorRegisterDTO** | `MovieDirectorRegisterDTO` | Entrada | Registro simplificado de Director (email, senha, nome). |
| **ReportRequest** | `ReportRequest` | Entrada | Denúncia: `reporterEmail` + `reason`. |
| **PaymentDTO** | `PaymentDTO` | Entrada | Dados de pagamento: `method` + dados do cartão (se aplicável). |
| **SalesTransactionSuDTO** | `SalesTransactionSuDTO` | Saída | Resumo: `totalMovie`, `totalAmount`, `totalUser`. |
| **TransactionByMovieDTO** | `TransactionByMovieDTO` | Saída | Total de vendas agrupado por filme: `totalAmount` + `EnhancedMovieResponse`. |
| **GenreDTO** | `GenreDTO` (record) | Entrada/Saída | `{ id, name }` — usado dentro de `EnhancedMovieResponse`. |

---

## Enums

| Termo | Enum Java | Valores | Contexto |
|-------|-----------|---------|----------|
| **Role** | `Role` | `CUSTOMER`, `MOVIE_DIRECTOR`, `MODERATOR` | Autorização JWT. |
| **UserStatus** | `UserStatus` | `ACTIVE`, `INACTIVE` | Estado da conta do usuário. |
| **MovieType** | `MovieType` | `YOUTUBE`, `VIMEO_PRO`, `VIMEO_FREE` | Provedor do player de vídeo. Preferência: YouTube para GRATUITO, Vimeo para ALUGUEL. |
| **ModerationStatus** | `ModerationStatus` | `PUBLISHED`, `UNDER_REVIEW`, `REMOVED` | Estado de moderação do filme. Padrão ao criar: `UNDER_REVIEW`. |
| **PaymentMethodEnum** | `PaymentMethodEnum` | `CREDIT_CARD`, `BOLETO`, `PIX` | Método de pagamento Asaas. |
| **OrderStatusEnum** | `OrderStatusEnum` | `PENDING`, `PAID`, `CANCELED`, `FAILED` | Status do pedido de pagamento. |
| **TransactionStatus** | `TransactionStatus` | `ACTIVE`, `EXPIRED`, `CANCELLED`, `REFUNDED` | Ciclo de vida de uma transação. |

---

## Termos de Infraestrutura

| Termo | Classe/Config | Definição |
|-------|---------------|-----------|
| **Slug** | `Movie.slug` | URL amigável gerada automaticamente do título (`title.toLower().replaceAll("[^a-z0-9]+", "_")`). Deve ser único. |
| **WalletId** | `User.walletId` | ID da subconta do diretor no gateway Asaas. Obrigatório para publicar filmes. |
| **R2** | `R2Service` | Serviço de storage Cloudflare R2 para poster e banner. |
| **Webhook** | `WebhookController` / `WebhookService` | Notificação assíncrona do Asaas sobre mudança de status de pagamento. |
| **ProhibitedTerms** | `app.prohibited-terms` (env) | Lista CSV de termos que bloqueiam criação/edição de filmes. |
| **SoftDelete** | `Movie.active = false` | Filmes não são removidos fisicamente; `active = false` os oculta das listagens. |
