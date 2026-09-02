# BancodeDadosJDBC

## Overview

This repository gathers hands-on studies of **JDBC (Java Database Connectivity)** — the standard Java API for connecting to and interacting with relational databases (MySQL, in this case). It follows a step-by-step progression: each subfolder is an independent mini-project (with its own `src/` folder) that builds on the previous one, adding one new JDBC operation or concept at a time.

This repository is a natural companion to [`EstudosJava`](https://github.com/Menezesvm/EstudosJava) — while that repo covers core Java and OOP, this one focuses specifically on **database access with pure JDBC**, a direct on-ramp to Spring Data JPA / Hibernate later on.

## What You'll Find

- **Database connection management**: opening/closing a `Connection` safely, loading credentials from an external `db.properties` file
- **Custom exceptions**: wrapping `SQLException` in a domain-specific runtime exception (`DbException`)
- **Reading data**: `Statement` + `ResultSet` to run `SELECT` queries and iterate over rows
- **Inserting data**: `PreparedStatement` with parameterized `INSERT` queries and retrieving auto-generated keys
- **Updating data**: parameterized `UPDATE` queries and checking affected rows
- **Deleting data**: parameterized `DELETE` queries and handling referential-integrity errors with a dedicated exception (`DbIntegrityException`)
- **Resource cleanup**: consistently closing `Connection`, `Statement`, and `ResultSet` in `finally` blocks

## Folder Structure

| Project | Main concept | Description |
|---|---|---|
| [`jdbc`](jdbc) | **Connection** | The simplest possible example: opens a database `Connection` using `DriverManager` and closes it right away. Introduces `DB.getConnection()` / `DB.closeConnection()` and the `DbException` wrapper. |
| [`jdbc2`](jdbc2) | **SELECT** (read) | Uses a `Statement` to run `SELECT * FROM department`, iterates the `ResultSet` with `while(rs.next())`, and prints each row. Adds `DB.closedStatement()` / `DB.closedResultSet()` helpers for safe cleanup. |
| [`jdbc3`](jdbc3) | **INSERT** (create) | Uses a `PreparedStatement` with `Statement.RETURN_GENERATED_KEYS` to insert new rows into `department` and retrieve the auto-generated `Id` via `getGeneratedKeys()`. Also contains a commented-out example inserting into `seller` with `?` placeholders (string, email, date, salary, foreign key). |
| [`jdbc4`](jdbc4) | **UPDATE** | Uses a parameterized `PreparedStatement` to give every `seller` in a given department a salary raise (`UPDATE seller SET BaseSalary = BaseSalary + ? WHERE DepartmentId = ?`), then checks how many rows were affected. |
| [`jdbc5`](jdbc5) | **DELETE** + integrity errors | Uses a parameterized `PreparedStatement` to delete a `department` by `Id`. Introduces `DbIntegrityException`, a custom exception thrown when the deletion violates a foreign-key constraint (e.g., trying to delete a department that still has sellers linked to it). |

## Suggested Learning Progression

1. **`jdbc`** — Understand the absolute basics: how a JDBC connection is opened and closed, and how connection settings are externalized into `db.properties` instead of being hardcoded.
2. **`jdbc2`** — Learn to *read* data: run a query and iterate through the results with `ResultSet`.
3. **`jdbc3`** — Learn to *create* data: insert rows with a `PreparedStatement` and capture the database-generated primary key.
4. **`jdbc4`** — Learn to *update* data: run a parameterized `UPDATE` and validate the number of affected rows.
5. **`jdbc5`** — Learn to *delete* data safely: run a parameterized `DELETE` and handle real-world integrity constraint errors with a dedicated exception type.

This mirrors the classic CRUD cycle (Create, Read, Update, Delete), with the connection setup as the mandatory first step.

## Key Concepts Covered

| Concept | Where it appears |
|---|---|
| **`Connection`, `DriverManager`** | `jdbc` (and every project) |
| **Externalized configuration (`db.properties`, `Properties`)** | `jdbc` (and every project) |
| **Custom checked-to-unchecked exception wrapping (`DbException`)** | `jdbc` (and every project) |
| **`Statement` + `ResultSet`** | `jdbc2` |
| **`PreparedStatement` (parameterized queries, `?` placeholders)** | `jdbc3`, `jdbc4`, `jdbc5` |
| **Auto-generated keys (`RETURN_GENERATED_KEYS`, `getGeneratedKeys()`)** | `jdbc3` |
| **`executeUpdate()` and affected-row counting** | `jdbc3`, `jdbc4`, `jdbc5` |
| **Referential integrity handling (`DbIntegrityException`)** | `jdbc5` |
| **Resource cleanup in `finally` blocks** | `jdbc2`, `jdbc3`, `jdbc4`, `jdbc5` |

## Database Setup

Each project expects a `db.properties` file (already present in each folder) with your database connection details:

```properties
user=root
password=your_password_here
dburl=jdbc:mysql://127.0.0.1:3306/bancoteste
useSSL=false
```
## How to Run

Each project folder contains a `lib/` directory with the MySQL JDBC driver (`mysql-connector-j`), already referenced by the IntelliJ IDEA project files (`.iml`, `.idea/`).

1. Open the desired subfolder (e.g., `jdbc2`) as a project in IntelliJ IDEA.
2. Make sure the MySQL driver in `lib/` is added to the module's classpath.
3. Update `db.properties` with your own local database credentials.
4. Make sure the referenced database/tables (`bancoteste`, `department`, `seller`) exist — create them beforehand if needed.
5. Run `src/application/Program.java`.

Or compile/run manually from the terminal:

```bash
cd jdbc2
javac -cp "lib/*" -d bin src/application/*.java src/db/*.java
java -cp "bin:lib/*" application.Program
```

## Next Steps

After completing this repository, natural next steps toward the Spring Boot roadmap include:

- Connection pooling (HikariCP)
- JPA / Hibernate (mapping entities instead of writing raw SQL)
- Flyway for database migrations
- Transactions (`commit`/`rollback`) with JDBC
- DAO (Data Access Object) design pattern
- Integration testing with Testcontainers

---

**Repository:** [Menezesvm/BancodeDadosJDBC](https://github.com/Menezesvm/BancodeDadosJDBC)
**Language:** Java
**Topic:** JDBC / relational database access
# BancodeDadosJDBC
