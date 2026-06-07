package com.example.mesa12app

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mesa12app.databinding.ActivityHistoricPlayersBinding

class HistoricPlayers : AppCompatActivity() {
    private lateinit var binding: ActivityHistoricPlayersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoricPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupListeners()
        displayPlayerData()
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun displayPlayerData() {
        val sharedPref = getSharedPreferences("Mesa12Prefs", MODE_PRIVATE)
        
        val p1Name = sharedPref.getString("player1_name", "Jogador 1")
        val p2Name = sharedPref.getString("player2_name", "Jogador 2")
        
        // Recupera as vitórias acumuladas
        val p1Wins = sharedPref.getInt("player1_wins", 0)
        val p2Wins = sharedPref.getInt("player2_wins", 0)

        // Atualiza a tela de histórico
        binding.tvPlayer1Name.text = p1Name
        binding.tvPlayer1Wins.text = getString(R.string.wins_label, p1Wins.toString())

        binding.tvPlayer2Name.text = p2Name
        binding.tvPlayer2Wins.text = getString(R.string.wins_label, p2Wins.toString())
    }
}