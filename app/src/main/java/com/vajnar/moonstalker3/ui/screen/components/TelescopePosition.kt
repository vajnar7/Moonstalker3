package com.vajnar.moonstalker3.ui.screen.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun TelescopePosition(
    azimuth: Double,
    altitude: Double,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    val outline = MaterialTheme.colorScheme.outlineVariant
    val azimuthIsValid = azimuth.isFinite()
    val altitudeIsValid = altitude.isFinite()
    val normalizedAzimuth = if (azimuthIsValid) ((azimuth % 360.0) + 360.0) % 360.0 else 0.0
    val clampedAltitude = if (altitudeIsValid) altitude.coerceIn(0.0, 90.0) else 0.0

    ElevatedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Telescope Position",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "AZIMUTH",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                    )
                    CompassGraphic(azimuth = normalizedAzimuth, color = primary, outline = outline)
                    Text(
                        text = if (azimuthIsValid) "${formatAngle(normalizedAzimuth)}°" else "--°",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ALTITUDE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.SemiBold,
                    )
                    AltitudeGraphic(altitude = clampedAltitude, color = primary, outline = outline)
                    Text(
                        text = if (altitudeIsValid) "${formatAngle(clampedAltitude)}°" else "--°",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

@Composable
private fun CompassGraphic(
    azimuth: Double,
    color: androidx.compose.ui.graphics.Color,
    outline: androidx.compose.ui.graphics.Color,
) {
    Box(
        modifier = Modifier.size(112.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(96.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension * 0.43f
            drawCircle(color = outline, radius = radius, center = center, style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx()))

            repeat(8) { tick ->
                val angle = Math.toRadians(tick * 45.0)
                val innerRadius = if (tick % 2 == 0) radius * 0.78f else radius * 0.86f
                drawLine(
                    color = outline,
                    start = Offset(
                        center.x + sin(angle).toFloat() * innerRadius,
                        center.y - cos(angle).toFloat() * innerRadius,
                    ),
                    end = Offset(
                        center.x + sin(angle).toFloat() * radius,
                        center.y - cos(angle).toFloat() * radius,
                    ),
                    strokeWidth = 1.5.dp.toPx(),
                )
            }

            val angle = Math.toRadians(azimuth)
            val pointerEnd = Offset(
                center.x + sin(angle).toFloat() * radius * 0.72f,
                center.y - cos(angle).toFloat() * radius * 0.72f,
            )
            drawLine(
                color = color,
                start = center,
                end = pointerEnd,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round,
            )
            drawCircle(color = color, radius = 4.dp.toPx(), center = center)
        }

        Text("N", modifier = Modifier.align(Alignment.TopCenter), style = MaterialTheme.typography.labelSmall, color = color)
        Text("E", modifier = Modifier.align(Alignment.CenterEnd), style = MaterialTheme.typography.labelSmall, color = color)
        Text("S", modifier = Modifier.align(Alignment.BottomCenter), style = MaterialTheme.typography.labelSmall, color = color)
        Text("W", modifier = Modifier.align(Alignment.CenterStart), style = MaterialTheme.typography.labelSmall, color = color)
    }
}

@Composable
private fun AltitudeGraphic(
    altitude: Double,
    color: androidx.compose.ui.graphics.Color,
    outline: androidx.compose.ui.graphics.Color,
) {
    Canvas(modifier = Modifier.size(width = 112.dp, height = 112.dp)) {
        val base = Offset(size.width * 0.16f, size.height * 0.82f)
        val radius = size.width * 0.72f
        val arcSize = Size(radius * 2f, radius * 2f)
        drawArc(
            color = outline,
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(base.x - radius, base.y - radius),
            size = arcSize,
            style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx()),
        )
        drawLine(
            color = outline,
            start = base,
            end = Offset(size.width * 0.94f, base.y),
            strokeWidth = 2.dp.toPx(),
        )

        val angle = Math.toRadians(altitude)
        val telescopeLength = radius * 0.9f
        val telescopeEnd = Offset(
            base.x + cos(angle).toFloat() * telescopeLength,
            base.y - sin(angle).toFloat() * telescopeLength,
        )
        drawLine(
            color = color,
            start = base,
            end = telescopeEnd,
            strokeWidth = 5.dp.toPx(),
            cap = StrokeCap.Round,
        )
        drawCircle(color = color, radius = 4.dp.toPx(), center = base)
    }
}

private fun formatAngle(angle: Double): String = "%05.1f".format(java.util.Locale.US, angle)
