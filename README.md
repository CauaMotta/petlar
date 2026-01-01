# 🐾 PetLar

O **PetLar** é um sistema desenvolvido para **facilitar a adoção de animais**.
O backend é construído com **Java** e **Spring**, oferecendo uma API robusta para cadastro, gerenciamento e busca de animais disponíveis para adoção.
O frontend, construído em React, oferece uma interface moderna, responsiva e de fácil uso.
Na camada de persistência, utiliza **MongoDB** para garantir flexibilidade e alta performance no armazenamento de dados.

> **Nota:** O projeto está em refatoração, no momento o frontend não está mais funcional.

## 📐 Modelos do Projeto

Os modelos representam as **entidades principais do domínio** da aplicação e definem a estrutura dos dados persistidos no banco, além de servirem como base para regras de negócio.

### Modelos disponíveis

- 🐶 [Animal](Animal.md) – Representa os animais disponíveis para adoção
- 👤 [User](User.md) – Representa os usuários da aplicação

## 🛠️ Tecnologias Utilizadas

### > Backend

- ☕ [Java](https://www.java.com/) - Linguagem de programação principal do projeto
- 🌱 [Spring Boot](https://spring.io/projects/spring-boot) - Framework para construção de aplicações Java rápidas e produtivas
- 🌐 [Spring Web](https://docs.spring.io/spring-framework/reference/web.html) - Módulo para criação de APIs REST
- 🔐 [Spring Security](https://spring.io/projects/spring-security) - Autenticação, autorização e proteção de rotas da aplicação
- 🔑 [Auth0 Java JWT](https://github.com/auth0/java-jwt) - Geração e validação de tokens JWT para autenticação segura
- 🛠️ [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools) - Ferramentas para produtividade em desenvolvimento
- 🧬 [Lombok](https://projectlombok.org/) - Redução de código boilerplate com anotações
- ✅ [Spring Validation](https://docs.spring.io/spring-framework/reference/core/validation.html) - Validação de dados de entrada
- 📖 [SpringDoc OpenAPI](https://springdoc.org/) - Geração automática de documentação Swagger para a API
- 🍃 [MongoDB](https://www.mongodb.com/) - Banco de dados NoSQL orientado a documentos
- 📦 [Flapdoodle Embedded MongoDB](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) – Banco de dados MongoDB embarcado para testes de integração
- 📁 [Apache Commons IO](https://commons.apache.org/proper/commons-io/) – Utilitários para manipulação de arquivos, streams e diretórios

### > Frontend

- ⚡ [Vite](https://vitejs.dev/) - Build tool para otimizar performance
- ⚛️ [React](https://react.dev/) - Biblioteca para construção da UI
- 💅 [Styled Components](https://styled-components.com/) - Estilização com CSS-in-JS
- 🎨 [Font Awesome](https://fontawesome.com/) - Ícones personalizáveis para a interface
- 🧭 [React Router DOM](https://reactrouter.com/) - Navegação entre páginas com rotas dinâmicas no React
- 🗂️ [Redux](https://redux.js.org/) – Gerenciamento global de estado
- 🔄 [React Spinners](https://www.davidhu.io/react-spinners/) - Componentes de carregamento estilizados para React
- 🧪 [Vitest](https://vitest.dev/) - Testes unitários rápidos e eficientes para projetos com Vite
- 🧩 [Testing Library](https://testing-library.com/) - Conjunto de ferramentas para testes acessíveis e eficazes
- ✅ [Yup](https://github.com/jquense/yup) - Validação de formulários de forma simples e eficiente
- 📝 [Formik](https://formik.org/) - Gerenciamento de formulários no React
- 🔽 [React Select](https://react-select.com/) - Componente poderoso e personalizável para seleção
- 🔢 [React IMask](https://imask.js.org/react.html) - Máscaras de entrada flexíveis e fáceis de integrar em inputs React

## 📦 Instalação e Execução

Para rodar o projeto localmente, siga os passos abaixo:

```sh
# Clone este repositório
git clone https://github.com/CauaMotta/petlar

# Acesse a pasta do projeto
cd petlar

# Pode ser executado via Docker com o seguinte comando
docker-compose up --build
# A aplicação fica disponivel através de: http://localhost:8080

# Ou manualmente com os passos a seguir:
# 1º passo: executar o servidor backend
# Acesse a pasta
cd ../PetLar_BackEnd/

# Compile e execute o projeto com Maven
mvn spring-boot:run

# Não se esqueça de:
# Ter o MongoDB em execução e
# Configurar as váriaveis de ambiente
```
