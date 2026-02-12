package com.collectoslot.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A custom-drawn cherry icon that can be rendered in any color.
 * Draws two cherry fruits with stems joining at a single point, plus a leaf.
 */
@Composable
fun CherryIcon(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    Canvas(modifier = modifier.size(size)) {
        drawCherry(color)
    }
}

private fun DrawScope.drawCherry(color: Color) {
    val w = size.width
    val h = size.height

    // Two cherry spheres
    val leftCenter = Offset(w * 0.32f, h * 0.72f)
    val rightCenter = Offset(w * 0.68f, h * 0.72f)
    val fruitRadius = w * 0.22f

    // Darker shade for depth
    val darkColor = color.copy(
        red = (color.red * 0.6f).coerceIn(0f, 1f),
        green = (color.green * 0.6f).coerceIn(0f, 1f),
        blue = (color.blue * 0.6f).coerceIn(0f, 1f)
    )

    // Stem join point
    val stemTop = Offset(w * 0.50f, h * 0.10f)

    // Draw stems (curved lines from fruit tops to join point)
    val stemColor = Color(0xFF4E3524)
    val stemStroke = Stroke(width = w * 0.04f, cap = StrokeCap.Round)

    // Left stem
    val leftStemPath = Path().apply {
        moveTo(leftCenter.x, leftCenter.y - fruitRadius * 0.8f)
        cubicTo(
            leftCenter.x - w * 0.05f, h * 0.35f,
            stemTop.x - w * 0.12f, h * 0.20f,
            stemTop.x, stemTop.y
        )
    }
    drawPath(leftStemPath, stemColor, style = stemStroke)

    // Right stem
    val rightStemPath = Path().apply {
        moveTo(rightCenter.x, rightCenter.y - fruitRadius * 0.8f)
        cubicTo(
            rightCenter.x + w * 0.05f, h * 0.35f,
            stemTop.x + w * 0.12f, h * 0.20f,
            stemTop.x, stemTop.y
        )
    }
    drawPath(rightStemPath, stemColor, style = stemStroke)

    // Leaf
    val leafColor = Color(0xFF2E7D32)
    val leafPath = Path().apply {
        moveTo(stemTop.x, stemTop.y + h * 0.02f)
        cubicTo(
            stemTop.x + w * 0.20f, stemTop.y - h * 0.05f,
            stemTop.x + w * 0.28f, stemTop.y + h * 0.08f,
            stemTop.x + w * 0.10f, stemTop.y + h * 0.14f
        )
        cubicTo(
            stemTop.x + w * 0.18f, stemTop.y + h * 0.06f,
            stemTop.x + w * 0.10f, stemTop.y + h * 0.01f,
            stemTop.x, stemTop.y + h * 0.02f
        )
    }
    drawPath(leafPath, leafColor, style = Fill)

    // Left cherry fruit (main body)
    drawCircle(color, radius = fruitRadius, center = leftCenter)
    // Highlight
    drawOval(
        color = Color.White.copy(alpha = 0.35f),
        topLeft = Offset(leftCenter.x - fruitRadius * 0.55f, leftCenter.y - fruitRadius * 0.65f),
        size = Size(fruitRadius * 0.5f, fruitRadius * 0.4f)
    )
    // Shadow edge
    drawArc(
        color = darkColor.copy(alpha = 0.4f),
        startAngle = 30f,
        sweepAngle = 150f,
        useCenter = false,
        topLeft = Offset(leftCenter.x - fruitRadius, leftCenter.y - fruitRadius),
        size = Size(fruitRadius * 2, fruitRadius * 2),
        style = Stroke(width = fruitRadius * 0.15f)
    )

    // Right cherry fruit (main body)
    drawCircle(color, radius = fruitRadius, center = rightCenter)
    // Highlight
    drawOval(
        color = Color.White.copy(alpha = 0.35f),
        topLeft = Offset(rightCenter.x - fruitRadius * 0.55f, rightCenter.y - fruitRadius * 0.65f),
        size = Size(fruitRadius * 0.5f, fruitRadius * 0.4f)
    )
    // Shadow edge
    drawArc(
        color = darkColor.copy(alpha = 0.4f),
        startAngle = 30f,
        sweepAngle = 150f,
        useCenter = false,
        topLeft = Offset(rightCenter.x - fruitRadius, rightCenter.y - fruitRadius),
        size = Size(fruitRadius * 2, fruitRadius * 2),
        style = Stroke(width = fruitRadius * 0.15f)
    )
}
