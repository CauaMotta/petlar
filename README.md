# 🐾 PetLar

O **PetLar** é um sistema monolítico fullstack desenvolvido para **facilitar a adoção de animais**.
O backend é construído com **Java** e **Spring**, oferecendo uma API robusta para cadastro, gerenciamento e busca de animais disponíveis para adoção.  
Na persistência, utiliza **MongoDB** para garantir flexibilidade e alta performance no armazenamento de dados.

## 🛠️ Tecnologias Utilizadas

- ☕ [Java](https://www.java.com/) - Linguagem de programação principal do projeto
- 🌱 [Spring Boot](https://spring.io/projects/spring-boot) - Framework para construção de aplicações Java rápidas e produtivas
- 🌐 [Spring Web](https://docs.spring.io/spring-framework/reference/web.html) - Módulo para criação de APIs REST
- ✅ [Spring Validation](https://docs.spring.io/spring-framework/reference/core/validation.html) - Validação de dados de entrada
- 🍃 [MongoDB](https://www.mongodb.com/) - Banco de dados NoSQL orientado a documentos

## 📦 Instalação e Execução

Para rodar o projeto localmente, siga os passos abaixo:

```sh
# Clone este repositório
git clone https://github.com/CauaMotta/petlar

# Acesse a pasta do projeto
cd petlar

# Compile e execute o projeto com Maven
mvn spring-boot:run

# Não se esqueça de:
# Ter o MongoDB em execução
# Configurar as credenciais e a URL do banco
```

## 🚧 Próximos Passos

### 🖥️ Backend

- Iniciar o projeto Backend com Spring Boot ✅
- Criar domínio de **Animais** ✅
- Configurar o banco de dados **MongoDB** ✅
- Implementar métodos **CRUD** para animais (Create, Read, Update, Delete) ✅
- Estruturar o projeto seguindo boas práticas de arquitetura (controller, service, repository)
- Criar **Controller** para expor endpoints REST
- Criar testes para serviços e controllers
- Preparar ambiente para deploy inicial (Docker ou hospedagem local)

### 🎨 Frontend

- Iniciar o projeto Frontend com React + Vite
- Criar layout base (header, footer, navegação)
- Criar páginas:
- Listagem de animais disponíveis
- Detalhes de um animal
- Formulário para cadastrar animal
- Consumir API do backend para listar, criar, atualizar e remover animais
- Implementar estilização responsiva
- Criar testes de componentes
