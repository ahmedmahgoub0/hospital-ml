package com.acoding.hospital.ui.bio

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acoding.hospital.ui.home.HomeListState
import com.acoding.hospital.ui.theme.greenBackground
import java.time.LocalDate

//enum class BioType {
//    Sugar,
//    Pressure,
//    Temperature,
////    Statistics
//}

@Composable
fun BioScreen(
    state: HomeListState,
    filter: (start: LocalDate, end: LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (!isSystemInDarkTheme()) Color.Black else Color.White

//    val chosen = remember { mutableStateOf(BioType.Sugar) }
//    var isExpanded by remember { mutableStateOf(false) }
    val dataPointsList = when (state.tabTypeIndex) {
        0 -> state.sugarDataPoints
        1 -> state.pressureDataPoints
        else -> state.temperatureDataPoints
//        BioType.Statistics -> state.sugarDataPoints
    }

    if (state.detailsLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        val patient = state.selectedPatient


        if (patient != null) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val criticalColor =
                    if (patient.healthStatus < 60) MaterialTheme.colorScheme.error
                    else greenBackground
                val critical = if (patient.healthStatus < 60) "Critical" else "Normal"

                Text(
                    text = patient.name,
                    color = contentColor,
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier
                            .clip(RoundedCornerShape(100f))
                            .background(criticalColor)
                            .padding(horizontal = 8.dp),
                        text = critical,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = "Age",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = "${patient.age} years old",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = contentColor
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 12.dp)
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = "Gender",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = "Male",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = contentColor
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 12.dp)
                )


                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = "Phone",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = patient.phoneNo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = contentColor
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 12.dp)
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = "Address",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.Gray
                )

                Text(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp),
                    text = patient.address,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light,
                    color = contentColor
                )
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 12.dp)
                )

                DateRangePicker(
                    minStartDate = LocalDate.now().minusMonths(3)
                ) { startDate, endDate ->
                    filter(startDate, endDate)
                }

//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp)
//                        .clip(RoundedCornerShape(12.dp))
//                        .clickable { isExpanded = !isExpanded }
//                        .padding(vertical = 12.dp, horizontal = 8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "${chosen.value.name} Graph",
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Light,
//                        color = contentColor,
//                        modifier = Modifier
//                            .weight(1f)
//                    )
//                    Icon(
//                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
//                        contentDescription = null,
//                        modifier = Modifier
//                    )
//                }

//                AnimatedVisibility(
//                    visible = isExpanded,
//                    modifier = Modifier
//                        .background(Color.Transparent)
//                        .padding(horizontal = 16.dp)
//                ) {
//
//                    val scrollState = rememberScrollState()
//                    Column(
//                        modifier = Modifier
//                            .padding(top = 12.dp)
//                            .fillMaxWidth()
//                            .height(180.dp)
//                            .verticalScroll(scrollState)
//                    ) {
//                        BioType.entries.forEach {
//                            Text(
//                                text = it.name,
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Light,
//                                color = contentColor,
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 4.dp, horizontal = 12.dp)
//                                    .clip(MaterialTheme.shapes.medium)
//                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = (0.1f)))
//                                    .clickable {
//                                        chosen.value = it
//                                        isExpanded = false
//                                    }
//                                    .padding(12.dp)
//                            )
//                        }
//                    }
//                }


                AnimatedVisibility(
                    modifier = modifier.padding(horizontal = 6.dp),
                    visible = dataPointsList.isNotEmpty()
                ) {
                    var selectedDataPoint by remember {
                        mutableStateOf<DataPoint?>(null)
                    }
                    var labelWidth by remember {
                        mutableFloatStateOf(0f)
                    }
                    var totalChartWidth by remember {
                        mutableFloatStateOf(0f)
                    }
                    val amountOfVisibleDataPoints = if (labelWidth > 0) {
                        ((totalChartWidth - 2.5 * labelWidth) / labelWidth).toInt()
                    } else {
                        0
                    }
                    val startIndex =
                        (dataPointsList.lastIndex - amountOfVisibleDataPoints)
                            .coerceAtLeast(0)
                    LineChart(
                        dataPoints = dataPointsList,
                        style = ChartStyle(
                            chartLineColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.3f
                            ),
                            selectedColor = MaterialTheme.colorScheme.primary,
                            helperLinesThicknessPx = 5f,
                            axisLinesThicknessPx = 5f,
                            labelFontSize = 14.sp,
                            minYLabelSpacing = 25.dp,
                            verticalPadding = 8.dp,
                            horizontalPadding = 2.dp,
                            xAxisLabelSpacing = 8.dp
                        ),
                        visibleDataPointsIndices = startIndex..dataPointsList.lastIndex,
                        unit = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16 / 14f)
                            .onSizeChanged { totalChartWidth = it.width.toFloat() },
                        selectedDataPoint = selectedDataPoint,
                        onSelectedDataPoint = {
                            selectedDataPoint = it
                        },
                        onXLabelWidthChange = { labelWidth = it }
                    )
                }

            }
        }
    }
}