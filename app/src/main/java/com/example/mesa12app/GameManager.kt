package com.example.mesa12app

class GameManager {
    var player1Points = 0
    var player2Points = 0
    val winThreshold = 12

    fun addPoints(playerNum: Int, points: Int): Boolean {
        if (playerNum == 1) {
            player1Points += points
            return player1Points >= winThreshold
        } else {
            player2Points += points
            return player2Points >= winThreshold
        }
    }

    fun resetPoints() {
        player1Points = 0
        player2Points = 0
    }
}