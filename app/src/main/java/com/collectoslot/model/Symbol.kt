package com.collectoslot.model

/**
 * Slot symbols - currently all cherry color variants.
 * Each symbol belongs to a category for the collection system.
 * Equal weight (10 each) on the reels for fair distribution.
 */
enum class Symbol(
    val displayChar: String,
    val displayName: String,
    val category: String,
    val basePayout3x: Int,
    val weight: Int
) {
    GREEN_CHERRY("\uD83C\uDF52", "Green Cherry", "Cherries", 10, 10),
    RED_CHERRY("\uD83C\uDF52", "Red Cherry", "Cherries", 10, 10),
    BLUE_CHERRY("\uD83C\uDF52", "Blue Cherry", "Cherries", 10, 10),
    WHITE_CHERRY("\uD83C\uDF52", "White Cherry", "Cherries", 10, 10);

    /** Color tint used to distinguish cherry variants on the reels. */
    val tintDescription: String get() = when (this) {
        GREEN_CHERRY -> "green"
        RED_CHERRY -> "red"
        BLUE_CHERRY -> "blue"
        WHITE_CHERRY -> "white"
    }

    companion object {
        /** Build a weighted pool for random reel strip generation. */
        fun weightedPool(): List<Symbol> {
            return entries.flatMap { symbol ->
                List(symbol.weight) { symbol }
            }
        }

        /** All categories in the game. */
        val categories: List<String> get() = entries.map { it.category }.distinct()

        /** Get all symbols belonging to a category. */
        fun forCategory(category: String): List<Symbol> =
            entries.filter { it.category == category }
    }
}
