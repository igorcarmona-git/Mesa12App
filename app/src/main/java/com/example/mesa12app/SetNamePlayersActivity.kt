package com.example.mesa12app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mesa12app.databinding.ActivityPlayersNameBinding

class SetNamePlayersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayersNameBinding

    companion object {
        const val EXTRA_PLAYER1_NAME = "extra_player1_name"
        const val EXTRA_PLAYER2_NAME = "extra_player2_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayersNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainSetName) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val name1 = binding.etPlayer1.text.toString()
            val name2 = binding.etPlayer2.text.toString()

            if (name1.isNotBlank() && name2.isNotBlank()) {
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_PLAYER1_NAME, name1)
                    putExtra(EXTRA_PLAYER2_NAME, name2)
                }
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                if (name1.isBlank()) binding.tilPlayer1.error = "Nome obrigatório"
                if (name2.isBlank()) binding.tilPlayer2.error = "Nome obrigatório"
            }
        }
    }

}