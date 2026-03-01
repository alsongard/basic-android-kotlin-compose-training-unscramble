package com.example.unscramble.ui.test

import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.ui.GameViewModel
import org.junit.Test
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.data.MAX_NO_OF_WORDS
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import org.junit.Assert.assertTrue


class GameViewModelTest {
    // to begin a test for the GameViewModel: provides uiState for the UI create instance
    private val viewModel = GameViewModel()

    // positive path:Success path
    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset(){
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        // after running the above we then get
        // currentState of ui
        currentGameUiState = viewModel.uiState.value
        // use assert to check process
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }

    /*
    * thingUnderTest_Action2Perform_ResultoFTest
    *
    * Access the current scrambled word: from gameuiState
    * */


    // error path test
    @Test
    fun gameViewModel_IncorrectGuess_ScoreUnchangedAndErrorFlagTrue()
    {
        var currentUiState = viewModel.uiState.value
        // get currentScrambled Word
        // pass random world
        // run updateGuess
        // run checkUserGuess
        // get currentUiState
        // assert False

        currentUiState.currentScrambledWord
        viewModel.updateUserGuess("random")
        viewModel.checkUserGuess() // this accessed updateUserGuess: userGuess var updated on updateUserGuess and checks against unscrambleWord and updates gameUiState
        currentUiState = viewModel.uiState.value


        assertEquals(true, currentUiState.isGuessedWordWrong)
        assertEquals(0, currentUiState.score)
    }

    // To run a test click the play button on the index column


    // boundary path test
    @Test
    fun gameViewModel_Initialization_FirstWordLoaded()
    {
        val gameUiState = viewModel.uiState.value
        val unScrambleWord = getUnscrambledWord(gameUiState.currentScrambledWord)

        // performing the test using Assert
        assertNotEquals(unScrambleWord, gameUiState.currentScrambledWord)


        // assert that word Count starts at 1: why: number of times player can palyer is 10 times: therefore it starts with 1 goes to 2 , increment upto 10 when 10 reset
        assertTrue(gameUiState.currentWordCount==1)

        // assertTrue score is zero:
        assertTrue(gameUiState.score == 0)

        assertTrue(gameUiState.isGuessedWordWrong == false)
    }


    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var expectedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        repeat(MAX_NO_OF_WORDS) {
            expectedScore += SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            // Assert that after each correct answer, score is updated correctly.
            assertEquals(expectedScore, currentGameUiState.score)
        }
        // Assert that after all questions are answered, the current word count is up-to-date.
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        // Assert that after 10 questions are answered, the game is over.
        assertTrue(currentGameUiState.isGameOver)
    }

    @Test
    fun gameViewModel_WordSkipped_ScoreUnchangedAndWordCountIncreased() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        val lastWordCount = currentGameUiState.currentWordCount
        viewModel.skipWord()
        currentGameUiState = viewModel.uiState.value
        // Assert that score remains unchanged after word is skipped.
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
        // Assert that word count is increased by 1 after word is skipped.
        assertEquals(lastWordCount + 1, currentGameUiState.currentWordCount)
    }

}