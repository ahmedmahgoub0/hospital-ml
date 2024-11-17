package com.acoding.hospital.ui.bio
//
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.gestures.detectHorizontalDragGestures
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.LocalTextStyle
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.Path
//import androidx.compose.ui.graphics.StrokeCap
//import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.text.drawText
//import androidx.compose.ui.text.rememberTextMeasurer
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.TextUnit
//
//@Composable
//fun MultiLineChart(
//    dataPointsList: List<List<DataPoint>>,
//    style: ChartStyle,
//    visibleDataPointsIndices: IntRange,
//    unit: String,
//    modifier: Modifier = Modifier,
//    selectedDataPoints: List<DataPoint?> = listOf(),
//    onSelectedDataPoints: (List<DataPoint?>) -> Unit = {},
//    onXLabelWidthChange: (Float) -> Unit = {},
//    showHelperLines: Boolean = true
//) {
//    val textStyle = LocalTextStyle.current.copy(
//        fontSize = style.labelFontSize
//    )
//
//    val measurer = rememberTextMeasurer()
//
//    val maxYValue = remember(dataPointsList) {
//        dataPointsList.flatten().maxOfOrNull { it.y } ?: 0f
//    }
//    val minYValue = remember(dataPointsList) {
//        dataPointsList.flatten().minOfOrNull { it.y } ?: 0f
//    }
//
//    val visibleDataPoints = remember(dataPointsList, visibleDataPointsIndices) {
//        dataPointsList.map { it.slice(visibleDataPointsIndices) }
//    }
//
//    val xLabelTextLayoutResultsList = visibleDataPoints.map { dataPoints ->
//        dataPoints.map {
//            measurer.measure(
//                text = it.xLabel,
//                style = textStyle.copy(textAlign = TextAlign.Center)
//            )
//        }
//    }
//
//    val maxXLabelWidth = xLabelTextLayoutResultsList.flatten().maxOfOrNull { it.size.width } ?: 0
//
//    var xLabelWidth by remember { mutableFloatStateOf(0f) }
//    LaunchedEffect(key1 = xLabelWidth) {
//        onXLabelWidthChange(xLabelWidth)
//    }
//
//    var drawPointsList by remember {
//        mutableStateOf(List(dataPointsList.size) { listOf<DataPoint>() })
//    }
//
//    Canvas(
//        modifier = modifier
//            .fillMaxSize()
//            .pointerInput(drawPointsList, xLabelWidth) {
//                detectHorizontalDragGestures { change, _ ->
//                    val newSelectedDataPoints = drawPointsList.mapIndexed { index, drawPoints ->
//                        val newIndex = getSelectedDataPointIndex(
//                            touchOffsetX = change.position.x,
//                            triggerWidth = xLabelWidth,
//                            drawPoints = drawPoints
//                        )
//                        if ((newIndex + visibleDataPointsIndices.first) in visibleDataPointsIndices) {
//                            visibleDataPoints[index].getOrNull(newIndex)
//                        } else null
//                    }
//                    onSelectedDataPoints(newSelectedDataPoints)
//                }
//            }
//    ) {
//        val verticalPaddingPx = style.verticalPadding.toPx()
//        val horizontalPaddingPx = style.horizontalPadding.toPx()
//        val xAxisLabelSpacingPx = style.xAxisLabelSpacing.toPx()
//
//        val maxXLabelHeight =
//            xLabelTextLayoutResultsList.flatten().maxOfOrNull { it.size.height } ?: 0
//        val xLabelLineHeight = if (maxXLabelHeight > 0) maxXLabelHeight else 0
//
//        val viewPortHeightPx =
//            size.height - (maxXLabelHeight + 2 * verticalPaddingPx + xLabelLineHeight + xAxisLabelSpacingPx)
//        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f
//        val viewPortBottomY = viewPortTopY + viewPortHeightPx
//        val viewPortLeftX = 2f * horizontalPaddingPx
//        val viewPortRightX = size.width
//
//        xLabelWidth = maxXLabelWidth + xAxisLabelSpacingPx
//
//        drawPointsList = visibleDataPoints.map { visiblePoints ->
//            visiblePoints.map {
//                val x =
//                    viewPortLeftX + (it.y - visibleDataPointsIndices.first) * xLabelWidth + xLabelWidth / 2f
//                val ratio = (it.y - minYValue) / (maxYValue - minYValue)
//                val y = viewPortBottomY - (ratio * viewPortHeightPx)
//                DataPoint(x = x, y = y, xLabel = it.xLabel)
//            }
//        }
//
//        drawPointsList.forEachIndexed { index, drawPoints ->
//            val path = Path().apply {
//                if (drawPoints.isNotEmpty()) {
//                    moveTo(drawPoints.first().x, drawPoints.first().y)
//                    for (i in 1 until drawPoints.size) {
//                        val p0 = drawPoints[i - 1]
//                        val p1 = drawPoints[i]
//                        cubicTo(
//                            x1 = (p0.x + p1.x) / 2f, y1 = p0.y,
//                            x2 = (p0.x + p1.x) / 2f, y2 = p1.y,
//                            x3 = p1.x, y3 = p1.y
//                        )
//                    }
//                }
//            }
//            val chartColors = listOf(Color.Blue, Color.Green)
//
//            val color = chartColors.getOrElse(index) { Color.Gray }
//            drawPath(
//                path = path,
//                color = color,
//                style = Stroke(width = 5f, cap = StrokeCap.Round)
//            )
//
//            drawPoints.forEach { point ->
//                drawCircle(
//                    color = color,
//                    radius = 10f,
//                    center = Offset(point.x, point.y)
//                )
//            }
//        }
//
//        xLabelTextLayoutResultsList.forEachIndexed { dataSetIndex, textLayoutResults ->
//            textLayoutResults.forEachIndexed { index, result ->
//                val x = viewPortLeftX + xAxisLabelSpacingPx / 2f + xLabelWidth * index
//                drawText(
//                    textLayoutResult = result,
//                    topLeft = Offset(
//                        x = x,
//                        y = viewPortBottomY + xAxisLabelSpacingPx
//                    ),
//                    color = style.unselectedColor
//                )
//            }
//        }
//    }
//}
//
//private fun getSelectedDataPointIndex(
//    touchOffsetX: Float,
//    triggerWidth: Float,
//    drawPoints: List<DataPoint>
//): Int {
//    val triggerRangeLeft = touchOffsetX - triggerWidth / 2f
//    val triggerRangeRight = touchOffsetX + triggerWidth / 2f
//    return drawPoints.indexOfFirst { it.x in triggerRangeLeft..triggerRangeRight }
//}
//
//
//data class ChartStyle2(
//    val labelFontSize: TextUnit,
//    val selectedColor: Color,
//    val unselectedColor: Color,
//    val chartColors: List<Color>,
//    val verticalPadding: Dp,
//    val horizontalPadding: Dp,
//    val xAxisLabelSpacing: Dp,
//    val helperLinesThicknessPx: Float
//)
