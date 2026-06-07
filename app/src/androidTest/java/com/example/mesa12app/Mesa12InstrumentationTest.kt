package com.example.mesa12app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Testes de Instrumentação (UI Tests)
 * Estes testes rodam num dispositivo real ou emulador e interagem com a ‘interface’.
 */
@RunWith(AndroidJUnit4::class)
class Mesa12InstrumentationTest {

    // Regra que lança a MainActivity antes de cada teste
    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun testSetNameFlow() {
        // 1. Clica no botão de definir nomes
        onView(withId(R.id.btn_setNamePlayers)).perform(click())

        // 2. Digita os nomes dos jogadores
        onView(withId(R.id.et_player1)).perform(typeText("Carlos"), closeSoftKeyboard())
        onView(withId(R.id.et_player2)).perform(typeText("Ana"), closeSoftKeyboard())

        // 3. Clica em salvar
        onView(withId(R.id.btn_save)).perform(click())

        // 4. Verifica se os nomes foram atualizados na MainActivity
        // Como tv_playerNameValue está em um include, usamos isDescendantOfA
        onView(allOf(withId(R.id.tv_playerNameValue), isDescendantOfA(withId(R.id.player1Section))))
            .check(matches(withText("Carlos")))
            
        onView(allOf(withId(R.id.tv_playerNameValue), isDescendantOfA(withId(R.id.player2Section))))
            .check(matches(withText("Ana")))
    }

    @Test
    fun testPointIncrementAndWin() {
        // Adiciona 12 pontos para o jogador 1 (clicando no botão +12 do include)
        onView(allOf(withId(R.id.bt_win12), isDescendantOfA(withId(R.id.player1Section))))
            .perform(click())

        // Verifica se o contador de partidas subiu para 1
        onView(withId(R.id.tv_numberOfMatches)).check(matches(withText("1")))
        
        // Verifica se o ‘banner’ de liderança apareceu informando que o Jogador 1 (ou nome padrão) lidera
        onView(withId(R.id.tv_lider_match)).check(matches(isDisplayed()))
    }

    @Test
    fun testNavigationToHistory() {
        // Clica no botão de histórico
        onView(withId(R.id.btn_historicOfMatches)).perform(click())

        // Verifica se o título da tela de histórico está visível
        onView(withId(R.id.tv_title)).check(matches(withText(R.string.title_historic)))
        
        // Clica no botão voltar
        onView(withId(R.id.btn_back)).perform(click())
        
        // Verifica se voltou para a MainActivity (procurando o título de Matches)
        onView(withId(R.id.tv_match)).check(matches(isDisplayed()))
    }
}