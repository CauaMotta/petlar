#### ⬅️ [Voltar para o README principal](README.md)

# 🐶 Animal

O projeto utiliza o modelo de entidade `Animal` para gerenciar as informações sobre os animais disponíveis para adoção. Esta classe é mapeada como um **documento** na coleção **`Animals`** do MongoDB.

| Campo                  | Tipo                    | Descrição                                                   |
| :--------------------- | :---------------------- | :---------------------------------------------------------- |
| **`id`**               | `String`                | O identificador único do animal (Chave Primária).           |
| **`name`**             | `String`                | O nome do animal.                                           |
| **`dob`**              | `LocalDate`             | A data de nascimento do animal.                             |
| **`weight`**           | `Integer`               | O peso do animal (em gramas).                               |
| **`type`**             | `AnimalType` (Enum)     | O tipo de animal (Cão, Gato, Passaro ou Outro).             |
| **`sex`**              | `AnimalSex` (Enum)      | O sexo do animal (Macho ou Fêmea).                          |
| **`size`**             | `AnimalSize` (Enum)     | O porte do animal (Pequeno, Médio ou Grande).               |
| **`registrationDate`** | `LocalDateTime`         | A data e hora em que o animal foi registrado.               |
| **`status`**           | `AdoptionStatus` (Enum) | O status atual de adoção (Disponível, Pendente ou Adotado). |
| **`description`**      | `String`                | Descrição detalhada e características do animal.            |

## 🌐 Endpoints da API: Gerenciamento de Animais

O gerenciamento dos animais disponíveis para adoção é feito através do **`AnimalController`**, que expõe os endpoints RESTful para operações CRUD (Criar, Ler, Atualizar, Excluir). Todos os endpoints estão agrupados sob o caminho base: **`/api/animals`**.

| Método HTTP  | Endpoint        | Descrição                 | Parâmetros/Corpo da Requisição                                                                                        | Resposta Esperada                                                            |
| :----------- | :-------------- | :------------------------ | :-------------------------------------------------------------------------------------------------------------------- | :--------------------------------------------------------------------------- |
| **`GET`**    | `/animals`      | **Listar Animais**        | Filtros opcionais: `status`, `type` e `parâmetros de paginação`.                                                      | `200 OK` com uma lista paginada de animais (`AnimalResponseDto`).            |
| **`GET`**    | `/animals/{id}` | **Buscar por ID**         | Variável de caminho: `{id}` (ID do animal).                                                                           | `200 OK` com os detalhes do animal (`AnimalResponseDto`) ou `404 Not Found`. |
| **`POST`**   | `/animals`      | **Cadastrar Novo Animal** | Corpo da Requisição: Objeto `AnimalRequestDto` com os dados do novo animal.                                           | `200 OK` com o animal cadastrado, incluindo o ID.                            |
| **`PUT`**    | `/animals/{id}` | **Atualizar Animal**      | Variável de caminho: `{id}` (ID do animal). Corpo da Requisição: `AnimalRequestDto` com os dados a serem atualizados. | `200 OK` com o animal atualizado ou `404 Not Found`.                         |
| **`DELETE`** | `/animals/{id}` | **Excluir Animal**        | Variável de caminho: `{id}` (ID do animal).                                                                           | `204 No Content` (Sucesso na exclusão).                                      |

---

### 📝 Detalhes dos Endpoints

#### 1. Listagem e Filtros (`GET /animals`)

Este endpoint é o principal para consulta. Por padrão, ele lista apenas os animais com status **`disponivel`**, mas permite que o usuário filtre a lista por:

- **`status`**: O status de adoção desejado (ex: `disponivel`, `adotado`).
- **`type`**: O tipo de animal (ex: `gato`, `cachorro`).
- **`Paginação`**: Utiliza parâmetros `page`, `size` e `sort` para gerenciar grandes volumes de dados de forma eficiente.

#### 2. Cadastro (`POST /animals`)

Permite o registro de um novo animal, exigindo que o corpo da requisição siga o formato do `AnimalRequestDto`. A validação é aplicada para garantir a integridade dos dados antes do cadastro.

#### 3. Exclusão (`DELETE /animals/{id}`)

Realiza a remoção **permanente** de um registro do animal do banco de dados, retornando um status `204 No Content` em caso de sucesso.

---

### 📖 Documentação da API (Swagger UI)

A documentação interativa completa de todos os endpoints da API está disponível via **Swagger UI**.

Para acessar e testar os endpoints no ambiente de desenvolvimento local, utilize o seguinte link:

> **URL de Acesso:** > `http://localhost:8080/swagger-ui.html`

## 📦 Data Transfer Objects (DTOs)

Os **Data Transfer Objects (DTOs)** são utilizados para garantir a separação de responsabilidades e para padronizar o formato dos dados que entram e saem da API. O projeto **PetLar** utiliza dois DTOs principais para a entidade `Animal`:

### 1. `AnimalRequestDto` (Entrada de Dados)

Este DTO é usado para **receber** dados nas operações de **criação (`POST`)** e **atualização (`PUT`)** de um animal. Ele implementa regras de validação para garantir a integridade dos dados na entrada:

| Campo             | Tipo        | Validações                  | Descrição                           | Exemplo                      |
| :---------------- | :---------- | :-------------------------- | :---------------------------------- | :--------------------------- |
| **`name`**        | `String`    | `@NotBlank`, `@Size(3-110)` | Nome do animal.                     | `"Rex"`                      |
| **`dob`**         | `LocalDate` | `@NotNull`                  | Data de nascimento do animal.       | `"2025-11-10"`               |
| **`weight`**      | `Integer`   | `@NotNull`, `@Min(1)`       | Peso do animal                      | `1200`                       |
| **`type`**        | `String`    | `@NotBlank`                 | Espécie do animal.                  | `"Cachorro"`                 |
| **`sex`**         | `String`    | `@NotBlank`                 | Sexo do animal.                     | `"Macho"`                    |
| **`size`**        | `String`    | `@NotBlank`                 | Porte do animal.                    | `"Médio"`                    |
| **`description`** | `String`    | `@Size(3-255)`              | Descrição adicional sobre o animal. | `"Animal dócil e vacinado."` |

### 2. `AnimalResponseDto` (Saída de Dados)

Este DTO é usado para **retornar** os dados de um animal em operações de **consulta (`GET`)** e após o sucesso em operações de modificação. Ele contém todos os campos da entidade, incluindo os valores de **`enum`** e informações geradas pelo sistema:

| Campo                  | Tipo                    | Descrição                      |
| :--------------------- | :---------------------- | :----------------------------- |
| **`id`**               | `String`                | Identificador único do animal. |
| **`name`**             | `String`                | Nome do animal.                |
| **`dob`**              | `LocalDate`             | Data de nascimento do animal.  |
| **`weight`**           | `Integer`               | Peso (em gramas).              |
| **`type`**             | `AnimalType` (Enum)     | Espécie do animal.             |
| **`sex`**              | `AnimalSex` (Enum)      | Sexo do animal.                |
| **`size`**             | `AnimalSize` (Enum)     | Porte do animal.               |
| **`registrationDate`** | `LocalDateTime`         | Data e hora do cadastro.       |
| **`status`**           | `AdoptionStatus` (Enum) | Status de adoção atual.        |
| **`description`**      | `String`                | Descrição adicional.           |
