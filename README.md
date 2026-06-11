# Sistema de Controle de Refeitório

Sistema desenvolvido para controle de acesso, consumo e pagamento em refeitórios, com identificação de usuários por RFID.

## Funcionalidades

- Cadastro e edição de usuários
- Cadastro e gerenciamento de produtos
- Identificação de usuários por RFID
- Registro de compras
- Produtos vendidos por quantidade ou peso
- Controle de saldo e limite de crédito
- Bloqueio de usuários inadimplentes
- Registro de acessos
- Histórico de compras
- Dashboard com informações gerais

## Tecnologias

- Java
- Spring Boot
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- Maven
- HTML
- CSS
- JavaScript

## Como executar

1. Criar o banco de dados PostgreSQL chamado `refeitorio`.
2. Configurar o arquivo `application.properties`.
3. Executar o projeto pelo NetBeans ou pelo comando:

```bash
./mvnw spring-boot:run
