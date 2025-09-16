# Clínica ERP - Sistema de Agendamento de Consultas MVC

Este projeto é um **sistema ERP para clínicas** desenvolvido para a disciplina de Arquitetura MVC, focado em **agendamento de consultas médicas**.  
Ele foi desenvolvido utilizando **Spring Boot**, **Vaadin** para a interface e **PostgreSQL** como banco de dados.

---

## 🚀 Tecnologias Utilizadas
- [Spring Boot](https://spring.io/projects/spring-boot)  
- [Vaadin](https://vaadin.com/)  
- [PostgreSQL](https://www.postgresql.org/)  
- [Docker & Docker Compose](https://www.docker.com/)  
- [Maven](https://maven.apache.org/)  

---

## 📦 Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- [Docker](https://docs.docker.com/get-docker/)  
- [Docker Compose](https://docs.docker.com/compose/)  
- [Java 17+](https://adoptium.net/)  
- [Maven](https://maven.apache.org/) (ou use o `./mvnw` que já vem no projeto)

---

## ▶️ Como rodar o projeto

### 1. Subir o banco de dados PostgreSQL com Docker Compose
No terminal, execute:

```bash
docker-compose up -d
```

Isso irá iniciar o PostgreSQL conforme configurado no arquivo docker-compose.yml.

### 2. Compilar e executar o projeto Spring Boot

Você pode rodar direto via Maven:

```bash
./mvnw spring-boot:run
```

ou 

```bash
mvn spring-boot:run
```

Ou gerar o .jar e rodar manualmente:

```bash
./mvnw clean package # ou mvn clean package
java -jar target/clinica-mvc-1.0-SNAPSHOT.jar
```

### 3. Acessar a Aplicação
Abra o navegador em: http://localhost:8080

### 📖 Documentação útil

[Documentação oficial do Vaadin](https://vaadin.com/docs/latest/)
[Documentação do Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)
[Documentação PostgreSQL](https://www.postgresql.org/docs/)