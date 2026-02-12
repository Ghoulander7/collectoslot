package com.collectoslot.engine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.collectoslot.model.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class Screen { SLOT, COLLECTION }

class SlotViewModel : ViewModel() {

    private val engine = SlotEngine()

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private val _currentScreen = MutableStateFlow(Screen.SLOT)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Reel animation sequences for each reel
    private val _reelSequences = MutableStateFlow<List<List<com.collectoslot.model.Symbol>>>(emptyList())
    val reelSequences: StateFlow<List<List<com.collectoslot.model.Symbol>>> = _reelSequences.asStateFlow()

    private val _spinningReels = MutableStateFlow(listOf(false, false, false))
    val spinningReels: StateFlow<List<Boolean>> = _spinningReels.asStateFlow()

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun spin() {
        val currentState = _state.value
        if (!currentState.canSpin) return

        val bet = currentState.bet

        // Deduct bet
        _state.update {
            it.copy(
                credits = it.credits - bet,
                isSpinning = true,
                wins = emptyList(),
                lastWinTotal = 0,
                message = "Spinning..."
            )
        }

        // Generate final results
        val finalWindows = engine.spin()

        // Generate animation sequences for each reel (more symbols = longer smooth scroll)
        val sequences = finalWindows.mapIndexed { index, window ->
            engine.getSpinSequence(window, extraSymbols = 20 + index * 5)
        }
        _reelSequences.value = sequences

        // Start all reels spinning
        _spinningReels.value = listOf(true, true, true)

        // Stop reels sequentially - timings match the 2s animation in ReelView
        viewModelScope.launch {
            delay(2100)  // Reel 1 finishes its animation
            _spinningReels.value = listOf(false, true, true)
            _state.update { it.copy(reelWindows = listOf(finalWindows[0], it.reelWindows[1], it.reelWindows[2])) }

            delay(500)  // Reel 2 finishes
            _spinningReels.value = listOf(false, false, true)
            _state.update { it.copy(reelWindows = listOf(finalWindows[0], finalWindows[1], it.reelWindows[2])) }

            delay(500)  // Reel 3 finishes
            _spinningReels.value = listOf(false, false, false)
            _state.update { it.copy(reelWindows = finalWindows) }

            delay(200)

            // Evaluate wins using current collection state for bonus calculation
            val currentCollection = _state.value.collection
            val wins = engine.evaluateWins(finalWindows, bet, currentCollection)
            val totalWin = wins.sumOf { it.payout }

            // Add newly collected symbols from 3-of-a-kind wins
            var updatedCollection = currentCollection
            val newSymbols = mutableListOf<com.collectoslot.model.Symbol>()
            for (win in wins) {
                if (win.matchCount == 3 && !updatedCollection.hasCollected(win.symbol)) {
                    updatedCollection = updatedCollection.collect(win.symbol)
                    newSymbols.add(win.symbol)
                }
            }

            val collectionMsg = if (newSymbols.isNotEmpty()) {
                " NEW: ${newSymbols.joinToString { it.displayName }}!"
            } else ""

            val categoryJustCompleted = newSymbols.any { symbol ->
                updatedCollection.isCategoryComplete(symbol.category) &&
                    !currentCollection.isCategoryComplete(symbol.category)
            }

            _state.update {
                it.copy(
                    credits = it.credits + totalWin,
                    isSpinning = false,
                    wins = wins,
                    lastWinTotal = totalWin,
                    collection = updatedCollection,
                    message = when {
                        categoryJustCompleted -> "CATEGORY COMPLETE! 3x bonus active!$collectionMsg"
                        totalWin > bet * 50 -> "JACKPOT! Won $totalWin credits!$collectionMsg"
                        totalWin > 0 -> "Winner! +$totalWin credits!$collectionMsg"
                        it.credits == 0 -> "No credits remaining. Game over!"
                        else -> "No win. Try again!"
                    }
                )
            }
        }
    }

    fun increaseBet() {
        _state.update {
            if (it.canIncreaseBet) {
                it.copy(bet = (it.bet + GameState.BET_STEP).coerceAtMost(GameState.MAX_BET))
            } else it
        }
    }

    fun decreaseBet() {
        _state.update {
            if (it.canDecreaseBet) {
                it.copy(bet = (it.bet - GameState.BET_STEP).coerceAtLeast(GameState.MIN_BET))
            } else it
        }
    }

    fun maxBet() {
        _state.update {
            if (!it.isSpinning) {
                it.copy(bet = GameState.MAX_BET.coerceAtMost(it.credits))
            } else it
        }
    }
}
