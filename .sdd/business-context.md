# business-context.md — CineSK

## Overview
CineSK é uma plataforma REST API para streaming e venda de filmes independentes brasileiros. Diretores de cinema independente publicam seus filmes, clientes compram ou alugam, e moderadores garantem a qualidade e conformidade do conteúdo.

---

## Atores / Personas

| Ator | Role JWT | Operações principais |
|------|----------|----------------------|
| **Customer** | `CUSTOMER` | Comprar/alugar filmes, visualizar transações próprias, alterar senha |
| **Director** | `MOVIE_DIRECTOR` | Publicar filmes, fazer upload de mídia, ver receitas, registrar moderadores |
| **Moderator** | `MODERATOR` | Ver todos os usuários, acessar resumo de vendas de qualquer diretor, moderar filmes |
| **Público** | sem token | Listar/buscar filmes, ver detalhes, denunciar conteúdo |

---

## Fluxos de Negócio Principais

### 1. Publicação de Filme
```
Director autenticado
  → POST /enhanced-films (multipart: dto + poster + banner)
  → MovieService.create()
     ├── Verifica walletId configurado
     ├── Bloqueia isAdultConfirmed = true
     ├── Verifica termos proibidos (título + sinopse)
     ├── Garante ao menos 1 gênero
     ├── Gera slug único a partir do título
     ├── ModerationStatus = UNDER_REVIEW (padrão ao criar)
     └── Salva no banco + R2 (poster/banner)
```

### 2. Compra/Pagamento
```
Customer autenticado
  → POST /transactions { movieId, payment: { method, ... } }
  → TransactionService.create()
     ├── Chama gateway Asaas (PIX / Boleto / Cartão)
     └── Retorna TransactionResponse com QR Code (PIX) ou link (Boleto)

Asaas notifica via webhook
  → POST /asaas/webhook { event, payment.id }
  → WebhookService.processWebhookPayloadAsync()
     └── Atualiza status da Transaction
```

### 3. Moderação de Conteúdo
```
Público
  → POST /enhanced-films/{id}/report { reporterEmail, reason }
  → MovieService.reportMovie()
     └── Salva MovieReport no banco

Moderator
  → PUT /enhanced-films/{id} { moderationStatus: "REMOVED" }
     └── Atualiza ModerationStatus do filme
```

---

## Regras de Negócio Críticas

| # | Regra | Onde é aplicada |
|---|-------|-----------------|
| R1 | `isAdultConfirmed = true` é proibido | `MovieService.create()` e `update()` |
| R2 | Diretor precisa de `walletId` para publicar | `MovieService.create()` |
| R3 | Slug é gerado automaticamente e deve ser único | `MovieService.create()` e `update()` |
| R4 | Filme novo entra como `UNDER_REVIEW` | `MovieService.create()` |
| R5 | Termos proibidos bloqueiam título e sinopse | `MovieService` via `app.prohibited-terms` |
| R6 | Soft delete — `active = false` | `MovieService.delete()` |
| R7 | Apenas filmes com `active = true` aparecem nas listagens | `MovieRepository` |
