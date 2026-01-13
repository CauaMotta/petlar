#### ⬅️ [Voltar para o README principal](README.md)

# 📝 Adoption

O projeto utiliza o modelo de entidade `Adoption` para gerenciar as solicitações de adoção realizadas sobre animais disponíveis para adoção. Esta classe é mapeada como um **documento** na coleção **`Adoptions`** do MongoDB.

| Campo               | Tipo                    | Descrição                                                             |
| :------------------ | :---------------------- | :-------------------------------------------------------------------- |
| **`id`**            | `String`                | O identificador único da solicitação.                                 |
| **`status`**        | `AdoptionStatus` (Enum) | O status atual da adoção (Pendente, Cancelado, Recusado ou Aprovado). |
| **`animalId`**      | `String`                | O identificador único do animal.                                      |
| **`animalOwnerId`** | `String`                | O identificador único do usuário doador do animal.                    |
| **`adopterId`**     | `String`                | O identificador único do adotante.                                    |
| **`reason`**        | `String`                | A justificativa do porquê o usuário deseja adotar o animal.           |
| **`createdAt`**     | `String`                | A data e hora em que a adoção foi registrada.                         |
| **`updatedAt`**     | `String`                | A data e hora em que a adoção foi atualizada.                         |

## 🌐 Endpoints da API: Gerenciamento de Adoções

O gerenciamento das solicitações de adoção é realizado através do **`AdoptionController`**, responsável por controlar todo o fluxo de adoção entre adotantes e doadores. Todos os endpoints estão agrupados sob o caminho base: **`/api/adoptions`**.

> 🔒 Todos os endpoints exigem autenticação.

| Método HTTP | Endpoint       | Descrição                               | Parâmetros/Corpo da Requisição                                                                                  | Resposta Esperada                                                   |
| :---------- | :------------- | :-------------------------------------- | :-------------------------------------------------------------------------------------------------------------- | :------------------------------------------------------------------ |
| **`POST`**  | `/`            | **Solicitar Adoção**                    | Corpo da Requisição: `AdoptionRequestDto` com os dados da solicitação.                                          | `201 CREATED` com a solicitação de adoção iniciada.                 |
| **`GET`**   | `/me/requests` | **Minhas solicitações**                 | Filtros opcionais: `parâmetros de paginação`.                                                                   | `200 OK` com uma lista paginada de adoções (`AdoptionResponseDto`). |
| **`GET`**   | `/me/animals`  | **Solicitações para meus animais**      | Filtros opcionais: `parâmetros de paginação`.                                                                   | `200 OK` com uma lista paginada de adoções (`AdoptionResponseDto`). |
| **`PATCH`** | `/{id}`        | **Editar justificativa da solicitação** | Variável de caminho: `{id}` (ID da solicitação). Corpo da Requisição: `EditReasonDto` com a nova justificativa. | `200 OK` com a solicitação editada.                                 |
| **`PATCH`** | `/{id}/cancel` | **Cancelar solicitação**                | Variável de caminho: `{id}` (ID da solicitação).                                                                | `200 OK` com a solicitação cancelada.                               |
| **`PATCH`** | `/{id}/accept` | **Aprovar solicitação**                 | Variável de caminho: `{id}` (ID da solicitação).                                                                | `200 OK` com a solicitação aprovada.                                |
| **`PATCH`** | `/{id}/deny`   | **Rejeitar solicitação**                | Variável de caminho: `{id}` (ID da solicitação).                                                                | `200 OK` com a solicitação recusada.                                |

---

### 📝 Detalhes dos Endpoints

#### 1. Solicitar adoção (`POST /adoptions`)

Este endpoin é responsável por iniciar o processo de adoção de um animal disponível. O usuário autenticado é automaticamente identificado como adotante. Regras importantes:

- O usuário não pode solicitar adoção do próprio animal.
- O animal deve estar com status DISPONÍVEL.
- Apenas uma solicitação ativa por animal é permitida.

#### 2. Minhas solicitações de adoção (`GET /adoptions/me/requests`)

Retorna uma lista paginada de todas as solicitações de adoção feitas pelo usuário autenticado.

#### 3. Solicitações para meus animais (`GET /adoptions/me/animals`)

Permite que o doador visualize todas as solicitações de adoção recebidas para os animais que ele cadastrou.

#### 4. Editar justificativa de uma solicitação (`PATCH /adoptions/{id}`)

Permite que o adotante altere a justificativa da solicitação enquanto ela estiver **`PENDENTE`**.

#### 5. Cancelar solicitação (`PATCH /adoptions/{id}/cancel`)

Permite que o adotante cancele uma solicitação de adoção.

🚫 Ações **não** permitidas:

- **Cancelar** solicitações já aprovadas ou rejeitadas
- **Cancelar** solicitações de outros usuários

#### 6. Aprovar ou rejeitar solicitação (`PATCH /adoptions/{id}/accept | /adoptions/{id}/deny`)

Permite que o doador aprove ou rejeite uma solicitação de adoção.

✅ Ao aprovar:

- A solicitação recebe status **APROVADO**
- O animal é marcado como **ADOTADO**

❌ Ao rejeitar:

- A solicitação recebe status **NEGADO**
- O animal volta a ficar **DISPONÍVEL**

---

### 📖 Documentação da API (Swagger UI)

A documentação interativa completa de todos os endpoints da API está disponível via **Swagger UI**.

Para acessar e testar os endpoints no ambiente de desenvolvimento local, utilize o seguinte link:

> **URL de Acesso:** > `http://localhost:8080/swagger-ui.html`

## 📦 Data Transfer Objects (DTOs)

Os **Data Transfer Objects (DTOs)** são utilizados para garantir a separação de responsabilidades e para padronizar o formato dos dados que entram e saem da API. O projeto **PetLar** utiliza dos seguintes DTOs para a entidade `Adoption`:

### 1. `AdoptionRequestDto` (Entrada de Dados)

Este DTO é usado para **receber** dados na operação de iniciar uma solicitação de adoção **(`POST`)**. Ele implementa regras de validação para garantir a integridade dos dados na entrada:

| Campo          | Tipo     | Validações                 | Descrição                                                 | Exemplo                                                |
| :------------- | :------- | :------------------------- | :-------------------------------------------------------- | :----------------------------------------------------- |
| **`animalId`** | `String` | `@NotBlank`                | ID do animal a ser requisitado.                           | `"72af45b0..."`                                        |
| **`reason`**   | `String` | `@NotNull`, `@Size(3-255)` | Justificativa do porquê o usuário deseja adotar o animal. | `"Procuro um novo amigo para fazer parte da família."` |

### 2. `EditReasonDto` (Entrada de Dados)

Este DTO é usado para **receber** dados na operação de atualizar a justificativa de uma solicitação de adoção **(`PATCH`)**:

| Campo        | Tipo     | Validações                 | Descrição                     | Exemplo                                      |
| :----------- | :------- | :------------------------- | :---------------------------- | :------------------------------------------- |
| **`reason`** | `String` | `@NotNull`, `@Size(3-255)` | Nova justificativa da adoção. | `"Agora tenho mais tempo livre para o pet."` |

### 3. `AdoptionResponseDto` (Saída de Dados)

Este DTO é usado para **retornar** os dados de uma solicitação em operações de **(`GET`)** e após o sucesso em operações de modificação:

| Campo             | Tipo                    | Descrição                           |
| :---------------- | :---------------------- | :---------------------------------- |
| **`id`**          | `String`                | Identificador único da solicitação. |
| **`status`**      | `AdoptionStatus` (Enum) | Status atual da solicitação.        |
| **`animal`**      | `AnimalSummaryDto`      | Animal que está sendo adotado.      |
| **`animalOwner`** | `UserSummaryDto`        | O usuário doador do animal.         |
| **`adopter`**     | `UserSummaryDto`        | O usuário que deseja adotar.        |
| **`reason`**      | `String`                | A justificativa da adoção.          |
| **`createdAt`**   | `String`                | Data e hora do cadastro.            |
| **`updatedAt`**   | `String`                | Data e hora da última atualização.  |
