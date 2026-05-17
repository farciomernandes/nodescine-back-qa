# Variáveis de Ambiente — Banco de Dados (QA / Homolog)

> Banco: **nordestine-db-2**  
> Provider: Render — PostgreSQL 18  
> Região: Ohio (US East)  
> Ambiente: **Homologação (QA)**  
> ⚠️ Expira em: **16 de junho de 2026** (plano Free — fazer upgrade para manter)

---

## Conexão (External)

| Variável | Valor |
|----------|-------|
| `DB_HOST` | `dpg-d84qdemq1p3s73e88820-a.ohio-postgres.render.com` |
| `DB_PORT` | `5432` |
| `DB_NAME` | `nordestine_db_2` |
| `DB_USERNAME` | `nordestine_db_2_user` |
| `DB_PASSWORD` | `TpODX74439DYrCOAUT2X4zEBqWt6FVcS` |

---

## JDBC URL (Spring Boot)

```
jdbc:postgresql://dpg-d84qdemq1p3s73e88820-a.ohio-postgres.render.com:5432/nordestine_db_2
```

---

## Connection String Completa

```
postgresql://nordestine_db_2_user:TpODX74439DYrCOAUT2X4zEBqWt6FVcS@dpg-d84qdemq1p3s73e88820-a.ohio-postgres.render.com/nordestine_db_2
```

---

## Envs para `.env` / Deploy

```env
DB_HOST=dpg-d84qdemq1p3s73e88820-a.ohio-postgres.render.com
DB_PORT=5432
DB_NAME=nordestine_db_2
DB_USERNAME=nordestine_db_2_user
DB_PASSWORD=TpODX74439DYrCOAUT2X4zEBqWt6FVcS

SPRING_JPA_HIBERNATE_DDL_AUTO=validate
SPRING_JPA_SHOW_SQL=false
SPRING_FLYWAY_ENABLED=true
```

---

## Comando PSQL (acesso direto)

```bash
PGPASSWORD=TpODX74439DYrCOAUT2X4zEBqWt6FVcS psql \
  -h dpg-d84qdemq1p3s73e88820-a.ohio-postgres.render.com \
  -U nordestine_db_2_user \
  nordestine_db_2
```

---

## Informações do Serviço Render

| Campo | Valor |
|-------|-------|
| Service ID | `dpg-d84qdemq1p3s73e88820-a` |
| PostgreSQL Version | 18 |
| Região | Ohio (US East) |
| RAM | 256 MB |
| CPU | 0.1 vCPU |
| Storage | 1 GB |
| Plano | Free |

---

> 🔒 **Atenção:** Não commitar este arquivo em repositórios públicos com credenciais reais.  
> Considere usar `.gitignore` ou um vault de secrets em produção.
