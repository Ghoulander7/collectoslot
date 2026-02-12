package com.collectoslot.model

/**
 * Tracks which symbols the player has collected.
 * A symbol is collected when it lands as a 3-of-a-kind on any payline.
 */
data class Collection(
    val collected: Set<Symbol> = emptySet()
) {
    /** Check if a specific symbol has been collected. */
    fun hasCollected(symbol: Symbol): Boolean = symbol in collected

    /** Check if an entire category is complete. */
    fun isCategoryComplete(category: String): Boolean {
        val categorySymbols = Symbol.forCategory(category)
        return categorySymbols.all { it in collected }
    }

    /** Get collected symbols for a category. */
    fun collectedInCategory(category: String): Set<Symbol> =
        collected.filter { it.category == category }.toSet()

    /** Get progress for a category as collected/total. */
    fun categoryProgress(category: String): Pair<Int, Int> {
        val total = Symbol.forCategory(category).size
        val done = collectedInCategory(category).size
        return done to total
    }

    /** Add a symbol to the collection. Returns new Collection. */
    fun collect(symbol: Symbol): Collection =
        copy(collected = collected + symbol)
}
