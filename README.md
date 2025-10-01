# 🎬 Nordescine - Sistema de Cinema

Sistema backend para gerenciamento de cinema com funcionalidades de autenticação, catálogo de filmes, transações e integração com múltiplos gateways de pagamento.

## 📋 Funcionalidades

- **🔐 Autenticação JWT**: Sistema completo de login/registro com tokens JWT
- **🎥 Catálogo de Filmes**: Gerenciamento de filmes, categorias e gêneros
- **💳 Pagamentos**: Integração com múltiplos gateways (Asaas, PagSeguro, Stripe)
- **☁️ Storage**: Upload de arquivos para AWS S3
- **👥 Gestão de Usuários**: Controle de perfis e permissões
- **🔄 Transações**: Sistema completo de aluguel e compra de filmes

## 🛠️ Tecnologias

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **PostgreSQL**
- **OpenFeign** (Integração com APIs externas)
- **AWS S3** (Storage de arquivos)
- **Maven** (Gerenciamento de dependências)
- **Docker** (Containerização)

## 🚀 Como Executar

### Pré-requisitos

- **Java 17+**
- **Maven 3.6+**
- **PostgreSQL** (para produção)
- **Docker** (opcional)

### 1. Configuração do Ambiente

1. **Clone o repositório:**
```bash
git clone <repository-url>
cd cine-sk
```

2. **Configure as variáveis de ambiente:**
```bash
# Copie o arquivo de exemplo
cp .env.example .env

# Edite o arquivo .env com suas configurações
nano .env
```

3. **Carregue as variáveis de ambiente:**
```bash
export $(cat .env | xargs)
```

### 2. Executando com Maven

```bash
# Instalar dependências
./mvnw clean install

# Executar a aplicação
./mvnw spring-boot:run
```

### 3. Executando com Docker

```bash
# Build da imagem
docker build -t cine-sk .

# Executar container
docker-compose up -d
```

### 4. Verificação

A API estará disponível em: `http://localhost:8080/api`

- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`

## ⚙️ Configuração

### Variáveis de Ambiente Obrigatórias

#### Banco de Dados (Produção)
```bash
DATABASE_HOST=jdbc:postgresql://localhost:5432/cine_sk
DATABASE_USER=your_user
DATABASE_PASS=your_password
```

#### JWT
```bash
JWT_SECRET=your_jwt_secret_base64
SPRING_JWT_SECRET_KEY=your_spring_jwt_secret
SPRING_JWT_EXPIRATION=86400000
```

#### AWS S3
```bash
AWS_S3_BUCKET=your-bucket
AWS_S3_REGION=us-east-1
AWS_S3_ACCESS_KEY=your_access_key
AWS_S3_SECRET_KEY=your_secret_key
```

#### APIs de Pagamento
```bash
ASAAS_API_URL=https://sandbox.asaas.com/api/v3
ASAAS_API_KEY=your_api_key

# Opcionais: PagSeguro, Stripe
PAGSEGURO_API_URL=https://sandbox.api.pagseguro.com
STRIPE_API_KEY=your_stripe_key
```

### Como usar:

1. **Para desenvolvimento local:**
```bash
cp .env.example .env
# Editar com suas configurações de desenvolvimento
export $(cat .env | xargs)
./mvnw spring-boot:run
```

2. **Para produção:**
```bash
cp .env.example .env
# Configurar todas as variáveis de produção
export $(cat .env | xargs)
./mvnw spring-boot:run
```

O `application.properties` busca todas as configurações das variáveis de ambiente definidas no `.env`.

## 📚 Documentação da API

### Endpoints Principais

#### Autenticação
- `POST /api/auth/register` - Registrar usuário
- `POST /api/auth/login` - Login
- `POST /api/auth/refresh` - Renovar token

#### Filmes
- `GET /api/films` - Listar filmes
- `GET /api/films/{id}` - Buscar filme
- `POST /api/films` - Criar filme (Admin)
- `PUT /api/films/{id}` - Atualizar filme (Admin)

#### Usuários
- `GET /api/users/profile` - Perfil do usuário
- `PUT /api/users/profile` - Atualizar perfil
- `GET /api/users/{id}/rentals` - Aluguéis do usuário

#### Transações
- `POST /api/transactions/rental` - Alugar filme
- `POST /api/transactions/purchase` - Comprar filme
- `GET /api/transactions/history` - Histórico

### Swagger Documentation

Acesse `http://localhost:8080/api/swagger-ui.html` para documentação interativa completa.

## 🗂️ Estrutura do Projeto

```
src/main/java/com/cine/sk/Nordescine/
├── controller/          # Controllers REST
├── domain/             # Entidades e lógica de negócio
│   ├── auth/          # Autenticação e autorização
│   ├── file/          # Gerenciamento de arquivos
│   ├── movie/         # Filmes e categorias
│   ├── transaction/   # Transações e pagamentos
│   └── user/          # Usuários
├── infrastructure/    # Configurações e infraestrutura
│   ├── cors/         # Configuração CORS
│   ├── jwt/          # Configuração JWT
│   ├── security/     # Spring Security
│   └── swagger/      # Documentação API
└── util/             # Utilitários
```

## 🔧 Desenvolvimento

### Adicionando Novos Gateways de Pagamento

1. Criar client no package `domain.transaction.payment.client`
2. Implementar service correspondente
3. Adicionar configurações no `.env`
4. Configurar `@FeignClient` com URL da variável de ambiente

### Exemplo:
```java
@FeignClient(name = "new-payment", url = "${NEW_PAYMENT_API_URL}")
public interface NewPaymentClient {
    // métodos da API
}
```

### Testes

```bash
# Executar todos os testes
./mvnw test

# Executar testes específicos
./mvnw test -Dtest=AuthControllerTest
```

## 🐳 Docker

### Docker Compose Completo

```yaml
version: '3.8'
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DATABASE_HOST=jdbc:postgresql://db:5432/cine_sk
      - DATABASE_USER=postgres
      - DATABASE_PASS=postgres
    depends_on:
      - db
  
  db:
    image: postgres:15
    environment:
      POSTGRES_DB: cine_sk
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 📝 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

## 📞 Suporte

Para suporte e dúvidas:
- Abra uma issue no GitHub
- Entre em contato: [seu-email@example.com]

---

**Desenvolvido com ❤️ para a comunidade de cinema digital**