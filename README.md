# 🐾 PetLar

O **PetLar** é um sistema monolítico fullstack desenvolvido para **facilitar a adoção de animais**.
O backend é construído com **Java** e **Spring**, oferecendo uma API robusta para cadastro, gerenciamento e busca de animais disponíveis para adoção.  
Na persistência, utiliza **MongoDB** para garantir flexibilidade e alta performance no armazenamento de dados.

## 🛠️ Tecnologias Utilizadas

### Backend

- ☕ [Java](https://www.java.com/) - Linguagem de programação principal do projeto
- 🌱 [Spring Boot](https://spring.io/projects/spring-boot) - Framework para construção de aplicações Java rápidas e produtivas
- 🌐 [Spring Web](https://docs.spring.io/spring-framework/reference/web.html) - Módulo para criação de APIs REST
- ✅ [Spring Validation](https://docs.spring.io/spring-framework/reference/core/validation.html) - Validação de dados de entrada
- 🍃 [MongoDB](https://www.mongodb.com/) - Banco de dados NoSQL orientado a documentos
- 📦 [Flapdoodle Embedded MongoDB](https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo) – Banco de dados MongoDB embarcado para testes de integração

### Frontend

- ⚡ [Vite](https://vitejs.dev/) - Build tool para otimizar performance
- ⚛️ [React](https://react.dev/) - Biblioteca para construção da UI
- 💅 [Styled Components](https://styled-components.com/) - Estilização com CSS-in-JS
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

# Para fazer o build manualmente:
# Acesse a pasta do frontend
cd ./PetLar_FrontEnd/

# Compile o projeto com npm
npm run build:deploy

# Acesse a pasta do backend
cd ../PetLar_BackEnd/

# Compile e execute o projeto com Maven
mvn spring-boot:run

# Não se esqueça de:
# Ter o MongoDB em execução
# Configurar as credenciais e a URL do banco

# Para rodar com Docker:
# Suba os containers
docker-compose up --build
```

## 🚧 Próximos Passos

### 🖥️ Backend

- Iniciar o projeto Backend com Spring Boot ✅
- Criar domínio de **Animais** ✅
- Configurar o banco de dados **MongoDB** ✅
- Implementar métodos **CRUD** para animais (Create, Read, Update, Delete) ✅
- Estruturar o projeto seguindo boas práticas de arquitetura (controller, service, repository) ✅
- Criar **Controller** para expor endpoints REST ✅
- Criar testes para serviços e controllers ✅
- Preparar ambiente para deploy inicial (Docker ou hospedagem local) ✅

### 🎨 Frontend

- Iniciar o projeto Frontend com React + Vite ✅
- Criar layout base (header, footer, navegação) ✅
- Criar páginas: ✅
- Listagem de animais disponíveis ✅
- Detalhes de um animal ✅
- Formulário para cadastrar animal ✅
- Consumir API do backend para listar, criar, atualizar e remover animais ✅
- Implementar estilização responsiva ✅
- Criar testes de componentes ✅
