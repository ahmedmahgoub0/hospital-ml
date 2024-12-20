package com.acoding.hospital.ui.bio

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acoding.hospital.R
import com.acoding.hospital.data.model.Bio
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.ui.home.HomeListState
import com.acoding.hospital.ui.home.LanguageDialog
import com.acoding.hospital.ui.home.patientToImage
import com.acoding.hospital.ui.theme.Inter
import com.acoding.hospital.ui.theme.LightGray
import com.acoding.hospital.ui.theme.greenBackground
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
//enum class BioType {
//    Sugar,
//    Pressure,
//    Temperature,
////    Statistics
//}
 */
@Composable
fun BioScreen(
    state: HomeListState,
    filter: (date: String) -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    val contentColor = if (!isSystemInDarkTheme()) Color.Black else Color.White
    /**    val chosen = remember { mutableStateOf(BioType.Sugar) }
    //    var isExpanded by remember { mutableStateOf(false) }
    //    val dataPointsList = when (state.tabTypeIndex) {
    //        0 -> state.sugarDataPoints
    //        1 -> state.pressureDataPoints
    //        else -> state.temperatureDataPoints
    ////        BioType.Statistics -> state.sugarDataPoints
    //    }
     */

    if (state.detailsLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (state.patients.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(R.string.no_data_found))
        }
    } else {
        val patient = state.selectedPatient
        if (patient != null) {
            /**            val modelProducer = remember { CartesianChartModelProducer() }
            //            LaunchedEffect(Unit) {
            //                withContext(Dispatchers.Default) {
            //                    modelProducer.runTransaction {
            //                        lineSeries { series(x, x.map { Random.nextFloat() * 6 }) }
            //                    }
            //                }
            //            }
             */

            val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl

            var showDialog by remember { mutableStateOf(false) }
            var selectedDate by remember { mutableStateOf("2024-11-30") }

            if (showDialog) {
                DatePickerDialog(
                    onDismissRequest = { showDialog = false },
                    onDateSelected = { date ->
                        selectedDate = date
                        showDialog = false
                        filter(date)
                    }
                )
            }

            var shouldShowLanguageDialog by remember { mutableStateOf(false) }
            var graphType by remember { mutableStateOf(GraphType.Curved) }

            if (shouldShowLanguageDialog) {
                LanguageDialog(
                    onDismissRequest = {
                        shouldShowLanguageDialog = false
                    }
                ) {
                    Column {
                        Text(
                            text = stringResource(R.string.graph_type),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            modifier = Modifier.padding(16.dp)
                        )
                        GraphType.entries.forEach {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        graphType = it
                                        shouldShowLanguageDialog = false
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = when (it) {
                                        GraphType.Curved -> stringResource(R.string.curved)
                                        GraphType.Line -> stringResource(R.string.line)
                                        GraphType.Bar -> stringResource(R.string.bar)
                                    }
                                )
                                RadioButton(
                                    selected = graphType == it,
                                    onClick = {
                                        graphType = it
                                        shouldShowLanguageDialog = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val criticalColor =
                    if (patient.healthStatus < 60) MaterialTheme.colorScheme.error
                    else greenBackground
                val critical = when (state.tabTypeIndex) {
                    0 -> if (patient.healthStatus < 60) "Critical" else "Normal" // sugar
                    1 -> if (patient.healthStatus < 63) "Critical" else "Normal"  // pressure
                    else -> if (patient.healthStatus < 62) "Critical" else "Normal" // temperature
                }
                val context = LocalContext.current

                /** Box for back button and patient profile title */
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightGray.copy(alpha = 0.5f))
                            .clickable {
                                onBack()
                            }
                            .padding(12.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Icon(
                            modifier = Modifier.align(Alignment.Center),
                            imageVector = if (!isRtl) Icons.Default.ArrowBack else Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }

                    Text(
                        text = stringResource(R.string.patient_profile),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }


                Image(
                    painter = painterResource(patientToImage(patient.id, patient.gender)),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .size(110.dp)
                        .clip(CircleShape)
                        .border(
                            width = 3.dp,
                            color = if (critical == "Normal") Color.Green else Color.Red,
                            shape = CircleShape
                        )
                )

                Text(
                    text = patient.name,
                    color = contentColor,
                    fontSize = 24.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 0.dp)
                )
                /** critical text previous code
                //                Text(
                //                    modifier = modifier
                //                        .clip(RoundedCornerShape(100f))
                //                        .background(criticalColor)
                //                        .padding(horizontal = 8.dp),
                //                    text = critical,
                //                    color = Color.White,
                //                    fontSize = 12.sp,
                //                    fontWeight = FontWeight.Medium
                //                )
                 */

                /** row for patient code and age*/
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = patient.code,
                        color = contentColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightGray.copy(alpha = 0.3f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                    Text(
                        text = "${patient.age} ${stringResource(R.string.years_old)}",
                        color = contentColor,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightGray.copy(alpha = 0.3f))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }

                PatientInfo(
                    patient = patient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                if (state.patientBio.isEmpty()) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_data_found))
                    }
                } else {
                    PatientStatus(
                        bio = state.patientBio.last(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                /**  Patient info previous code
                //
                //
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = "Age",
                //                    fontSize = 12.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = Color.Gray
                //                )
                //
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = "${patient.age} years old",
                //                    fontSize = 14.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = contentColor
                //                )
                //                HorizontalDivider(
                //                    modifier = Modifier
                //                        .padding(horizontal = 16.dp)
                //                        .padding(top = 8.dp, bottom = 12.dp)
                //                )
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = "Gender",
                //                    fontSize = 12.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = Color.Gray
                //                )
                //
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = "Male",
                //                    fontSize = 14.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = contentColor
                //                )
                //                HorizontalDivider(
                //                    modifier = Modifier
                //                        .padding(horizontal = 16.dp)
                //                        .padding(top = 8.dp, bottom = 12.dp)
                //                )
                //
                //
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = "Phone",
                //                    fontSize = 12.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = Color.Gray
                //                )
                //
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = patient.phoneNo,
                //                    fontSize = 14.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = contentColor
                //                )
                //                HorizontalDivider(
                //                    modifier = Modifier
                //                        .padding(horizontal = 16.dp)
                //                        .padding(top = 8.dp, bottom = 12.dp)
                //                )
                //
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = "Address",
                //                    fontSize = 12.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = Color.Gray
                //                )
                //
                //                Text(
                //                    modifier = Modifier
                //                        .align(Alignment.Start)
                //                        .padding(start = 16.dp),
                //                    text = patient.address,
                //                    fontSize = 14.sp,
                //                    fontWeight = FontWeight.Light,
                //                    color = contentColor
                //                )
                //                HorizontalDivider(
                //                    modifier = Modifier
                //                        .padding(horizontal = 16.dp)
                //                        .padding(top = 8.dp, bottom = 12.dp)
                //                )
                 */
//                DateRangePicker(
//                    minStartDate = LocalDate.now().minusMonths(3)
//                ) { startDate, endDate ->
//                    filter(startDate, endDate)
//                }
                /** graph types expanded previous code
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
                 */

                /**
                 * Line chart previous code


                AnimatedVisibility(
                modifier = modifier
                .padding(horizontal = 6.dp),
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
                val amountOfVisibleDataPoints = 24
                //                    val amountOfVisibleDataPoints = if (labelWidth > 0) {
                //                        ((totalChartWidth - 0.5 * labelWidth) / labelWidth).toInt()
                //                    } else {
                //                        0
                //                    }
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
                .horizontalScroll(rememberScrollState())
                .aspectRatio(16 / 16f)
                .onSizeChanged { totalChartWidth = it.width.toFloat() },
                selectedDataPoint = selectedDataPoint,
                onSelectedDataPoint = {
                selectedDataPoint = it
                },
                onXLabelWidthChange = { labelWidth = it }
                )
                }
                 */


                /** Chart vico */
                /**
                 *    TODO: VicoChart number 1- 7- 9


                val marker = rememberMarker(
                labelPosition = DefaultCartesianMarker.LabelPosition.Top,
                showIndicator = true
                )
                CartesianChartHost(
                chart =
                rememberCartesianChart(
                rememberLineCartesianLayer(
                LineCartesianLayer.LineProvider.series(
                LineCartesianLayer.rememberLine(
                remember {
                LineCartesianLayer.LineFill.single(
                fill(
                Color(
                0xFF247CFF
                )
                )
                )
                },
                thickness = 2.dp,
                dataLabelVerticalPosition = VerticalPosition.Top,
                )
                )
                ),
                getXStep = { cartesianChartModel -> 1.0 },
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                guideline = null,
                itemPlacer = remember { HorizontalAxis.ItemPlacer.segmented() },
                ),
                marker = marker,
                layerPadding = cartesianLayerPadding(
                scalableStart = 0.dp,
                scalableEnd = 0.dp
                ),

                persistentMarkers = rememberExtraLambda(marker) { marker at 5f },
                ),
                modelProducer = modelProducer,
                modifier = Modifier
                .padding(
                horizontal = 8.dp,
                vertical = 36.dp
                )
                .height(340.dp),
                zoomState = rememberVicoZoomState(zoomEnabled = false),
                )
                 */


                /** chart two
                 */

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightGray.copy(alpha = 0.4f))
                            .clickable {
                                showDialog = true
                            }
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_filter),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = selectedDate,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightGray.copy(alpha = 0.4f))
                            .clickable {
                                shouldShowLanguageDialog = true
                            }
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_graph),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = when (graphType) {
                                GraphType.Curved -> stringResource(R.string.curved)
                                GraphType.Line -> stringResource(R.string.line)
                                GraphType.Bar -> stringResource(R.string.bar)
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                when (state.tabTypeIndex) {
                    0 -> SugarGraph(
                        type = graphType,
                        date = selectedDate,
                        state = state,
                        modifier = Modifier.fillMaxWidth()
                    )

                    1 -> PressureGraph(
                        type = graphType,
                        date = selectedDate,
                        state = state,
                        modifier = Modifier.fillMaxWidth()
                    )

                    else -> TemperatureGraph(
                        type = graphType,
                        date = selectedDate,
                        state = state,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun PatientInfo(
    patient: Patient,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.patient_info),
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = Inter
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(LightGray.copy(0.3f)),
        ) {
            PatientInfoRow(
                text = stringResource(R.string.gender),
                value = patient.gender,
                iconRes = R.drawable.ic_gender
            )
            PatientInfoRow(
                text = stringResource(R.string.phone),
                value = patient.phoneNo,
                iconRes = R.drawable.ic_phone,
                iconColor = Color.Green.copy(alpha = 0.6f)
            )
            PatientInfoRow(
                text = stringResource(R.string.address),
                value = patient.address,
                iconRes = R.drawable.ic_location,
                iconColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun PatientStatus(
    bio: Bio,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.bio_indicators),
            style = MaterialTheme.typography.titleMedium.copy(
                fontFamily = Inter
            ),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(LightGray.copy(0.3f)),
        ) {
            PatientInfoRow(
                text = stringResource(R.string.sugar),
                value = "${bio.bloodSugar} ${stringResource(R.string.mg_dl)}",
                iconRes = R.drawable.ic_sugar,
                iconColor = Color.Red.copy(0.6f)
            )
            PatientInfoRow(
                text = stringResource(R.string.pressure),
                value = "${bio.bloodPressure} ${stringResource(R.string.mm_hg)}",
                iconRes = R.drawable.ic_blood,
                iconColor = MaterialTheme.colorScheme.error.copy(0.9f)
            )
            PatientInfoRow(
                text = stringResource(R.string.temperature),
                value = "${bio.averageTemperature}°",
                iconRes = R.drawable.ic_temp,
                iconColor = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
            )
        }
    }
}

/** other composable
//
//@Composable
//fun Others(
//    modifier: Modifier = Modifier
//) {
//    val context = LocalContext.current
//
//    Column(
//        modifier = modifier.fillMaxWidth()
//    ) {
//        Text(
//            text = stringResource(R.string.others),
//            style = MaterialTheme.typography.titleMedium,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier.align(Alignment.Start)
//        )
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clip(MaterialTheme.shapes.small)
//                .background(MaterialTheme.colorScheme.surfaceVariant),
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//                    .clickable {
//                        openGithub(context)
//                    }
//                    .padding(horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    modifier = Modifier.size(32.dp),
//                    painter = painterResource(MoviwaraIcons.GitHubIcon),
//                    contentDescription = null
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = stringResource(R.string.github_page),
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//
//            HorizontalDivider(
//                thickness = 1.dp,
//                color = MaterialTheme.colorScheme.background
//            )
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//                    .clickable {
//                        emailDeveloper(context)
//                    }
//                    .padding(horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    modifier = Modifier.size(32.dp),
//                    painter = painterResource(MoviwaraIcons.Contact),
//                    contentDescription = null
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = stringResource(R.string.developer_contact),
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//        }
//    }
//}

 */

@Composable
fun PatientInfoRow(
    text: String,
    value: String,
    @DrawableRes
    iconRes: Int,
    iconColor: Color = Color.Unspecified
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = "$text: ",
            style = MaterialTheme.typography.titleSmall.copy(
                fontFamily = Inter
            ),
            modifier = Modifier
                .alpha(.7f)
                .padding(start = 12.dp)
        )
        Text(
            modifier = Modifier,
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = Inter
            ),
        )
    }
    HorizontalDivider(
        thickness = 2.dp,
        color = MaterialTheme.colorScheme.background
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    // Use Material3 DatePicker
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val selectedDate =
                            LocalDate.ofEpochDay(selectedDateMillis / (1000 * 60 * 60 * 24))
                        val formattedDate =
                            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        onDateSelected(formattedDate)
                    }
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

enum class GraphType {
    Curved,
    Line,
    Bar,
}