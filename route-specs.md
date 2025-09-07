# 📌 API Spec (Draft)

## 🔑 Auth

| Método   | Rota                    | Body                                                                         | Descrição                                      |
| -------- | ----------------------- | ---------------------------------------------------------------------------- | ---------------------------------------------- |
| **POST** | `/auth/login`           | `{ email: string, password: string }`                                        | Login do usuário. Retorna `token` + `user`.    |
| **POST** | `/auth/register`        | `{ name: string, email: string, password: string, confirmPassword: string }` | Registro de usuário. Retorna `token` + `user`. |
| **POST** | `/auth/change-password` | `{ oldPassword: string, newPassword: string, confirmNewPassword: string }`   | Alterar senha do usuário logado.               |
| **GET**  | `/auth/me`              | Header `Authorization: Bearer token`                                         | Retorna dados do usuário autenticado.          |

---

## 👤 Users

| Método     | Rota         | Body                                                                                | Descrição                                    |
| ---------- | ------------ | ----------------------------------------------------------------------------------- | -------------------------------------------- |
| **GET**    | `/users/:id` | —                                                                                   | Retorna dados de um usuário específico.      |
| **PUT**    | `/users/:id` | `{ name?: string, email?: string, avatar?: string, role?: Roles, status?: Status }` | Atualiza dados do usuário (admin/moderator). |
| **DELETE** | `/users/:id` | —                                                                                   | Deleta um usuário.                           |

---

## 🎬 Enhanced Films

| Método     | Rota                  | Body                    | Descrição                     |
| ---------- | --------------------- | ----------------------- | ----------------------------- |
| **GET**    | `/enhanced-films`     | —                       | Lista todos os filmes.        |
| **GET**    | `/enhanced-films/:id` | —                       | Retorna detalhes de um filme. |
| **POST**   | `/enhanced-films`     | `EnhancedFilm`          | Cria um novo filme.           |
| **PUT**    | `/enhanced-films/:id` | `Partial<EnhancedFilm>` | Atualiza um filme.            |
| **DELETE** | `/enhanced-films/:id` | —                       | Remove um filme.              |

---

## 💳 Transactions

| Método   | Rota                             | Body                                 | Descrição                                       |
| -------- | -------------------------------- | ------------------------------------ | ----------------------------------------------- |
| **GET**  | `/transactions`                  | —                                    | Lista todas as transações do usuário logado.    |
| **GET**  | `/transactions/:id`              | —                                    | Retorna detalhes de uma transação.              |
| **POST** | `/transactions`                  | `{ filmId: string, amount: string }` | Cria uma nova transação (ex: aluguel de filme). |
| **GET**  | `/transactions/:id/receipt`      | —                                    | Retorna um **PDF** do recibo da transação.      |
| **POST** | `/transactions/:id/send-receipt` | `{ email: string }`                  | Envia o recibo para o e-mail do usuário.        |

---

## 🔧 Extras úteis

* **GET `/categories`** → retorna lista de categorias com `filmCount`.
* **GET `/rented-films`** → retorna os filmes alugados (`RentedFilm[]`).
* **POST `/logout`** → invalida o token atual.

---

### 📄 Exemplo de Resposta do Login

```json
{
  "token": "jwt-token-123",
  "user": {
    "id": "uuid",
    "name": "Mocked User",
    "email": "user@email.com",
    "avatar": "https://i.pravatar.cc/150?u=user@email.com",
    "status": "active",
    "createdAt": "2025-08-31T10:00:00Z",
    "role": "user"
  }
}
```
