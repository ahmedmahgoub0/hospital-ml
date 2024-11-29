package com.acoding.hospital.ui.bio

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acoding.hospital.R
import com.acoding.hospital.helpers.convertToEpochMillis
import com.acoding.hospital.helpers.getValueAfterSlash
import com.acoding.hospital.helpers.getValueBeforeSlash
import com.acoding.hospital.ui.home.HomeListState
import com.acoding.hospital.ui.theme.Inter
import ir.ehsannarmani.compose_charts.models.AnimationMode
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
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
import java.time.format.DateTimeFormatter

@Composable
fun SugarGraph(
    type: GraphType,
    date: String,
    state: HomeListState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val sugarDataPoints by remember { mutableStateOf(state.sugarDataPoints) }
    val sugarDatePoints = state.sugarDatePoints

    val filteredSugarDataPoints = state.patientBio.filter {
        it.date == date
    }.sortedBy { it.time }.map { value -> value.bloodSugar.toDouble() }

    val filteredDatePoints = state.patientBio.filter {
        it.date == date
    }.sortedBy { it.time }.map { value ->
        val epochMillis = convertToEpochMillis(value.date, value.time)
        println("Epoch for ${value.date}, ${value.time}: $epochMillis") // Debugging
        DateTimeFormatter.ofPattern("ha").format(epochMillis)
    }

    if (filteredSugarDataPoints.isEmpty())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_data_assigned_at_this_day)
            )
        }
    else {
        when (type) {
            GraphType.Curved -> {
                ir.ehsannarmani.compose_charts.LineChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 40).dp)
                        .padding(bottom = 36.dp),
                    data =
                    listOf(
                        Line(
                            label = context.getString(R.string.sugar),
                            values = filteredSugarDataPoints,
                            color = SolidColor(Color(0xFF247CFF)),
                            firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        )
                    ),
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
                            lineCount = (filteredDatePoints.size) % 25
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
                        labels = filteredDatePoints
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
                            "%.1f".format(value) + " ${context.getString(R.string.mg_dl)}"
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
//        maxValue = sugarDataPoints.max(),
//        minValue = sugarDataPoints.min(),
                )
            }

            GraphType.Line -> {
                ir.ehsannarmani.compose_charts.LineChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 40).dp)
                        .padding(bottom = 36.dp),
                    curvedEdges = false,
                    data =
                    listOf(
                        Line(
                            label = context.getString(R.string.sugar),
                            values = filteredSugarDataPoints,
                            color = SolidColor(Color(0xFF247CFF)),
                            firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        )
                    ),
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
                            lineCount = (filteredDatePoints.size) % 25
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
                        labels = filteredDatePoints
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
                            "%.1f".format(value) + " ${context.getString(R.string.mg_dl)}"
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
//        maxValue = sugarDataPoints.max(),
//        minValue = sugarDataPoints.min(),
                )
            }

            GraphType.Bar -> {
                ir.ehsannarmani.compose_charts.ColumnChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 40).dp)
                        .padding(bottom = 36.dp),
                    data = toBars(
                        values = filteredSugarDataPoints,
                        labels = filteredDatePoints,
                        label1 = stringResource(R.string.sugar)

                    ),
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
                            lineCount = (filteredDatePoints.size) % 25
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
                        labels = filteredDatePoints
                    ),
                    popupProperties = PopupProperties(
                        enabled = true,
                        animationSpec = tween(0),
                        duration = 2000L,
                        textStyle = MaterialTheme.typography.labelMedium,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        cornerRadius = 8.dp,
                        contentHorizontalPadding = 4.dp,
                        contentVerticalPadding = 2.dp,
                        contentBuilder = { value ->
                            "%.1f".format(value) + " ${context.getString(R.string.mg_dl)}"
                        }
                    ),
                    barProperties = BarProperties(
                        cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                        spacing = 3.dp,
                        thickness = 10.dp
                    ),
//        maxValue = sugarDataPoints.max(),
//        minValue = sugarDataPoints.min(),
                )
            }
        }
    }

}

@Composable
fun TemperatureGraph(
    type: GraphType,
    date: String,
    state: HomeListState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val filteredTempDataPoints = state.patientBio.filter {
        it.date == date
    }.sortedBy { it.time }.map { value -> value.averageTemperature.toDouble() }

    val filteredDatePoints = state.patientBio.filter {
        it.date == date
    }.sortedBy { it.time }.map { value ->
        val epochMillis = convertToEpochMillis(value.date, value.time)
        println("Epoch for ${value.date}, ${value.time}: $epochMillis") // Debugging
        DateTimeFormatter.ofPattern("ha").format(epochMillis)
    }

    if (filteredTempDataPoints.isEmpty())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data assigned at this day"
            )
        }
    else {
        when (type) {
            GraphType.Curved -> {
                ir.ehsannarmani.compose_charts.LineChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 34).dp)
                        .padding(bottom = 36.dp),
                    data = listOf(
                        Line(
                            label = context.getString(R.string.temperature),
                            values = filteredTempDataPoints,
                            color = SolidColor(Color(0xFF247CFF)),
                            firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        )
                    ),
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
                            lineCount = filteredDatePoints.size % 25
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
                        labels = filteredDatePoints
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
                    maxValue = filteredTempDataPoints.max().plus(2),
                    minValue = filteredTempDataPoints.min().minus(2),
                )
            }

            GraphType.Line -> {
                ir.ehsannarmani.compose_charts.LineChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 34).dp)
                        .padding(bottom = 36.dp),
                    curvedEdges = false,
                    data = listOf(
                        Line(
                            label = context.getString(R.string.temperature),
                            values = filteredTempDataPoints,
                            color = SolidColor(Color(0xFF247CFF)),
                            firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        )
                    ),
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
                            lineCount = filteredDatePoints.size % 25
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
                        labels = filteredDatePoints
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
                    maxValue = filteredTempDataPoints.max().plus(2),
                    minValue = filteredTempDataPoints.min().minus(2),
                )
            }

            GraphType.Bar -> {
                ir.ehsannarmani.compose_charts.ColumnChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 34).dp)
                        .padding(bottom = 36.dp),
                    data = toBars(
                        values = filteredTempDataPoints,
                        labels = filteredDatePoints,
                        label1 = stringResource(R.string.temperature)
                    ),
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
                            lineCount = filteredDatePoints.size % 25
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
                        labels = filteredDatePoints
                    ),
                    popupProperties = PopupProperties(
                        enabled = true,
                        animationSpec = tween(0),
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
                    barProperties = BarProperties(
                        cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                        spacing = 3.dp,
                        thickness = 10.dp
                    ),
                    maxValue = filteredTempDataPoints.max().plus(2),
//                    minValue = filteredTempDataPoints.min().minus(50),
                )
            }
        }
    }
}

@Composable
fun PressureGraph(
    type: GraphType,
    date: String,
    state: HomeListState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val filteredPressHighDataPoints = state.patientBio.filter {
        it.date == date
    }.sortedBy { it.time }.map { value -> value.bloodPressure.getValueBeforeSlash().toDouble() }

    val filteredPressLowDataPoints = state.patientBio.filter {
        it.date == date
    }.sortedBy { it.time }.map { value -> value.bloodPressure.getValueAfterSlash().toDouble() }

    val filteredDatePoints = state.patientBio.filter {
        it.date == date
    }.sortedBy { it.time }.map { value ->
        val epochMillis = convertToEpochMillis(value.date, value.time)
        println("Epoch for ${value.date}, ${value.time}: $epochMillis") // Debugging
        DateTimeFormatter.ofPattern("ha").format(epochMillis)
    }

    if (filteredPressHighDataPoints.isEmpty())
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data assigned at this day"
            )
        }
    else {
        when (type) {
            GraphType.Curved -> {
                ir.ehsannarmani.compose_charts.LineChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 40).dp)
                        .padding(bottom = 36.dp),
                    data =
                    listOf(
                        Line(
                            label = context.getString(R.string.pressure_high),
                            values = filteredPressHighDataPoints,
                            color = SolidColor(Color(0xFF247CFF)),
                            firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        ),
                        Line(
                            label = context.getString(R.string.pressure_low),
                            values = filteredPressLowDataPoints,
                            color = SolidColor(Color(0xFF5E3ADB)),
                            firstGradientFillColor = Color(0xFF5E3ADB).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        ),
                    ),
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
                            lineCount = filteredDatePoints.size % 25
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
                        labels = filteredDatePoints
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
                            "%.1f".format(value) + " ${context.getString(R.string.mm_hg)}"
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
//        maxValue = pressHighDataPoints.max().plus(5),
//        minValue = pressLowDataPoints.min().minus(5),
                )
            }

            GraphType.Line -> {
                ir.ehsannarmani.compose_charts.LineChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 40).dp)
                        .padding(bottom = 36.dp),
                    curvedEdges = false,
                    data = listOf(
                        Line(
                            label = context.getString(R.string.pressure_high),
                            values = filteredPressHighDataPoints,
                            color = SolidColor(Color(0xFF247CFF)),
                            firstGradientFillColor = Color(0xFF247CFF).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        ),
                        Line(
                            label = context.getString(R.string.pressure_low),
                            values = filteredPressLowDataPoints,
                            color = SolidColor(Color(0xFF5E3ADB)),
                            firstGradientFillColor = Color(0xFF5E3ADB).copy(alpha = .5f),
                            secondGradientFillColor = Color.Transparent,
                            strokeAnimationSpec = tween(1000, easing = EaseInOutCubic),
                            gradientAnimationDelay = 1000,
                            drawStyle = Stroke(width = 2.dp),
                        ),
                    ),
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
                            lineCount = filteredDatePoints.size % 25
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
                        labels = filteredDatePoints
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
                            "%.1f".format(value) + " ${context.getString(R.string.mm_hg)}"
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
//        maxValue = pressHighDataPoints.max().plus(5),
//        minValue = pressLowDataPoints.min().minus(5),
                )
            }

            GraphType.Bar -> {
                ir.ehsannarmani.compose_charts.ColumnChart(
                    labelHelperProperties = LabelHelperProperties(
                        enabled = true, textStyle = MaterialTheme.typography.labelMedium
                    ),
                    modifier = modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(16.dp)
                        .height(400.dp)
                        .width((filteredDatePoints.size * 40).dp)
                        .padding(bottom = 36.dp),
                    data = toBarsPressure(
                        values1 = filteredPressHighDataPoints,
                        values2 = filteredPressLowDataPoints,
                        labels = filteredDatePoints,
                        label1 = stringResource(R.string.pressure_high),
                        label2 = stringResource(R.string.pressure_low)
                    ),
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
                            lineCount = filteredDatePoints.size % 25
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
                        labels = filteredDatePoints
                    ),
                    popupProperties = PopupProperties(
                        enabled = true,
                        animationSpec = tween(0),
                        duration = 2000L,
                        textStyle = MaterialTheme.typography.labelMedium,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        cornerRadius = 8.dp,
                        contentHorizontalPadding = 4.dp,
                        contentVerticalPadding = 2.dp,
                        contentBuilder = { value ->
                            "%.1f".format(value) + " ${context.getString(R.string.mm_hg)}"
                        }
                    ),
                    barProperties = BarProperties(
                        cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
                        spacing = 3.dp,
                        thickness = 8.dp
                    ),
//        maxValue = pressHighDataPoints.max().plus(5),
//        minValue = pressLowDataPoints.min().minus(5),
                )
            }
        }
    }
}

fun toBars(values: List<Double>, labels: List<String>, label1: String): List<Bars> {
    val list = mutableListOf<Bars>()
    for (i in values.indices) {
        list.add(
            Bars(
                label = labels[i],
                values = listOf(
                    Bars.Data(
                        label = label1,
                        value = values[i],
                        color = SolidColor(Color(0xFF247CFF)),
                    )
                )
            )
        )
    }

    return list
}

fun toBarsPressure(
    values1: List<Double>,
    values2: List<Double>,
    labels: List<String>,
    label1: String,
    label2: String
): List<Bars> {
    val list = mutableListOf<Bars>()
    for (i in values1.indices) {
        list.add(
            Bars(
                label = labels[i],
                values = listOf(
                    Bars.Data(
                        label = label1,
                        value = values1[i],
                        color = SolidColor(Color(0xFF247CFF)),
                    ),
                    Bars.Data(
                        label = label2,
                        value = values2[i],
                        color = SolidColor(Color(0xFF5E3ADB)),
                    )
                )
            )
        )
    }

    return list
}