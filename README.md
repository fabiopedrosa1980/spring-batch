Sprint Batch

## ▶️ Como executar

### Pré-requisitos

- Java 17 ou superior instalado
- Docker e Docker Compose instalados

### 1. Suba a infraestrutura (PostgreSQL)

```bash
docker compose up -d
```

Isso inicia o banco de dados PostgreSQL, usado pelo Spring Batch para armazenar o **metadata** de execução (histórico de Jobs, status de Steps, parâmetros de execução, etc.).

### 2. Rode a aplicação

```bash
# Linux/Mac
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

## 📚 Conceitos do Spring Batch explorados aqui

- **Job** — a unidade de trabalho completa, representando o processo batch a ser executado.
- **Step** — cada etapa independente dentro de um Job, podendo ser encadeadas sequencialmente ou com fluxos condicionais.
- **Chunk** — estratégia de processamento em blocos (leitura → processamento → escrita), controlando uso de memória e permitindo restart em caso de falha.
- **JobRepository** — persistência do histórico de execuções no PostgreSQL, garantindo rastreabilidade e possibilidade de retomar Jobs que falharam.

## 🗺️ Roadmap

- [ ] Job de leitura de arquivo (CSV) e gravação em banco
- [ ] Processamento com regras de negócio (`ItemProcessor`)
- [ ] Tratamento de erros e skip/retry policies
- [ ] Testes automatizados com `spring-batch-test`
- [ ] Agendamento de execução (`@Scheduled` ou trigger externo)

## 📝 Sobre

Este é um projeto de estudos, em evolução constante. Contribuições, sugestões e trocas de experiência sobre Spring Batch são muito bem-vindas!
