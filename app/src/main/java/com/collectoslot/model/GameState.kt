package com.collectoslot.model

data class WinResult(
    val payLine: PayLine,
    val symbol: Symbol,
    val matchCount: Int,
    val payout: Int,
    val newCollection: Boolean = false
)

data class GameState(
    val credits: Int = 1000,
    val bet: Int = 10,
    val reelWindows: List<List<Symbol>> = List(3) { List(3) { Symbol.GREEN_CHERRY } },
    val isSpinning: Boolean = false,
    val wins: List<WinResult> = emptyList(),
    val lastWinTotal: Int = 0,
    val message: String = "Place your bet and spin!",
    val collection: Collection = Collection()
) {
    val canSpin: Boolean get() = !isSpinning && credits >= bet
    val canIncreaseBet: Boolean get() = !isSpinning && bet < credits && bet < MAX_BET
    val canDecreaseBet: Boolean get() = !isSpinning && bet > MIN_BET

    companion object {
        const val MIN_BET = 5
        const val MAX_BET = 100
        const val BET_STEP = 5
        const val COMPLETION_BONUS_MULTIPLIER = 3
    }
}
