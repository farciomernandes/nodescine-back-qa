## Story: <Título>

**Epic:** <link para o epic>  
**Módulo:** <módulo>  
**Actor:** <role>  
**Spec TOON:** `.github/specs/<módulo>/<feature>.toon.yaml`

### Descrição
Como `<ator>`, quero `<ação>` para `<benefício>`.

### Critérios de Aceitação
- [ ] CA-1: ...
- [ ] CA-2: ...

### DoR Checklist
- [ ] Spec TOON criada
- [ ] Vocabulário atualizado (se necessário)
- [ ] Impacto no SecurityConfig identificado
- [ ] Migration identificada (se DDL)

### DoD Checklist
- [ ] Todos os CAs implementados
- [ ] Controller sem lógica de negócio
- [ ] DTOs corretos (sem persistir DTO)
- [ ] Migration criada (se DDL)
- [ ] SecurityConfig atualizado (se necessário)
- [ ] Sem System.out.println
- [ ] Código compila

### Subtasks
- [ ] Criar migration SQL
- [ ] Criar/atualizar Entity
- [ ] Criar/atualizar Repository
- [ ] Criar DTOs
- [ ] Implementar Service
- [ ] Implementar Controller
- [ ] Atualizar SecurityConfig
