package com.example.mesa12app

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Testes Unitários para a lógica do jogo (GameManager).
 * Estes testes rodam rapidamente no seu computador sem precisar de um celular.
 */
class GameManagerTest {

    private lateinit var gameManager: GameManager

    @Before
    fun setup() {
        gameManager = GameManager()
    }

    @Test
    fun addPoints_shouldIncrementScore() {
        gameManager.addPoints(1, 3)
        assertEquals(3, gameManager.player1Points)
    }

    @Test
    fun addPoints_shouldReturnTrue_whenThresholdReached() {
        // Jogador ganha ao atingir 12
        val isWin = gameManager.addPoints(1, 12)
        assertTrue(isWin)
    }

    @Test
    fun addPoints_shouldReturnFalse_whenThresholdNotReached() {
        val isWin = gameManager.addPoints(2, 9)
        assertFalse(isWin)
    }

    @Test
    fun resetPoints_shouldZeroScores() {
        gameManager.addPoints(1, 5)
        gameManager.addPoints(2, 8)
        gameManager.resetPoints()
        
        assertEquals(0, gameManager.player1Points)
        assertEquals(0, gameManager.player2Points)
    }
}