# 🎲 Mesa 12 App
Aplicativo Android desenvolvido em **Kotlin** para controlar partidas entre dois jogadores no jogo Mesa 12.
O app permite cadastrar os nomes dos jogadores, adicionar pontos durante a partida, registrar vitórias automaticamente, acompanhar o líder e limpar o histórico de partidas.

## 👨‍💻 Autor
Desenvolvido por **Igor Carmona**, trabalho academico para a matéria de Android Básico do Curso de Pós-Graduação em Desenvolvimento Mobile pela UTFPR/PB.

---

## 📱 Sobre o projeto

O **Mesa 12 App** foi criado para facilitar o controle de partidas entre dois jogadores, evitando a necessidade de anotar pontos manualmente.
Com ele, é possível:

- Alterar o nome dos jogadores;
- Somar pontos rapidamente;
- Registrar vitórias;
- Visualizar quem está liderando;
- Consultar o histórico de partidas;
- Limpar os dados salvos.

---

## 🚀 Funcionalidades

### 👥 Cadastro de jogadores
O usuário pode acessar a tela de configuração de nomes e informar o nome dos dois jogadores.
Após salvar, os nomes são enviados de volta para a tela principal usando `ActivityResultLauncher`.

```kotlin
setNameLauncher.launch(intent)
```
A tela de nomes retorna os dados para a `MainActivity` usando:

```kotlin
setResult(RESULT_OK, resultIntent)
finish()
```

---

### ➕ Controle de pontos
Cada jogador possui botões para adicionar pontos rapidamente:

- `+1`
- `+3`
- `+6`
- `+9`
- `+12`

Quando um jogador atinge a condição de vitória definida no `GameManager`, a vitória é registrada automaticamente.

---

### 🏆 Registro de vitórias
Quando um jogador vence uma partida, o app:

1. Salva a vitória no histórico;
2. Mostra uma mensagem informando o vencedor;
3. Atualiza o número de partidas;
4. Atualiza o líder;
5. Reseta os pontos da rodada atual.

Exemplo:

```kotlin
Toast.makeText(this, "$playerName venceu a partida!", Toast.LENGTH_SHORT).show()
```

---

### 📊 Líder da partida
O app compara a quantidade de vitórias dos dois jogadores e mostra quem está liderando.
Se os dois tiverem a mesma quantidade de vitórias, o app exibe uma mensagem de empate.

---

### 🧹 Limpar histórico
O usuário pode limpar o histórico de vitórias dos jogadores.
Ao limpar, o app zera:

- Vitórias do jogador 1;
- Vitórias do jogador 2;
- Quantidade de partidas exibida na sessão atual.

---

## 🛠️ Tecnologias utilizadas
- **Kotlin**
- **Android SDK**
- **ViewBinding**
- **SharedPreferences**
- **Activity Result API**
- **Material Components**
  
---

## 📂 Estrutura principal
```text
com.example.mesa12app
│
├── MainActivity.kt
├── SetNamePlayersActivity.kt
├── HistoricPlayers.kt
├── GameManager.kt
│
└── res/
    ├── layout/
    ├── values/
    └── drawable/
```

---
## 🧠 Como funciona o fluxo do app

### 1. Tela principal
A `MainActivity` é responsável por controlar a maior parte do app.

Ela cuida de:
- Carregar nomes salvos;
- Configurar os botões;
- Adicionar pontos;
- Registrar vitórias;
- Atualizar o líder;
- Limpar o histórico.

---

### 2. Tela de nomes
A `SetNamePlayersActivity` permite editar os nomes dos jogadores.
Quando o usuário clica em salvar, o app valida se os dois campos foram preenchidos.
Se estiver tudo certo, os nomes são enviados de volta para a `MainActivity`.

```kotlin
val resultIntent = Intent().apply {
    putExtra(EXTRA_PLAYER1_NAME, name1)
    putExtra(EXTRA_PLAYER2_NAME, name2)
}

setResult(RESULT_OK, resultIntent)
finish()
```

---

### 3. Salvamento de dados
O app usa `SharedPreferences` para salvar informações simples de forma permanente.

São salvos dados como:
- Nome do jogador 1;
- Nome do jogador 2;
- Vitórias do jogador 1;
- Vitórias do jogador 2.

Exemplo:
```kotlin
sharedPref.edit {
    putString("player1_name", name)
    apply()
}
```
Assim, mesmo fechando o app, os dados continuam salvos.

---

## 💾 Dados salvos no app
| Chave | Descrição |
|------|-----------|
| `player1_name` | Nome do jogador 1 |
| `player2_name` | Nome do jogador 2 |
| `player1_wins` | Vitórias do jogador 1 |
| `player2_wins` | Vitórias do jogador 2 |

---

## 📌 Principais arquivos

### `MainActivity.kt`
Controla a tela principal do app.

Responsabilidades:
- Exibir nomes dos jogadores;
- Controlar pontos;
- Registrar vitórias;
- Atualizar líder;
- Limpar histórico;
- Abrir outras telas.

---

### `SetNamePlayersActivity.kt`
Controla a tela onde o usuário altera os nomes dos jogadores.

Responsabilidades:

- Ler os nomes digitados;
- Validar campos vazios;
- Enviar os nomes para a `MainActivity`.

---

### `GameManager.kt`

Responsável pela regra de pontuação do jogo.
Ele controla quando um jogador recebe pontos e quando uma vitória acontece.

---

### `HistoricPlayers.kt`
Tela responsável por exibir o histórico das partidas ou vitórias registradas.

---

## ▶️ Como executar o projeto

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/mesa12app.git
```

2. Abra o projeto no **Android Studio**.
3. Aguarde o Gradle sincronizar.
4. Execute o app em um emulador ou dispositivo físico.

---

## 📋 Requisitos

- Android Studio
- Kotlin
- Gradle configurado
- Emulador Android ou celular físico

---

## 📄 Licença

Este projeto é de uso livre para estudos e aprendizado.

---

# 🎲 Mesa 12 App

Controle suas partidas de forma simples, rápida e organizada.
