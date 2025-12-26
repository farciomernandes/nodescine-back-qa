# Requests cURL para API

Abaixo estão exemplos de requisições cURL para autenticação e criação de filmes nacionais.
**Nota:** Todos os endpoints agora incluem o prefixo `/api`.

## 0. Login (Autenticação)
Antes de criar filmes, você precisa de um token de acesso.
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "moderator_final@teste.com",
    "password": ""
  }'
```
*Copie o token retornado e substitua `<TOKEN>` nas requisições abaixo.*

---

# 10 Requests cURL para Filmes Nacionais

As requisições utilizam `multipart/form-data` e exigem autenticação via Bearer Token.

## 1. Cidade de Deus
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Cidade de Deus\",
  \"director\": \"Fernando Meirelles, Kátia Lund\",
  \"year\": 2002,
  \"category\": \"Drama\",
  \"price\": 10.00,
  \"genres\": [{\"name\": \"Crime\"}, {\"name\": \"Drama\"}],
  \"duration\": \"130 min\",
  \"synopsis\": \"Buscapé é um jovem fotógrafo que registra o cotidiano violento da Cidade de Deus.\",
  \"cast\": [\"Alexandre Rodrigues\", \"Leandro Firmino\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": true,
  \"slug\": \"cidade-de-deus\"
};type=application/json"
```

## 2. Tropa de Elite
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Tropa de Elite\",
  \"director\": \"José Padilha\",
  \"year\": 2007,
  \"category\": \"Ação\",
  \"price\": 12.00,
  \"genres\": [{\"name\": \"Ação\"}, {\"name\": \"Policial\"}],
  \"duration\": \"115 min\",
  \"synopsis\": \"O Capitão Nascimento busca um substituto para seu posto enquanto combate o tráfico no Rio de Janeiro.\",
  \"cast\": [\"Wagner Moura\", \"André Ramiro\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": true,
  \"slug\": \"tropa-de-elite\"
};type=application/json"
```

## 3. Central do Brasil
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Central do Brasil\",
  \"director\": \"Walter Salles\",
  \"year\": 1998,
  \"category\": \"Drama\",
  \"price\": 15.00,
  \"genres\": [{\"name\": \"Drama\"}],
  \"duration\": \"110 min\",
  \"synopsis\": \"Uma ex-professora ajuda um menino a encontrar seu pai no interior do Nordeste.\",
  \"cast\": [\"Fernanda Montenegro\", \"Vinícius de Oliveira\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": false,
  \"slug\": \"central-do-brasil\"
};type=application/json"
```

## 4. O Auto da Compadecida
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"O Auto da Compadecida\",
  \"director\": \"Guel Arraes\",
  \"year\": 2000,
  \"category\": \"Comédia\",
  \"price\": 8.00,
  \"genres\": [{\"name\": \"Comédia\"}, {\"name\": \"Aventura\"}],
  \"duration\": \"104 min\",
  \"synopsis\": \"As aventuras de João Grilo e Chicó no sertão da Paraíba.\",
  \"cast\": [\"Matheus Nachtergaele\", \"Selton Mello\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": false,
  \"slug\": \"o-auto-da-compadecida\"
};type=application/json"
```

## 5. Bacurau
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Bacurau\",
  \"director\": \"Kleber Mendonça Filho, Juliano Dornelles\",
  \"year\": 2019,
  \"category\": \"Thriller\",
  \"price\": 20.00,
  \"genres\": [{\"name\": \"Mistério\"}, {\"name\": \"Thriller\"}],
  \"duration\": \"131 min\",
  \"synopsis\": \"Os moradores de um pequeno povoado no sertão brasileiro descobrem que a comunidade não consta mais nos mapas.\",
  \"cast\": [\"Barbara Colen\", \"Thomas Aquino\", \"Sonia Braga\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": true,
  \"slug\": \"bacurau\"
};type=application/json"
```

## 6. Que Horas Ela Volta?
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Que Horas Ela Volta?\",
  \"director\": \"Anna Muylaert\",
  \"year\": 2015,
  \"category\": \"Drama\",
  \"price\": 10.00,
  \"genres\": [{\"name\": \"Drama\"}],
  \"duration\": \"112 min\",
  \"synopsis\": \"A chegada da filha de uma empregada doméstica desestabiliza a rotina da casa onde ela trabalha.\",
  \"cast\": [\"Regina Casé\", \"Camila Márdila\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": false,
  \"slug\": \"que-horas-ela-volta\"
};type=application/json"
```

## 7. Carandiru
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Carandiru\",
  \"director\": \"Hector Babenco\",
  \"year\": 2003,
  \"category\": \"Crime\",
  \"price\": 10.00,
  \"genres\": [{\"name\": \"Crime\"}, {\"name\": \"Drama\"}],
  \"duration\": \"145 min\",
  \"synopsis\": \"O cotidiano da casa de detenção de São Paulo antes do massacre de 1992.\",
  \"cast\": [\"Luiz Carlos Vasconcelos\", \"Milton Gonçalves\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": true,
  \"slug\": \"carandiru\"
};type=application/json"
```

## 8. Minha Mãe é uma Peça
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Minha Mãe é uma Peça\",
  \"director\": \"André Pellenz\",
  \"year\": 2013,
  \"category\": \"Comédia\",
  \"price\": 5.00,
  \"genres\": [{\"name\": \"Comédia\"}],
  \"duration\": \"84 min\",
  \"synopsis\": \"Dona Hermínia é uma mãe superprotetora que não larga do pé dos filhos.\",
  \"cast\": [\"Paulo Gustavo\", \"Mariana Xavier\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": false,
  \"slug\": \"minha-mae-e-uma-peca\"
};type=application/json"
```

## 9. O Palhaço
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"O Palhaço\",
  \"director\": \"Selton Mello\",
  \"year\": 2011,
  \"category\": \"Comédia\",
  \"price\": 10.00,
  \"genres\": [{\"name\": \"Comédia\"}, {\"name\": \"Drama\"}],
  \"duration\": \"88 min\",
  \"synopsis\": \"Benjamin é um palhaço que vive uma crise de identidade e decide cair na estrada.\",
  \"cast\": [\"Selton Mello\", \"Paulo José\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": false,
  \"slug\": \"o-palhaco\"
};type=application/json"
```

## 10. Aquarius
```bash
curl -X POST http://localhost:8080/api/enhanced-films \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: multipart/form-data" \
  -F "dto={
  \"title\": \"Aquarius\",
  \"director\": \"Kleber Mendonça Filho\",
  \"year\": 2016,
  \"category\": \"Drama\",
  \"price\": 15.00,
  \"genres\": [{\"name\": \"Drama\"}],
  \"duration\": \"146 min\",
  \"synopsis\": \"Clara, uma jornalista aposentada, luta contra uma construtora que quer demolir seu prédio.\",
  \"cast\": [\"Sonia Braga\", \"Maeve Jinkings\"],
  \"movieType\": \"YOUTUBE\",
  \"isAdultConfirmed\": true,
  \"slug\": \"aquarius\"
};type=application/json"
```
