# Cinesk Movie API - Postman Collection

Esta collection contém todos os endpoints disponíveis na API de filmes do Cinesk, com exemplos de dados de teste.

## Como importar no Postman

1. Abra o Postman
2. Clique em "Import" no canto superior esquerdo
3. Selecione o arquivo `Cinesk_Movie_API_Collection.json`
4. A collection será importada com todas as requisições organizadas

## Configuração de Variáveis

A collection utiliza as seguintes variáveis de ambiente:

- `base_url`: URL base da API (padrão: `http://localhost:8080`)
- `movie_id`: UUID de um filme para testes (você pode atualizar com um ID real após criar um filme)

### Como configurar as variáveis:

1. No Postman, clique no ícone de "olho" no canto superior direito
2. Clique em "Add" ao lado de "Environments"
3. Crie um novo environment chamado "Cinesk Local"
4. Adicione as variáveis mencionadas acima

## Estrutura da Collection

### 1. CRUD Operations
- **Create Movie**: Criar um novo filme
- **Update Movie**: Atualizar um filme existente
- **Get Movie by ID**: Buscar detalhes de um filme
- **Delete Movie**: Deletar um filme

### 2. Search & Filter
- **Get Paginated Films**: Lista paginada com ordenação
- **Search Films**: Busca por título ou diretor
- **Filter Films**: Filtros por gênero, ano e categoria

### 3. Special Collections
- **Get Featured Films**: Filmes em destaque
- **Get New Releases**: Novos lançamentos
- **Get Popular Films**: Filmes populares
- **Get All Categories**: Lista de categorias

## Dados de Teste

Os exemplos incluem:

### Filme de Exemplo (Create/Update):
