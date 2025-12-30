#### ⬅️ [Voltar para o README principal](README.md)

# 👤 User

O projeto utiliza o modelo de entidade `User` para gerenciar as informações dos **usuários do sistema**. Esta classe é mapeada como um **documento** na coleção **`Users`** do MongoDB e integra-se diretamente ao **Spring Security**, implementando a interface `UserDetails`. A autenticação é baseada em **JWT (stateless)**.

| Campo           | Tipo     | Descrição                                      |
| :-------------- | :------- | :--------------------------------------------- |
| **`id`**        | `String` | Identificador único do usuário.                |
| **`email`**     | `String` | E-mail do usuário. (Único)                     |
| **`password`**  | `String` | Senha criptografada do usuário.                |
| **`name`**      | `String` | Nome do usuário.                               |
| **`createdAt`** | `String` | Data e hora de criação do registro.            |
| **`updatedAt`** | `String` | Data e hora da última atualização do registro. |
| **`deletedAt`** | `String` | Data e hora da exclusão lógica do usuário.     |

## 🌐 Endpoints da API: Gerenciamento de Usuários

O gerenciamento dos usuários é feito através do **`UserController`** e do **`AuthController`**. Os endpoints estão agrupados sob os caminhos base:

- **Usuários:** `/api/users`
- **Autenticação:** `/api/login`

### 📌 Endpoints de Usuário

| Método HTTP  | Endpoint                   | Descrição                              | Autenticação | Parâmetros/Corpo da Requisição                                  | Resposta Esperada                                            |
| :----------- | :------------------------- | :------------------------------------- | :----------- | :-------------------------------------------------------------- | :----------------------------------------------------------- |
| **`POST`**   | `/users/cadastrar`         | Cadastrar novo usuário                 | ❌ Não       | Corpo da Requisição: `UserRequestDto` com os dados do usuário.  | `201 Created` com os dados do usuário.                       |
| **`GET`**    | `/users/me`                | Obter dados do usuário autenticado     | ✅ Sim       | —                                                               | `200 OK` com os dados do usuário.                            |
| **`PUT`**    | `/users/me`                | Atualizar dados do usuário autenticado | ✅ Sim       | Corpo da Requisição: `UserUpdateRequestDto` com os novos dados. | `200 OK` com o usuário atualizado + novo JWT (se aplicável). |
| **`PUT`**    | `/users/me/changePassword` | Alterar senha do usuário autenticado   | ✅ Sim       | Corpo da Requisição: `UserChangePasswordDto` com a nova senha.  | `200 OK` com os dados do usuário.                            |
| **`DELETE`** | `/users/me`                | Exclusão lógica da conta do usuário    | ✅ Sim       | —                                                               | `204 No Content` (Sucesso na exclusão).                      |

### 📌 Endpoint de Autenticação

| Método HTTP | Endpoint | Descrição                         | Autenticação | Parâmetros / Corpo da Requisição                                     | Resposta Esperada        |
| :---------- | :------- | :-------------------------------- | :----------- | :------------------------------------------------------------------- | :----------------------- |
| **`POST`**  | `/login` | Login do usuário e geração de JWT | ❌ Não       | Corpo da Requisição: `AuthRequestDto` com as credenciais do usuário. | `200 OK` com o Token JWT |

---

### 📝 Detalhes dos Endpoints

#### 1. Cadastro de Usuário (`POST /users/cadastrar`)

Permite o registro de uma nova conta de usuário. Os dados são validados antes do cadastro e o **e-mail** deve ser **único** no sistema.

#### 2. Login (`POST /login`)

Realiza a autenticação do usuário com **e-mail e senha**. Em caso de sucesso, retorna um **Token JWT**, que deve ser enviado no cabeçalho: `Authorization: Bearer <token>`.

#### 3. Obter Dados do Usuário (`GET /users/me`)

Retorna as informações do usuário **atualmente autenticado**. O usuário é recuperado automaticamente pelo Spring Security através do token JWT.

#### 4. Atualizar Dados (`PUT /users/me`)

Atualiza o **nome** e/ou **e-mail** do usuário autenticado.

> ⚠️ Caso o e-mail seja alterado, um **novo Token JWT** é gerado e retornado no cabeçalho `Authorization`.

#### 5. Alterar Senha (`PUT /users/me/changePassword`)

Permite que o usuário autenticado defina uma nova senha. A nova senha deve conter entre **6 e 50 caracteres** e não pode ser igual à senha anterior.

#### 6. Exclusão Lógica (`DELETE /users/me`)

Realiza a exclusão **lógica** do usuário autenticado. O registro não é removido do banco, apenas marcado com o campo `deletedAt`.

---

### 📖 Documentação da API (Swagger UI)

A documentação interativa completa de todos os endpoints da API está disponível via **Swagger UI**.

Para acessar e testar os endpoints no ambiente de desenvolvimento local, utilize o seguinte link:

> **URL de Acesso:** > `http://localhost:8080/swagger-ui.html`

## 📦 Data Transfer Objects (DTOs)

Os **Data Transfer Objects (DTOs)** são utilizados para garantir a segurança, validação, separação de responsabilidades e para padronizar o formato dos dados que entram e saem da API. O projeto **PetLar** utiliza destes DTOs para a entidade `User`:

### Entrada de dados

#### 1. `UserRequestDto` (Cadastro de novos usuários)

| Campo      | Tipo     | Validações                  | Descrição          | Exemplo             |
| :--------- | :------- | :-------------------------- | :----------------- | :------------------ |
| `email`    | `String` | `@NotBlank`, `@Email`       | E-mail do usuário. | `usuario@email.com` |
| `password` | `String` | `@NotBlank`, `@Size(6-50)`  | Senha do usuário.  | `Senha123`          |
| `name`     | `String` | `@NotBlank`, `@Size(3-110)` | Nome do usuário.   | `João da Silva`     |

#### 2. `UserUpdateRequestDto` (Atualização dos dados do usuário)

| Campo   | Tipo     | Validações                  | Descrição               | Exemplo          |
| :------ | :------- | :-------------------------- | :---------------------- | :--------------- |
| `email` | `String` | `@NotBlank`, `@Email`       | Novo e-mail do usuário. | `novo@email.com` |
| `name`  | `String` | `@NotBlank`, `@Size(3-110)` | Novo nome do usuário.   | `João Silva`     |

#### 3. `UserChangePasswordDto` (Alteração de Senha)

| Campo      | Tipo     | Validações                 | Descrição              | Exemplo        |
| :--------- | :------- | :------------------------- | :--------------------- | :------------- |
| `password` | `String` | `@NotBlank`, `@Size(6-50)` | Nova senha do usuário. | `NovaSenha123` |

#### 4. `AuthRequestDto` (Autenticação / Login)

| Campo      | Tipo     | Validações | Descrição          | Exemplo             |
| :--------- | :------- | :--------- | :----------------- | :------------------ |
| `email`    | `String` | `@Email`   | E-mail do usuário. | `usuario@email.com` |
| `password` | `String` | —          | Senha do usuário.  | `Senha123`          |

### Saída de dados

#### 1. `UserResponseDto` (Resposta)

| Campo       | Tipo     | Descrição                          |
| :---------- | :------- | :--------------------------------- |
| `id`        | `String` | ID do usuário.                     |
| `email`     | `String` | E-mail do usuário.                 |
| `name`      | `String` | Nome do usuário.                   |
| `createdAt` | `String` | Data de criação.                   |
| `updatedAt` | `String` | Data da última atualização.        |
| `deletedAt` | `String` | Data da exclusão lógica ou `null`. |

#### 2. `TokenResponseDto` (Resposta de Autenticação)

| Campo   | Tipo     | Descrição                              |
| :------ | :------- | :------------------------------------- |
| `token` | `String` | Token JWT utilizado para autenticação. |
