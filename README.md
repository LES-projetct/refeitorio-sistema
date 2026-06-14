# Sistema de Controle de Refeitório

Sistema web desenvolvido para controle de acesso, consumo, pagamentos e despesas em refeitórios, com identificação de usuários por RFID, controle de saldo, limite de crédito, histórico de compras, comprovantes e dashboard administrativo.

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
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge\&logo=github\&logoColor=white)

---

## Sobre o projeto

O Sistema de Controle de Refeitório foi desenvolvido para simular o funcionamento de um refeitório informatizado, permitindo o controle de usuários, produtos, compras, pagamentos, acessos e despesas.

O sistema utiliza identificação por RFID para localizar clientes, controla saldo e limite de crédito, registra compras por unidade ou por peso, gera comprovantes e apresenta indicadores administrativos no dashboard.

A aplicação possui controle de perfis de acesso, separando as funções disponíveis para administrador, operador e cliente.

---

## Funcionalidades principais

* Cadastro, edição, ativação e desativação de usuários.
* Geração automática de RFID para clientes.
* Criação automática de conta de acesso para cliente.
* Troca obrigatória de PIN no primeiro acesso.
* Cadastro e gerenciamento de produtos.
* Produtos vendidos por unidade ou por peso.
* Registro de compras.
* Cálculo automático do valor total da compra.
* Controle de saldo e limite de crédito.
* Bloqueio de compra quando o limite de crédito é ultrapassado.
* Geração de comprovante de compra.
* Histórico de compras.
* Consulta de detalhes e comprovantes.
* Registro de pagamentos e recargas de saldo.
* Histórico de pagamentos.
* Registro de entrada e saída no refeitório.
* Controle de acesso por RFID.
* Dashboard administrativo com indicadores gerais.
* Relatório de clientes devedores.
* Controle de despesas com fornecedores.
* Cálculo de despesas pagas, despesas pendentes e lucro estimado.
* Controle de permissões por perfil de acesso.

---

## Perfis de acesso

### Administrador

O administrador possui acesso completo ao sistema. Pode gerenciar usuários, produtos, compras, pagamentos, acessos, contas, relatórios, despesas e visualizar o dashboard administrativo.

### Operador

O operador possui acesso às funções operacionais do refeitório, como produtos, compras, nova compra, pagamentos e acessos.

### Cliente

O cliente possui acesso à área própria, onde pode consultar saldo, limite de crédito, histórico de compras, comprovantes, acessos e pagamentos.

---

## Simulação de periféricos

O sistema simula o uso de periféricos comuns em refeitórios.

### Leitor RFID

O leitor RFID é simulado por um campo de identificação do cliente. Ao informar o código RFID, o sistema localiza o usuário correspondente e verifica se ele está autorizado a realizar compras ou acessar o refeitório.

### Balança

A balança é simulada pelo campo de peso em quilogramas. Esse campo é usado apenas para produtos vendidos por peso, como refeição por quilo ou salada por quilo.

### Leitor de código de barras

O leitor de código de barras é representado pelo cadastro dos produtos com código próprio, permitindo a simulação da identificação dos itens vendidos.

---

## Regras de negócio

* Usuários inativos não podem realizar compras.
* Usuários ativos podem realizar compras enquanto estiverem dentro do limite de crédito.
* O sistema verifica saldo e limite antes de finalizar a compra.
* Produtos vendidos por unidade aceitam apenas quantidades inteiras.
* Produtos vendidos por peso aceitam valores decimais em quilogramas.
* O saldo do cliente é atualizado automaticamente após cada compra.
* Pagamentos aumentam o saldo disponível do cliente.
* O cliente deve trocar o PIN temporário no primeiro acesso.
* Despesas ativas entram nos cálculos financeiros.
* Despesas desativadas permanecem no histórico, mas não entram nos cálculos.
* O lucro estimado é calculado com base no faturamento total menos as despesas pagas.
* O acesso ao sistema é controlado conforme o perfil do usuário.

---

## Módulo financeiro

O sistema possui controle financeiro dividido em entradas e saídas.

### Entradas

As entradas são representadas pelas compras realizadas pelos clientes.

### Saídas

As saídas são representadas pelas despesas cadastradas com fornecedores.

### Indicadores financeiros

O dashboard apresenta:

* Faturamento total.
* Despesas pagas.
* Despesas pendentes.
* Lucro estimado.

A regra usada é:

```text
Lucro estimado = Faturamento total - Despesas pagas
```

---

## Principais telas

* Login.
* Dashboard administrativo.
* Usuários.
* Produtos.
* Compras.
* Nova compra.
* Comprovante de compra.
* Pagamentos.
* Acessos.
* Despesas.
* Relatórios.
* Contas do sistema.
* Minha conta.
* Minhas compras.
* Meus acessos.
* Meus pagamentos.

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

Após configurar as variáveis, feche e abra novamente a IDE ou o terminal.

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

## Estrutura geral do projeto

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

## Status do projeto

Projeto acadêmico desenvolvido para fins de demonstração de um sistema de controle de refeitório, com foco em regras de negócio, controle de acesso, integração simulada com periféricos, controle financeiro e gerenciamento de consumo.

---

## Autora

Desenvolvido por **Wanessa Laurindo Rodrigues**.
