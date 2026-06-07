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
    
    private val gameManager = GameManager()
    private var numberOfMatches = 0

    // Launcher para abrir a tela de nomes e receber o resultado de volta
    private val setNameLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val name1 = data?.getStringExtra(SetNamePlayersActivity.EXTRA_PLAYER1_NAME)
            val name2 = data?.getStringExtra(SetNamePlayersActivity.EXTRA_PLAYER2_NAME)

            // Atualiza os nomes na UI e salva permanentemente
            name1?.let {
                binding.player1Section.tvPlayerNameValue.text = it
                savePlayerName(1, it)
            }
            name2?.let {
                binding.player2Section.tvPlayerNameValue.text = it
                savePlayerName(2, it)
            }
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
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Carrega dados iniciais
        loadSavedNames()
        setupListeners()
        updateMatchCountDisplay()
        updateLeaderDisplay()
    }

    private fun loadSavedNames() {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)
        binding.player1Section.tvPlayerNameValue.text =
            sharedPref.getString("player1_name", "Jogador 1")
        binding.player2Section.tvPlayerNameValue.text =
            sharedPref.getString("player2_name", "Jogador 2")
    }

    private fun savePlayerName(playerNum: Int, name: String) {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)
        sharedPref.edit {
            putString("player${playerNum}_name", name)
            apply()
        }
    }

    private fun setupListeners() {
        binding.btnSetNamePlayers.setOnClickListener {
            val intent = Intent(this, SetNamePlayersActivity::class.java)
            setNameLauncher.launch(intent)
        }

        binding.btnHistoricOfMatches.setOnClickListener {
            val intent = Intent(this, HistoricPlayers::class.java)
            startActivity(intent)
        }

        binding.btnCleanHistoric.setOnClickListener {
            resetHistoricalData()
        }

        setupPointsListeners(1)
        setupPointsListeners(2)
    }

    /**
     * Configura os botões de adicionar pontos (+1, +3, etc) de uma sessão de jogador.
     */
    private fun setupPointsListeners(playerNum: Int) {
        val section = if (playerNum == 1) binding.player1Section else binding.player2Section

        // Mapeia cada botão ao seu respetivo valor de pontos
        val winButtons = listOf(
            section.btWin1 to 1,
            section.btWin3 to 3,
            section.btWin6 to 6,
            section.btWin9 to 9,
            section.btWin12 to 12
        )

        winButtons.forEach { (button, points) ->
            button.setOnClickListener {
                addPoints(playerNum, points)
            }
        }
    }

    private fun addPoints(playerNum: Int, points: Int) {
        val isWin = gameManager.addPoints(playerNum, points)
        if (isWin) {
            recordWin(playerNum)
        }
    }

    /**
     * Registrar a vitória de um jogador, salva no histórico e reseta os pontos da rodada.
     */
    private fun recordWin(playerNum: Int) {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)
        val currentWins = sharedPref.getInt("player${playerNum}_wins", 0)

        sharedPref.edit {
            putInt("player${playerNum}_wins", currentWins + 1)
            apply()
        }

        val playerName = if (playerNum == 1) {
            binding.player1Section.tvPlayerNameValue.text
        } else {
            binding.player2Section.tvPlayerNameValue.text
        }

        Toast.makeText(this, "$playerName venceu a partida!", Toast.LENGTH_SHORT).show()

        numberOfMatches++
        updateMatchCountDisplay()
        updateLeaderDisplay() 
        gameManager.resetPoints()
    }

    private fun updateLeaderDisplay() {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)
        val p1Wins = sharedPref.getInt("player1_wins", 0)
        val p2Wins = sharedPref.getInt("player2_wins", 0)
        val p1Name = sharedPref.getString("player1_name", "Jogador 1") ?: "Jogador 1"
        val p2Name = sharedPref.getString("player2_name", "Jogador 2") ?: "Jogador 2"

        if (p1Wins == 0 && p2Wins == 0) {
            binding.containerLeaderMatch.visibility = View.GONE
            return
        }

        binding.containerLeaderMatch.visibility = View.VISIBLE

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
        sharedPref.edit {
            putInt("player1_wins", 0)
            putInt("player2_wins", 0)
            apply()
        }
        numberOfMatches = 0
        updateMatchCountDisplay()
        updateLeaderDisplay()
        Toast.makeText(this, "Histórico limpo!", Toast.LENGTH_SHORT).show()
    }

    private fun updateMatchCountDisplay() {
        binding.tvNumberOfMatches.text = numberOfMatches.toString()
    }
}