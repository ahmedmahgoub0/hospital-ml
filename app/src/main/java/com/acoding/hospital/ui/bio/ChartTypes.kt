package com.acoding.hospital.ui.bio

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acoding.hospital.R
import com.acoding.hospital.ui.home.HomeListState
import com.acoding.hospital.ui.theme.Inter
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.DrawStyle.Stroke
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.IndicatorCount
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties
import ir.ehsannarmani.compose_charts.models.StrokeStyle

@Composable
fun SugarGraph(
    state: HomeListState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ir.ehsannarmani.compose_charts.LineChart(
        labelHelperProperties = LabelHelperProperties(
            enabled = true, textStyle = MaterialTheme.typography.labelMedium
        ),
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(16.dp)
            .height(400.dp)
            .width(800.dp)
            .padding(bottom = 36.dp),
        data = remember {
            listOf(
                Line(
                    label = context.getString(R.string.sugar),
                    values = state.sugarDataPoints,
                    color = SolidColor(Color(0xFF247CFF)),
                    firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = Stroke(width = 2.dp),
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        dividerProperties = DividerProperties(
            enabled = true,
            xAxisProperties = LineProperties(
                enabled = true,
                style = StrokeStyle.Normal,
                color = SolidColor(MaterialTheme.colorScheme.primary),
                thickness = (1).dp,
            ),
            yAxisProperties = LineProperties(
                enabled = true,
                style = StrokeStyle.Normal,
                color = SolidColor(MaterialTheme.colorScheme.primary),
                thickness = (1).dp,
            ),
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                enabled = true,
                style = StrokeStyle.Dashed(
                    intervals = floatArrayOf(5f, 5f)
                ),
                color = Brush.linearGradient(listOf(Color.Gray, Color.Gray)),
                thickness = (0.0).dp,
                lineCount = 9

            ),
            yAxisProperties = GridProperties.AxisProperties(
                enabled = true,
                style = StrokeStyle.Dashed(
                    intervals = floatArrayOf(5f, 5f)
                ),
                color = Brush.linearGradient(listOf(Color.Gray, Color.Gray)),
                thickness = (0.0).dp,
                lineCount = 24
            )
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                color = Color.Black.copy(alpha = 0.6f)
            ),
            count = IndicatorCount.CountBased(9),
            padding = 8.dp,
        ),
        labelProperties = LabelProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(
                fontFamily = Inter,
                textAlign = TextAlign.Center,
                color = Color.Black.copy(alpha = 0.6f),
                lineHeight = 16.0.sp
            ),
            labels = state.datePoints
        ),
        popupProperties = PopupProperties(
            enabled = true,
            animationSpec = tween(3000),
            duration = 2000L,
            textStyle = MaterialTheme.typography.labelMedium,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            cornerRadius = 8.dp,
            contentHorizontalPadding = 4.dp,
            contentVerticalPadding = 2.dp,
            contentBuilder = { value ->
                "%.1f".format(value) + " mm/Hg"
            }
        ),
        dotsProperties = DotProperties(
            enabled = true,
            radius = 4.dp,
            color = SolidColor(Color.Red),
            strokeWidth = 1.dp,
            strokeColor = Brush.linearGradient(listOf(Color.White, Color.White)),
            strokeStyle = StrokeStyle.Normal,
            animationEnabled = true,
            animationSpec = tween(500)
        ),
        maxValue = state.sugarDataPoints.max(),
        minValue = state.sugarDataPoints.min(),
    )
}

@Composable
fun TemperatureGraph(
    state: HomeListState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ir.ehsannarmani.compose_charts.LineChart(
        labelHelperProperties = LabelHelperProperties(
            enabled = true, textStyle = MaterialTheme.typography.labelMedium
        ),
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(16.dp)
            .height(400.dp)
            .width(800.dp)
            .padding(bottom = 36.dp),
        data = remember {
            listOf(
                Line(
                    label = context.getString(R.string.temperature),
                    values = state.temperatureDataPoints,
                    color = SolidColor(Color(0xFF247CFF)),
                    firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = Stroke(width = 2.dp),
                )
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        dividerProperties = DividerProperties(
            enabled = true,
            xAxisProperties = LineProperties(
                enabled = true,
                style = StrokeStyle.Normal,
                color = SolidColor(MaterialTheme.colorScheme.primary),
                thickness = (1).dp,
            ),
            yAxisProperties = LineProperties(
                enabled = true,
                style = StrokeStyle.Normal,
                color = SolidColor(MaterialTheme.colorScheme.primary),
                thickness = (1).dp,
            ),
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                enabled = true,
                style = StrokeStyle.Dashed(
                    intervals = floatArrayOf(5f, 5f)
                ),
                color = Brush.linearGradient(listOf(Color.Gray, Color.Gray)),
                thickness = (0.0).dp,
                lineCount = 9
            ),
            yAxisProperties = GridProperties.AxisProperties(
                enabled = true,
                style = StrokeStyle.Dashed(
                    intervals = floatArrayOf(5f, 5f)
                ),
                color = Brush.linearGradient(listOf(Color.Gray, Color.Gray)),
                thickness = (0.0).dp,
                lineCount = 24
            )
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                color = Color.Black.copy(alpha = 0.6f)
            ),
            // for y labels appear
            count = IndicatorCount.CountBased(9),
            padding = 8.dp,
        ),
        labelProperties = LabelProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(
                fontFamily = Inter,
                textAlign = TextAlign.Center,
                color = Color.Black.copy(alpha = 0.6f),
                lineHeight = 16.0.sp
            ),
            labels = state.datePoints
        ),
        popupProperties = PopupProperties(
            enabled = true,
            animationSpec = tween(3000),
            duration = 2000L,
            textStyle = MaterialTheme.typography.labelMedium,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            cornerRadius = 8.dp,
            contentHorizontalPadding = 4.dp,
            contentVerticalPadding = 2.dp,
            contentBuilder = { value ->
                "%.1f".format(value) + "°"
            }
        ),
        dotsProperties = DotProperties(
            enabled = true,
            radius = 4.dp,
            color = SolidColor(Color.Red),
            strokeWidth = 1.dp,
            strokeColor = Brush.linearGradient(listOf(Color.White, Color.White)),
            strokeStyle = StrokeStyle.Normal,
            animationEnabled = true,
            animationSpec = tween(500)
        ),
        maxValue = state.temperatureDataPoints.max(),
        minValue = state.temperatureDataPoints.min(),
    )
}

@Composable
fun PressureGraph(
    state: HomeListState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ir.ehsannarmani.compose_charts.LineChart(
        labelHelperProperties = LabelHelperProperties(
            enabled = true, textStyle = MaterialTheme.typography.labelMedium
        ),
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(16.dp)
            .height(400.dp)
            .width(800.dp)
            .padding(bottom = 36.dp),
        data = remember {
            listOf(
                Line(
                    label = context.getString(R.string.pressure_high),
                    values = state.pressureHighDataPoints,
                    color = SolidColor(Color(0xFF247CFF)),
                    firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = Stroke(width = 2.dp),
                ),
                Line(
                    label = context.getString(R.string.pressure_low),
                    values = state.pressureLowDataPoints,
                    color = SolidColor(Color(0xFF5E3ADB)),
                    firstGradientFillColor = Color(0xFF5E3ADB).copy(alpha = .5f),
                    secondGradientFillColor = Color.Transparent,
                    strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                    gradientAnimationDelay = 1000,
                    drawStyle = Stroke(width = 2.dp),
                ),
            )
        },
        animationMode = AnimationMode.Together(delayBuilder = {
            it * 500L
        }),
        dividerProperties = DividerProperties(
            enabled = true,
            xAxisProperties = LineProperties(
                enabled = true,
                style = StrokeStyle.Normal,
                color = SolidColor(MaterialTheme.colorScheme.primary),
                thickness = (1).dp,
            ),
            yAxisProperties = LineProperties(
                enabled = true,
                style = StrokeStyle.Normal,
                color = SolidColor(MaterialTheme.colorScheme.primary),
                thickness = (1).dp,
            ),
        ),
        gridProperties = GridProperties(
            enabled = true,
            xAxisProperties = GridProperties.AxisProperties(
                enabled = true,
                style = StrokeStyle.Dashed(
                    intervals = floatArrayOf(5f, 5f)
                ),
                color = Brush.linearGradient(listOf(Color.Gray, Color.Gray)),
                thickness = (0.0).dp,
                lineCount = 9

            ),
            yAxisProperties = GridProperties.AxisProperties(
                enabled = true,
                style = StrokeStyle.Dashed(
                    intervals = floatArrayOf(5f, 5f)
                ),
                color = Brush.linearGradient(listOf(Color.Gray, Color.Gray)),
                thickness = (0.0).dp,
                lineCount = 24
            )
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelMedium.copy(
                color = Color.Black.copy(alpha = 0.6f)
            ),
            count = IndicatorCount.CountBased(9),
            padding = 8.dp,
        ),
        labelProperties = LabelProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.labelSmall.copy(
                fontFamily = Inter,
                textAlign = TextAlign.Center,
                color = Color.Black.copy(alpha = 0.6f),
                lineHeight = 16.0.sp
            ),
            labels = state.datePoints
        ),
        popupProperties = PopupProperties(
            enabled = true,
            animationSpec = tween(3000),
            duration = 2000L,
            textStyle = MaterialTheme.typography.labelMedium,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            cornerRadius = 8.dp,
            contentHorizontalPadding = 4.dp,
            contentVerticalPadding = 2.dp,
            contentBuilder = { value ->
                "%.1f".format(value) + " Mg/DL"
            }
        ),
        dotsProperties = DotProperties(
            enabled = true,
            radius = 3.dp,
            color = SolidColor(Color.Red),
            strokeWidth = 1.dp,
            strokeColor = Brush.linearGradient(listOf(Color.White, Color.White)),
            strokeStyle = StrokeStyle.Normal,
            animationEnabled = true,
            animationSpec = tween(500)
        ),
        maxValue = state.pressureHighDataPoints.max().plus(5),
        minValue = state.pressureLowDataPoints.min().minus(5),
    )
}