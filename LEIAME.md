# BancodeDadosJDBC

## Visão geral

Este repositório reúne estudos práticos de **JDBC (Java Database Connectivity)** — a API padrão do Java para conectar e interagir com bancos de dados relacionais (MySQL, neste caso). Ele segue uma progressão passo a passo: cada subpasta é um mini-projeto independente (com sua própria pasta `src/`) que evolui a partir do anterior, adicionando uma nova operação ou conceito de JDBC por vez.

Este repositório é um complemento natural do [`EstudosJava`](https://github.com/Menezesvm/EstudosJava) — enquanto aquele cobre Java puro e POO, este foca especificamente em **acesso a banco de dados com JDBC puro**, um caminho direto para o estudo de Spring Data JPA / Hibernate mais adiante.

## O que você vai encontrar

- **Gerenciamento de conexão com o banco**: abrir/fechar uma `Connection` de forma segura, carregando as credenciais de um arquivo externo `db.properties`
- **Exceções customizadas**: encapsular `SQLException` em uma exceção de runtime específica do domínio (`DbException`)
- **Leitura de dados**: `Statement` + `ResultSet` para executar consultas `SELECT` e percorrer as linhas retornadas
- **Inserção de dados**: `PreparedStatement` com `INSERT` parametrizado e recuperação de chaves geradas automaticamente
- **Atualização de dados**: `UPDATE` parametrizado e verificação de linhas afetadas
- **Exclusão de dados**: `DELETE` parametrizado e tratamento de erros de integridade referencial com uma exceção dedicada (`DbIntegrityException`)
- **Liberação de recursos**: fechamento consistente de `Connection`, `Statement` e `ResultSet` em blocos `finally`

## Estrutura da pasta

| Projeto | Conceito principal | Descrição |
|---|---|---|
| [`jdbc`](jdbc) | **Conexão** | O exemplo mais simples possível: abre uma `Connection` com o banco usando `DriverManager` e a fecha em seguida. Introduz `DB.getConnection()` / `DB.closeConnection()` e o encapsulamento `DbException`. |
| [`jdbc2`](jdbc2) | **SELECT** (leitura) | Usa um `Statement` para executar `SELECT * FROM department`, percorre o `ResultSet` com `while(rs.next())` e imprime cada linha. Adiciona os métodos auxiliares `DB.closedStatement()` / `DB.closedResultSet()` para liberação segura de recursos. |
| [`jdbc3`](jdbc3) | **INSERT** (criação) | Usa um `PreparedStatement` com `Statement.RETURN_GENERATED_KEYS` para inserir novas linhas em `department` e recuperar o `Id` gerado automaticamente via `getGeneratedKeys()`. Também contém um exemplo comentado inserindo em `seller` com placeholders `?` (nome, e-mail, data, salário, chave estrangeira). |
| [`jdbc4`](jdbc4) | **UPDATE** | Usa um `PreparedStatement` parametrizado para dar um reajuste salarial a todos os `seller` de um determinado departamento (`UPDATE seller SET BaseSalary = BaseSalary + ? WHERE DepartmentId = ?`) e verifica quantas linhas foram afetadas. |
| [`jdbc5`](jdbc5) | **DELETE** + erros de integridade | Usa um `PreparedStatement` parametrizado para excluir um `department` pelo `Id`. Introduz a `DbIntegrityException`, uma exceção customizada lançada quando a exclusão viola uma restrição de chave estrangeira (ex.: tentar excluir um departamento que ainda tem vendedores vinculados a ele). |
| [`jdbc6`](jdbc6) | **Transações** (`commit`/`rollback`) | Desativa o auto-commit (`conn.setAutoCommit(false)`) para executar dois comandos `UPDATE` como uma única transação atômica. Se ocorrer um erro entre eles, a transação é revertida (`conn.rollback()`), garantindo que nenhuma das duas atualizações seja persistida, e uma `DbException` é lançada; se ambas forem bem-sucedidas, a transação é confirmada (`conn.commit()`). |

## Progressão de aprendizado sugerida

1. **`jdbc`** — Entenda o básico absoluto: como uma conexão JDBC é aberta e fechada, e como as configurações de conexão são externalizadas em `db.properties` em vez de ficarem "hardcoded" no código.
2. **`jdbc2`** — Aprenda a *ler* dados: executar uma consulta e percorrer os resultados com `ResultSet`.
3. **`jdbc3`** — Aprenda a *criar* dados: inserir linhas com um `PreparedStatement` e capturar a chave primária gerada pelo banco.
4. **`jdbc4`** — Aprenda a *atualizar* dados: executar um `UPDATE` parametrizado e validar o número de linhas afetadas.
5. **`jdbc5`** — Aprenda a *excluir* dados com segurança: executar um `DELETE` parametrizado e tratar erros reais de restrição de integridade com um tipo de exceção dedicado.
6. **`jdbc6`** — Aprenda a agrupar várias operações em uma única **transação**: desativar o auto-commit, executar várias atualizações e confirmá-las todas juntas, ou reverter tudo caso algo falhe no meio do caminho — garantindo a consistência dos dados.

Essa progressão espelha o clássico ciclo CRUD (Create, Read, Update, Delete), com a configuração da conexão como etapa obrigatória inicial.

## Conceitos-chave abordados

| Conceito | Onde aparece |
|---|---|
| **`Connection`, `DriverManager`** | `jdbc` (e todos os projetos) |
| **Configuração externalizada (`db.properties`, `Properties`)** | `jdbc` (e todos os projetos) |
| **Encapsulamento de exceção checked em unchecked (`DbException`)** | `jdbc` (e todos os projetos) |
| **`Statement` + `ResultSet`** | `jdbc2` |
| **`PreparedStatement` (consultas parametrizadas, placeholders `?`)** | `jdbc3`, `jdbc4`, `jdbc5` |
| **Chaves geradas automaticamente (`RETURN_GENERATED_KEYS`, `getGeneratedKeys()`)** | `jdbc3` |
| **`executeUpdate()` e contagem de linhas afetadas** | `jdbc3`, `jdbc4`, `jdbc5` |
| **Tratamento de integridade referencial (`DbIntegrityException`)** | `jdbc5` |
| **Transações (`setAutoCommit`, `commit()`, `rollback()`)** | `jdbc6` |
| **Liberação de recursos em blocos `finally`** | `jdbc2`, `jdbc3`, `jdbc4`, `jdbc5`, `jdbc6` |

## Configuração do banco de dados

Cada projeto precisa de um arquivo `db.properties` local com os dados de conexão do seu banco (esse arquivo **não** é versionado no repositório — está listado no `.gitignore` — então você precisa criar sua própria cópia local):

```properties
user=root
password=sua_senha_aqui
dburl=jdbc:mysql://127.0.0.1:3306/bancoteste
useSSL=false
```

> ✅ **Nota de segurança:** o `db.properties` está corretamente excluído do controle de versão via `.gitignore` em cada pasta de projeto. Só mantenha esse mesmo padrão em qualquer subpasta nova que você adicionar — crie o `db.properties` local você mesmo, e nunca o remova do `.gitignore`.

## Como executar

Cada pasta de projeto contém um diretório `lib/` com o driver JDBC do MySQL (`mysql-connector-j`), já referenciado pelos arquivos de projeto do IntelliJ IDEA (`.iml`, `.idea/`).

1. Abra a subpasta desejada (ex.: `jdbc2`) como projeto no IntelliJ IDEA.
2. Garanta que o driver MySQL em `lib/` esteja adicionado ao classpath do módulo.
3. Atualize o `db.properties` com as credenciais do seu banco local.
4. Certifique-se de que o banco/tabelas referenciados (`bancoteste`, `department`, `seller`) existam — crie-os antes, se necessário.
5. Execute `src/application/Program.java`.

Ou compile/execute manualmente pelo terminal:

```bash
cd jdbc2
javac -cp "lib/*" -d bin src/application/*.java src/db/*.java
java -cp "bin:lib/*" application.Program
```

## Próximos passos

Depois de concluir este repositório, os próximos passos naturais em direção ao roadmap de Spring Boot são:

- Pool de conexões (HikariCP)
- JPA / Hibernate (mapear entidades em vez de escrever SQL puro)
- Flyway para migrações de banco de dados
- Padrão de projeto DAO (Data Access Object)
- Testes de integração com Testcontainers

---

**Repositório:** [Menezesvm/BancodeDadosJDBC](https://github.com/Menezesvm/BancodeDadosJDBC)
**Linguagem:** Java
**Tema:** JDBC / acesso a banco de dados relacional
