package com.collectoslot.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.collectoslot.model.Collection
import com.collectoslot.model.Symbol
import com.collectoslot.ui.components.CherryIcon
import com.collectoslot.ui.theme.Chrome
import com.collectoslot.ui.theme.CreditGreen
import com.collectoslot.ui.theme.DarkChrome
import com.collectoslot.ui.theme.DarkGold
import com.collectoslot.ui.theme.Gold
import com.collectoslot.ui.theme.SlotBackground
import com.collectoslot.ui.theme.SlotDarkPurple

@Composable
fun CollectionScreen(
    collection: Collection,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SlotBackground, SlotDarkPurple, SlotBackground)
                )
            )
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(DarkGold, Gold, DarkGold)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "COLLECTION",
                color = SlotBackground,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 4.sp,
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Overall progress
        val totalCollected = collection.collected.size
        val totalSymbols = Symbol.entries.size
        Text(
            text = "Collected: $totalCollected / $totalSymbols",
            color = Gold,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Category sections
        for (category in Symbol.categories) {
            CategorySection(
                category = category,
                collection = collection
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Back button
        Button(
            onClick = onBack,
            modifier = Modifier
                .size(width = 200.dp, height = 52.dp),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Gold.copy(alpha = 0.8f)
            )
        ) {
            Text(
                text = "BACK TO SLOTS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = SlotBackground,
                letterSpacing = 2.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun CategorySection(
    category: String,
    collection: Collection
) {
    val symbols = Symbol.forCategory(category)
    val (collected, total) = collection.categoryProgress(category)
    val isComplete = collection.isCategoryComplete(category)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = SlotDarkPurple,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = if (isComplete) CreditGreen else DarkChrome,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        // Category header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.uppercase(),
                color = if (isComplete) CreditGreen else Gold,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Text(
                text = if (isComplete) "COMPLETE! (3x BONUS)" else "$collected / $total",
                color = if (isComplete) CreditGreen else DarkChrome,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Symbol entries
        symbols.forEach { symbol ->
            val isCollected = collection.hasCollected(symbol)
            SymbolEntry(symbol = symbol, isCollected = isCollected)
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}

@Composable
private fun SymbolEntry(
    symbol: Symbol,
    isCollected: Boolean
) {
    val tintColor = symbolTintColor(symbol)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isCollected) Color(0xFF0A0A15) else Color(0xFF0A0A15).copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = if (isCollected) tintColor.copy(alpha = 0.6f) else DarkChrome.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CherryIcon(
                color = if (isCollected) tintColor else DarkChrome.copy(alpha = 0.3f),
                size = 32.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = if (isCollected) symbol.displayName else "??? ${symbol.displayName}",
                color = if (isCollected) Chrome else DarkChrome.copy(alpha = 0.5f),
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Status
        Text(
            text = if (isCollected) "COLLECTED" else "---",
            color = if (isCollected) CreditGreen else DarkChrome.copy(alpha = 0.3f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
    }
}

/** Get the display tint color for a symbol. */
fun symbolTintColor(symbol: Symbol): Color = when (symbol) {
    Symbol.GREEN_CHERRY -> Color(0xFF00E676)
    Symbol.RED_CHERRY -> Color(0xFFFF1744)
    Symbol.BLUE_CHERRY -> Color(0xFF448AFF)
    Symbol.WHITE_CHERRY -> Color(0xFFE0E0E0)
}
