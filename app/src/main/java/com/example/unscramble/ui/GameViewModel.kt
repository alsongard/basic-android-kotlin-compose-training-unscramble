package com.example.unscramble.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import kotlinx.coroutines.flow.update

class GameViewModel: ViewModel()
{
    private val _uiState = MutableStateFlow(GameUiState()) // the gameUIState is a data class with a multiple attributes: currentScrambledWord:String that declared with val:immutable has no setter function : **NOTE** this variable is only accessible in it's own class: GameViewModel
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow() // this is accessable in other classes : default modifier :public : this variable is used as it can be accessed in other classes. It is a read-only view : that is it can only be read by the UI : GameScreen and not modified
    private lateinit var currentWord: String // gets the current Scramble word
    private var usedWords:  MutableSet<String> = mutableSetOf("")

    init {
        resetGame()
    }
    var userGuess by mutableStateOf("") // variable for storing user input text
        private set
    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }


    private fun pickRandomWordAndShuffle(): String
    {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if (usedWords.contains(currentWord))
        {
            return pickRandomWordAndShuffle()
        } else {
                usedWords.add(currentWord)
                return shuffleCurrentWord(currentWord)
            }
    }
    fun updateUserGuess(guessedWord:String) // function to be passed to : onValueChange() for OutLinedText composable
    {
        userGuess = guessedWord
    }

    fun checkUserGuess()
    {
        if (userGuess.equals(currentWord, ignoreCase = true))
        {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        }
        else
        {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWrongWrong = true)
            }
        }
        updateUserGuess((""))

    }

    private fun updateGameState(updatedScore: Int)
    {
        if (usedWords.size == MAX_NO_OF_WORDS)
        {
            // Last round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWrongWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }
        else
        {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWrongWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    score =  updatedScore,
                    currentWordCount = currentState.currentWordCount.inc()
                )
            }
        }
    }

    fun skipWord()
    {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }




}





/*
Backing property

A backing property lets you return something from a getter other than the exact object.

For var property, the Kotlin framework generates getters and setters.

For getter and setter methods, you can override one or both of these methods and provide your own custom behavior. To implement a backing property, you override the getter method to return a read-only version of your data. The following example shows a backing property:
```kotlin
//Example code, no need to copy over

// Declare private mutable variable that can only be modified
// within the class it is declared.
private var _count = 0

// Declare another public immutable field and override its getter method.
// Return the private property's value in the getter method.
// When count is accessed, the get() function is called and
// the value of _count is returned.
val count: Int
    get() = _count
```
in the above example:
_count is var: has both getter(access backing field): return field and setter
count is val: has only getter

Note on copy() method: Use the copy() function to copy an object, allowing you to alter some of its properties while keeping the rest unchanged.
Example:
val jack = User(name = "Jack", age = 1)
val olderJack = jack.copy(age = 2)



Next, you pass the event callback checkUserGuess() up from GameScreen to ViewModel when the user clicks the Submit button or the done key in the keyboard. Pass the data, gameUiState.isGuessedWordWrong down from the ViewModel to the GameScreen to set the error in the text field.

 */