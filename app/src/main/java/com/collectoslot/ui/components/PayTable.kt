package com.collectoslot.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.collectoslot.model.Collection
import com.collectoslot.model.Symbol
import com.collectoslot.ui.symbolTintColor
import com.collectoslot.ui.theme.Chrome
import com.collectoslot.ui.theme.CreditGreen
import com.collectoslot.ui.theme.DarkChrome
import com.collectoslot.ui.theme.Gold
import com.collectoslot.ui.theme.SlotDarkPurple

@Composable
fun PayTable(
    collection: Collection,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SlotDarkPurple,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = DarkChrome,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { expanded = !expanded }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (expanded) "PAY TABLE (tap to close)" else "PAY TABLE (tap to open)",
            color = Gold,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        if (expanded) {
            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("SYMBOL", color = DarkChrome, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("x3 PAYOUT", color = DarkChrome, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                Symbol.entries.forEach { symbol ->
                    val categoryComplete = collection.isCategoryComplete(symbol.category)
                    val tintColor = symbolTintColor(symbol)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CherryIcon(color = tintColor, size = 20.dp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = symbol.displayName,
                                color = Chrome,
                                fontSize = 13.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Text(
                            text = if (categoryComplete) "${symbol.basePayout3x * 3}x (3x BONUS)" else "${symbol.basePayout3x}x",
                            color = if (categoryComplete) CreditGreen else Gold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Text(
                    text = "5 PAY LINES: Top, Middle, Bottom, Diag ↘, Diag ↗",
                    color = DarkChrome,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 6.dp)
                )
                Text(
                    text = "Complete a category collection for 3x payout bonus!",
                    color = Gold.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
