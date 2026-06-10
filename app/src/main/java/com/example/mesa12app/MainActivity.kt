package com.example.mesa12app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mesa12app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Classe responsável por controlar os pontos do jogo
    private val gameManager = GameManager()

    private var numberOfMatches = 0

    // Abre a tela de nomes esperando receber um resultado de volta
    private val setNameLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        // Verifica se a outra tela finalizou com sucesso (a outra tela retorna isso)
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data

            // Recupera os nomes usando as mesmas chaves usadas no putExtra
            val name1 = data?.getStringExtra(SetNamePlayersActivity.EXTRA_PLAYER1_NAME)
            val name2 = data?.getStringExtra(SetNamePlayersActivity.EXTRA_PLAYER2_NAME)

            // Se o nome do jogador veio preenchido, atualiza a tela e salva
            name1?.let {
                binding.player1Section.tvPlayerNameValue.text = it
                savePlayerName(1, it)
            }
            name2?.let {
                binding.player2Section.tvPlayerNameValue.text = it
                savePlayerName(2, it)
            }

            // Atualiza o líder, porque os nomes podem ter mudado
            updateLeaderDisplay()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left, systemBars.top, systemBars.right, systemBars.bottom
            )
            insets
        }
    }

    override fun onStart() {
        super.onStart()

        loadSavedNames()
        setupListeners()
        updateMatchCountDisplay()
        updateLeaderDisplay()
    }

    private fun loadSavedNames() {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)

        // Busca o nome salvo do jogador 1 ou usa "Jogador 1" como padrão
        binding.player1Section.tvPlayerNameValue.text =
            sharedPref.getString("player1_name", "Jogador 1")

        // Busca o nome salvo do jogador 2 ou usa "Jogador 2" como padrão
        binding.player2Section.tvPlayerNameValue.text =
            sharedPref.getString("player2_name", "Jogador 2")
    }

    private fun savePlayerName(playerNum: Int, name: String) {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)

        // Salva o nome do jogador de forma permanente no ‘app’ (sharedPreferences)
        sharedPref.edit {
            putString("player${playerNum}_name", name)
            apply()
        }
    }

    private fun setupListeners() {
        binding.btnSetNamePlayers.setOnClickListener {
            val intent = Intent(this, SetNamePlayersActivity::class.java)

            // Abre a tela esperando receber os nomes de volta
            setNameLauncher.launch(intent)
        }

        binding.btnHistoricOfMatches.setOnClickListener {
            val intent = Intent(this, HistoricPlayers::class.java)
            startActivity(intent)
        }

        binding.btnCleanHistoric.setOnClickListener {
            resetHistoricalData()
        }

        // Configura os botões de pontos dos dois jogadores
        setupPointsListeners(1)
        setupPointsListeners(2)
    }

    /**
     * Configura os botões de adicionar pontos de um jogador.
     */
    private fun setupPointsListeners(playerNum: Int) {
        // Escolhe a seção correta da tela dependendo do jogador
        val section = if (playerNum == 1) {
            binding.player1Section
        } else {
            binding.player2Section
        }

        // Lista que liga cada botão ao valor de pontos correspondente
        val winButtons = listOf(
            section.btWin1 to 1,
            section.btWin3 to 3,
            section.btWin6 to 6,
            section.btWin9 to 9,
            section.btWin12 to 12
        )

        // Para cada botão, adiciona os pontos correspondentes ao clicar
        winButtons.forEach { (button, points) ->
            button.setOnClickListener {
                addPoints(playerNum, points)
            }
        }
    }

    private fun addPoints(playerNum: Int, points: Int) {
        val isWin = gameManager.addPoints(playerNum, points)

        // Se venceu, registra a vitória
        if (isWin) {
            recordWin(playerNum)
        }
    }

    /**
     * Registry a vitória de um jogador, salva no histórico e reseta os pontos.
     */
    private fun recordWin(playerNum: Int) {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)

        // Busca quantas vitórias esse jogador já tinha
        val currentWins = sharedPref.getInt("player${playerNum}_wins", 0)

        // Salva a nova quantidade de vitórias
        sharedPref.edit {
            putInt("player${playerNum}_wins", currentWins + 1)
            apply()
        }

        // Pega o nome exibido na tela para mostrar no Toast
        val playerName = if (playerNum == 1) {
            binding.player1Section.tvPlayerNameValue.text
        } else {
            binding.player2Section.tvPlayerNameValue.text
        }

        Toast.makeText(this, "$playerName venceu a partida!", Toast.LENGTH_SHORT).show()

        numberOfMatches++

        // Atualiza a quantidade de partidas na tela e atualiza quem está a liderar
        updateMatchCountDisplay()
        updateLeaderDisplay()

        gameManager.resetPoints()
    }

    private fun updateLeaderDisplay() {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)

        // Busca vitórias salvas dos jogadores
        val p1Wins = sharedPref.getInt("player1_wins", 0)
        val p2Wins = sharedPref.getInt("player2_wins", 0)

        // Busca nomes salvos dos jogadores
        val p1Name = sharedPref.getString("player1_name", "Jogador 1") ?: "Jogador 1"
        val p2Name = sharedPref.getString("player2_name", "Jogador 2") ?: "Jogador 2"

        if (p1Wins == 0 && p2Wins == 0) {
            binding.containerLeaderMatch.visibility = View.GONE
            return
        }

        binding.containerLeaderMatch.visibility = View.VISIBLE

        // Decide qual mensagem mostrar conforme as vitórias
        when {
            p1Wins > p2Wins -> {
                binding.tvLiderMatch.text = getString(R.string.leader_message, p1Name)
            }

            p2Wins > p1Wins -> {
                binding.tvLiderMatch.text = getString(R.string.leader_message, p2Name)
            }

            else -> {
                binding.tvLiderMatch.text = getString(R.string.tied_match)
            }
        }
    }

    private fun resetHistoricalData() {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)

        // Zera as vitórias dos dois jogadores
        sharedPref.edit {
            putInt("player1_wins", 0)
            putInt("player2_wins", 0)
            apply()
        }

        numberOfMatches = 0

        // Atualiza a tela após limpar
        updateMatchCountDisplay()
        updateLeaderDisplay()

        Toast.makeText(this, "Histórico limpo!", Toast.LENGTH_SHORT).show()
    }

    // Mostra na tela a quantidade de partidas jogadas
    private fun updateMatchCountDisplay() {
        binding.tvNumberOfMatches.text = numberOfMatches.toString()
    }
}