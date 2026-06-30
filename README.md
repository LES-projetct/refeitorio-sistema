# RefeCard — Sistema de Controle de Refeitório

Sistema web desenvolvido para controle de acesso, consumo, saldo, pagamentos e relatórios em refeitórios institucionais.

Projeto desenvolvido para a disciplina de **Laboratório de Engenharia de Software**, no curso de **Sistemas de Informação — IFES Campus Colatina**.

## Problema

Em muitos refeitórios, o controle de acesso, consumo e pagamento ainda é feito de forma manual ou pouco integrada. Isso pode gerar demora no atendimento, erros no registro de compras, dificuldade no controle de saldo dos usuários e falta de histórico confiável para acompanhamento administrativo.

## Objetivo

Desenvolver um sistema web capaz de auxiliar o gerenciamento de refeitórios institucionais, permitindo:

* Identificação de clientes;
* Controle de acesso;
* Registro de compras;
* Controle de saldo e limite de crédito;
* Registro de pagamentos;
* Consulta de relatórios;
* Simulação de periféricos utilizados em um ambiente real.

## Solução Proposta

O **RefeCard** centraliza o controle do refeitório em uma única plataforma. O sistema permite que clientes sejam identificados por QR Code ou RFID, que produtos sejam selecionados manualmente ou por código de barras, e que produtos vendidos por peso utilizem uma balança simulada.

Além disso, o sistema registra entradas e saídas por meio de uma catraca simulada, controla saldo e limite de crédito dos usuários e gera relatórios administrativos.

## Tecnologias Utilizadas

<div align="center">

### Backend

<img src="https://skillicons.dev/icons?i=java,spring,maven" />

<br><br>

<img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/Spring%20Data%20JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white">

</div>

### Frontend

<div align="center">

<img src="https://skillicons.dev/icons?i=html,css,js" />

<br><br>

<img src="https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white">

</div>

### Banco de Dados

<div align="center">

<img src="https://skillicons.dev/icons?i=postgres" />

<br><br>

<img src="https://img.shields.io/badge/PostgreSQL-336791?style=for-the-badge&logo=postgresql&logoColor=white">

</div>

### Ferramentas

<div align="center">

<img src="https://skillicons.dev/icons?i=git,github,vscode" />

<br><br>

<img src="https://img.shields.io/badge/NetBeans-1B6AC6?style=for-the-badge&logo=apachenetbeanside&logoColor=white">
<img src="https://img.shields.io/badge/Insomnia-4000BF?style=for-the-badge&logo=insomnia&logoColor=white">
<img src="https://img.shields.io/badge/Navegador%20Web-4285F4?style=for-the-badge&logo=googlechrome&logoColor=white">

</div>

## Funcionalidades

### Usuários

* Cadastro de clientes;
* Cadastro de administradores e operadores;
* Controle de status ativo ou bloqueado;
* Vinculação de QR Code/RFID ao cliente;
* Consulta de saldo e limite de crédito.

### Produtos

* Cadastro de produtos;
* Código de barras gerado automaticamente em padrão EAN-13;
* Produtos vendidos por unidade;
* Produtos vendidos por peso;
* Ativação e desativação de produtos.

### Compras

* Identificação do cliente por QR Code ou RFID;
* Seleção de produto por modal de busca;
* Leitura de código de barras pela câmera;
* Cálculo automático do valor da compra;
* Validação de saldo e limite de crédito;
* Atualização automática do saldo do cliente;
* Registro do histórico de compras.

### Simulação de Balança

Para produtos vendidos por peso, o sistema abre uma simulação de balança. Como não há periférico físico conectado, o peso é informado manualmente no modal, simulando a entrada de dados de uma balança real.

### Simulação de Catraca

O sistema possui uma tela de catraca simulada, que permite registrar entrada e saída do cliente por QR Code/RFID. O sistema verifica o status do cliente, saldo e limite antes de permitir o acesso.

### Pagamentos

* Identificação do cliente por QR Code ou RFID;
* Registro de valor pago;
* Escolha da forma de pagamento;
* Atualização automática do saldo;
* Histórico de pagamentos.

### Relatórios

* Faturamento do dia;
* Quantidade de compras;
* Ticket médio;
* Compras por período;
* Clientes devedores;
* Histórico administrativo.

## Fluxo Principal do Sistema

1. O cliente é cadastrado no sistema;
2. O sistema gera um QR Code/RFID para o cliente;
3. O operador inicia uma nova compra;
4. O cliente é identificado por QR Code ou RFID;
5. O produto é selecionado manualmente ou por código de barras;
6. Se o produto for vendido por peso, o sistema abre a balança simulada;
7. O sistema calcula o valor da compra;
8. O sistema valida saldo e limite de crédito;
9. A compra é finalizada;
10. O saldo do cliente é atualizado;
11. Os dados ficam disponíveis nos relatórios.

## Fluxo de Acesso ao Refeitório

1. O cliente apresenta o QR Code;
2. A catraca simulada lê o código;
3. O sistema identifica o cliente;
4. O sistema verifica status, saldo e limite;
5. O acesso é liberado ou bloqueado;
6. O registro aparece no histórico de acessos.

## Perfis de Acesso

### Administrador

* Gerencia usuários;
* Gerencia produtos;
* Consulta relatórios;
* Acompanha acessos;
* Registra pagamentos;
* Acessa as principais funcionalidades administrativas.

### Operador

* Realiza compras;
* Identifica clientes;
* Registra pagamentos;
* Utiliza os periféricos simulados.

### Cliente

* Acessa sua conta;
* Visualiza QR Code;
* Consulta saldo;
* Consulta histórico de compras.

## Simulações Implementadas

O projeto simula três periféricos comuns em refeitórios:

* **QR Code/RFID:** identificação do cliente;
* **Leitor de código de barras:** seleção automática do produto;
* **Balança:** entrada de peso para produtos vendidos por quilo;
* **Catraca:** controle de entrada e saída do refeitório.

Essas simulações permitem demonstrar o funcionamento do sistema mesmo sem equipamentos físicos conectados.

## Requisitos para Executar

* Java 22 ou superior;
* Maven;
* PostgreSQL;
* NetBeans ou outra IDE compatível;
* Navegador com permissão de câmera para uso de QR Code e código de barras.

## Configuração do Banco de Dados

No arquivo `application.properties`, configure o acesso ao PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/refeitorio
spring.datasource.username=postgres
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

## Como Executar

1. Clone o repositório:

```bash
git clone URL_DO_REPOSITORIO
```

2. Acesse a pasta do projeto:

```bash
cd refeitorio
```

3. Configure o banco PostgreSQL.

4. Execute o projeto pela IDE ou pelo Maven:

```bash
mvn spring-boot:run
```

5. Acesse no navegador:

```text
http://localhost:8080/login
```

## Demonstração Recomendada

Para apresentar o sistema, recomenda-se seguir esta ordem:

1. Login no sistema;
2. Cadastro ou consulta de cliente;
3. Visualização do QR Code do cliente;
4. Nova compra com identificação por QR Code;
5. Seleção de produto por busca manual;
6. Seleção de produto por código de barras;
7. Produto por peso com balança simulada;
8. Finalização da compra;
9. Pagamento com identificação por QR Code/RFID;
10. Catraca simulada com entrada e saída;
11. Relatórios administrativos.

## Benefícios da Solução

* Reduz erros manuais;
* Agiliza o atendimento;
* Melhora o controle financeiro;
* Registra compras, pagamentos e acessos;
* Facilita a consulta de relatórios;
* Simula periféricos reais sem necessidade de hardware físico;
* Centraliza as principais operações do refeitório em uma única plataforma.

## Status do Projeto

Projeto acadêmico funcional, desenvolvido como protótipo de sistema web para controle de refeitórios institucionais.

## Considerações Finais

O RefeCard demonstra uma solução integrada para controle de acesso, consumo e pagamento em refeitórios. Mesmo sendo um protótipo acadêmico, o sistema apresenta regras de negócio aplicadas, integração com banco de dados, controle de usuários, simulação de periféricos e geração de relatórios administrativos.
