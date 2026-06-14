# Sistema de Controle de Refeitório

Sistema web desenvolvido para controle de acesso, consumo e pagamento em refeitórios, com identificação de usuários por RFID, controle de saldo, registro de compras, gerenciamento de produtos e simulação de periféricos como leitor RFID e balança.

---

## Tecnologias utilizadas

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge\&logo=springboot\&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge\&logo=springsecurity\&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-59666C?style=for-the-badge\&logo=hibernate\&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge\&logo=thymeleaf\&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge\&logo=postgresql\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge\&logo=apachemaven\&logoColor=white)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge\&logo=html5\&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge\&logo=css\&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge\&logo=javascript\&logoColor=black)
![NetBeans](https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge\&logo=apachenetbeanside\&logoColor=white)

---

## Sobre o projeto

O sistema foi desenvolvido para simular o funcionamento de um refeitório informatizado, permitindo que usuários sejam identificados por RFID, realizem compras com base no saldo disponível e tenham seu consumo registrado.

A aplicação possui controle de perfis de acesso, separando as funcionalidades disponíveis para administrador, operador e cliente.

---

## Funcionalidades principais

* Cadastro, edição, ativação e desativação de usuários.
* Cadastro e gerenciamento de produtos.
* Produtos vendidos por unidade ou por peso.
* Identificação de clientes por RFID.
* Registro de compras.
* Cálculo automático do valor total da compra.
* Controle de saldo e limite de crédito.
* Bloqueio de operações quando o limite de crédito é ultrapassado.
* Geração de comprovante de compra.
* Histórico de compras.
* Registro de pagamentos e recargas de saldo.
* Histórico de pagamentos.
* Registro de entrada e saída no refeitório.
* Controle de acessos por RFID.
* Dashboard com informações gerais do sistema.
* Relatório de clientes devedores.
* Área do cliente para consulta de saldo, compras, acessos e pagamentos.
* Controle de login por perfil.
* Troca obrigatória de PIN no primeiro acesso do cliente.

---

## Perfis de acesso

### Administrador

O administrador possui acesso completo ao sistema, podendo gerenciar usuários, produtos, compras, pagamentos, acessos, contas e relatórios.

### Operador

O operador possui acesso às funções operacionais do refeitório, como registro de compras, produtos, pagamentos e acessos.

### Cliente

O cliente acessa sua própria área para consultar saldo, limite de crédito, histórico de compras, comprovantes, acessos e pagamentos.

---

## Simulação de periféricos

O sistema simula o uso de periféricos comuns em refeitórios:

### Leitor RFID

O leitor RFID é simulado por um campo de identificação do cliente. Ao informar o código RFID, o sistema localiza o usuário correspondente e verifica se ele pode realizar compras ou acessar o refeitório.

### Balança

A balança é simulada pelo campo de peso em quilogramas, utilizado apenas para produtos vendidos por peso, como refeição por quilo ou salada por quilo.

### Leitor de código de barras

O leitor de código de barras é representado pelo cadastro e seleção de produtos com código próprio, permitindo simular a identificação dos itens vendidos.

---

## Regras de negócio

* Usuários inativos não podem realizar compras.
* O sistema verifica o saldo e o limite de crédito antes de finalizar a compra.
* Produtos por unidade aceitam apenas quantidade inteira.
* Produtos por peso aceitam valores decimais em quilogramas.
* O saldo do cliente é atualizado automaticamente após cada compra.
* Pagamentos aumentam o saldo disponível do cliente.
* O cliente deve trocar o PIN temporário no primeiro acesso.
* O acesso ao sistema é controlado conforme o perfil do usuário.

---

## Como executar o projeto

### Pré-requisitos

Antes de executar o projeto, é necessário ter instalado:

* Java JDK 17 ou superior.
* PostgreSQL.
* Maven ou Maven Wrapper.
* NetBeans, IntelliJ IDEA, VS Code ou outra IDE compatível.

---

## Configuração do banco de dados

Crie um banco de dados PostgreSQL com o nome:

```sql
CREATE DATABASE refeitorio;
```

Depois configure o arquivo:

```text
src/main/resources/application.properties
```

Exemplo de configuração local:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/refeitorio
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

server.port=8080
```

---

## Conta administrativa inicial

O sistema pode criar uma conta administrativa inicial por meio de variáveis de ambiente.

Exemplo:

```text
APP_ADMIN_NOME=Administrador
APP_ADMIN_LOGIN=admin
APP_ADMIN_SENHA=1234
```

No Windows, é possível configurar pelo terminal:

```cmd
setx APP_ADMIN_NOME "Administrador"
setx APP_ADMIN_LOGIN "admin"
setx APP_ADMIN_SENHA "1234"
```

Após configurar, feche e abra novamente a IDE ou o terminal.

---

## Executando pela IDE

Abra o projeto no NetBeans ou em outra IDE compatível e execute a classe principal:

```text
RefeitorioApplication.java
```

Depois acesse no navegador:

```text
http://localhost:8080/login
```

---

## Executando pelo terminal

Na pasta do projeto, execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```cmd
mvnw.cmd spring-boot:run
```

Depois acesse:

```text
http://localhost:8080/login
```

---

## Estrutura geral do sistema

```text
src/main/java/com/wanessa/refeitorio
├── config
├── controller
├── dto
├── enums
├── model
├── repository
├── service
└── RefeitorioApplication.java
```

```text
src/main/resources
├── static
│   ├── css
│   └── js
├── templates
└── application.properties
```

---

## Principais telas

* Login.
* Dashboard.
* Usuários.
* Produtos.
* Compras.
* Nova compra.
* Comprovante de compra.
* Pagamentos.
* Acessos.
* Relatórios.
* Contas do sistema.
* Minha conta.
* Minhas compras.
* Meus acessos.
* Meus pagamentos.

---

## Status do projeto

Projeto acadêmico desenvolvido para fins de demonstração de um sistema de controle de refeitório, com foco em regras de negócio, controle de acesso, integração simulada com periféricos e gerenciamento de consumo.

---

## Autora

Desenvolvido por **Wanessa Laurindo Rodrigues**.

Curso Técnico em Edificações / Projeto acadêmico de Sistema de Controle de Refeitório.
