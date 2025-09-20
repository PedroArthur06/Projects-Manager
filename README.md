# 📋 Gerenciador de Projetos (Projects Manager)

## ℹ️ Sobre o Projeto

O Gerenciador de Projetos é uma aplicação de console em Java desenvolvida como um projeto escolar, com o objetivo de demonstrar a implementação de uma arquitetura em camadas e a manipulação de um banco de dados relacional 🚀. O sistema simula um ambiente de gerenciamento onde é possível criar projetos, adicionar membros e gerenciar tarefas, aplicando regras de negócio para garantir a integridade e a lógica das operações.

Este projeto foi construído utilizando Java puro com JDBC para a persistência de dados, Maven para o gerenciamento de dependências e JUnit para futuros testes, seguindo as melhores práticas de desenvolvimento como a separação de responsabilidades (DAO, Service, Model) e o uso de um arquivo `.env` para a configuração do banco de dados.

## ✅ Funcionalidades Principais

A camada de serviço (`ManagerService.java`) implementa as seguintes regras de negócio:
- **Criação de Projetos:** Valida se o nome do projeto foi fornecido e define o status inicial como "Planejado".
- **Associação de Membros:** Permite adicionar membros a um projeto, mas impede a associação caso o projeto ou o membro não existam, ou se o projeto já estiver com o status "Concluído".
- **Conclusão de Tarefas:** Ao marcar uma tarefa como "Concluída", o sistema verifica se restam outras tarefas pendentes no mesmo projeto. Se não houver, o status do projeto é automaticamente alterado para "Concluído".

## 🛠️ Tecnologias Utilizadas

- **Linguagem:** Java 17 ☕
- **Banco de Dados:** MySQL 🐬
- **Gerenciador de Dependências:** Apache Maven
- **Bibliotecas:**
  - `MySQL Connector/J`: Driver para a conexão com o banco de dados MySQL.
  - `Lombok`: Para reduzir código boilerplate nos modelos (getters, setters).
  - `java-dotenv`: Para carregar as credenciais do banco de dados a partir de um arquivo `.env`.
  - `JUnit 5`: Para a suíte de testes unitários.

## 💾 Estrutura do Banco de Dados

O banco de dados é composto por 4 tabelas principais para gerenciar as entidades do sistema. O relacionamento entre `projects` e `members` é do tipo muitos-para-muitos, sendo controlado pela tabela associativa `project_members`.


## 🚀 Como Executar o Projeto

Siga os passos abaixo para configurar e executar o projeto em seu ambiente local.

**Pré-requisitos:**
- JDK 17 ou superior
- Apache Maven
- MySQL Server

**1. Clone o Repositório**
```bash
git clone https://github.com/PedroArthur06/Projects-Manager
cd projectsmanager
```

**2. Configure o Banco de Dados**
   - Certifique-se de que seu servidor MySQL está em execução.
   - Execute o script `src/db/schema.sql` no seu cliente MySQL (como o Workbench) para criar o banco de dados `projects_manager` e suas respectivas tabelas.

**3. Configure as Variáveis de Ambiente**
   - Na raiz do projeto, crie um arquivo chamado `.env`.
   - Adicione as suas credenciais de acesso ao banco de dados neste arquivo:
     ```env
     DB_URL=jdbc:mysql://localhost:3306/projects_manager
     DB_USER=seu_usuario_mysql
     DB_PASSWORD=sua_senha_mysql
     ```

**4. Compile e Execute**
   - Use o Maven para compilar o projeto e instalar as dependências:
     ```bash
     mvn clean install
     ```
   - Execute a classe principal para testar as funcionalidades:
     ```bash
     mvn exec:java -Dexec.mainClass="br.com.pedroart.projectsmanager.Main"
     ```

## 👨‍💻 Autor

**Pedro Art** - www.linkedin.com/in/pedro-arthur-rod
