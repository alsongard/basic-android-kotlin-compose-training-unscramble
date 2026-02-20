package com.example.unscramble.ui


// this data class provides the scambled word
data class GameUiState(
    val currentScrambledWord: String = "",
    val isGuessedWrongWrong: Boolean = false,
    val score:Int = 0,
    val currentWordCount: Int = 0,
    val isGameOver: Boolean = false
)


/*
StateFlow is a data holder observable flow that emits the current state and new state updates.
Its value property reflects the current state value. To update state and send it to the flow, assign a new value to the value property of the MutableStateFlow class.
StateFlow works well with classes that must maintain an observable immutable state.
 */