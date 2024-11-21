package com.acoding.hospital.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.acoding.hospital.R
import com.acoding.hospital.data.datastore.Language
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.helpers.updateLocale
import com.acoding.hospital.ui.theme.Inter

//
//@Composable
//fun HomeScreen(
//    state: HomeListState,
//    onClick: (patientId: Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    if (state.isLoading) {
//        Box(
//            modifier = modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            CircularProgressIndicator()
//        }
//    } else {
//        var searchText by remember { mutableStateOf("") }
//        val filteredItems = state.patients.filter {
//            it.name.contains(searchText, ignoreCase = true)
//        }
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.surfaceVariant)
//        ) {
//
//            Text(
//                text = "Patients list",
//                style = MaterialTheme.typography.headlineLarge.copy(
//                    fontFamily = SpaceMono,
//                    fontWeight = FontWeight.W500
//                ),
//                modifier = Modifier
//                    .padding(horizontal = 16.dp)
//                    .padding(top = 16.dp)
//            )
//
//            OutlinedTextField(
//                value = searchText,
//                onValueChange = { searchText = it },
//                label = { Text("Patient name") },
//                singleLine = true,
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search Icon"
//                    )
//                },
//                shape = MaterialTheme.shapes.large,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            LazyColumn(
//                modifier = modifier
//                    .fillMaxSize()
//                    .clip(RoundedCornerShape(28.dp))
//                    .background(Color.White)
//                    .padding(top = 12.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                contentPadding = PaddingValues(horizontal = 16.dp)
//            ) {
//                items(
//                    items = filteredItems,
//                    key = { "${it.id}, ${it.code}" }
//                ) { patient ->
//                    HomeListItem(
//                        modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
//                        patient = patient,
//                        onCLick = { onClick(patient.id) },
//                    )
//                    HorizontalDivider()
//                }
//            }
//        }
//    }
//}


@Composable
fun HomeScreen(
    state: HomeListState,
    onClick: (patientId: Int, tabTypeIndex: Int) -> Unit,
    onLanguageChanged: (Language) -> Unit,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current

    // Tabs state
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    // Sample patient data for each category
    val sugarPatients = state.patients.sortedBy {
        it.healthStatus >= 90
    }

    val pressurePatients = state.patients.sortedBy {
        it.age >= 35
    }

    val tempPatients = state.patients.sortedBy {
        it.gender == "male"
    }

    var searchText by remember { mutableStateOf("") }
    val filteredItems = when (selectedTabIndex) {
        0 -> sugarPatients.filter { it.name.contains(searchText, ignoreCase = true) }
        1 -> pressurePatients.filter { it.name.contains(searchText, ignoreCase = true) }
        else -> tempPatients.filter { it.name.contains(searchText, ignoreCase = true) }
    }

    val tabTitles = listOf(
        stringResource(R.string.sugar),
        stringResource(R.string.pressure),
        stringResource(R.string.temperature)
    )

    var shouldShowLanguageDialog by remember { mutableStateOf(false) }

    if (shouldShowLanguageDialog) {
        LanguageDialog(
            onDismissRequest = {
                shouldShowLanguageDialog = false
            }
        ) {
            Column {
                Language.entries.forEach {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                updateLocale(context, it.locale)
                                onLanguageChanged(it)
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = it.languageName)
                        RadioButton(
                            selected = state.userPreferences?.language == it,
                            onClick = {
                                updateLocale(context, it.locale)
                                onLanguageChanged(it)
                                shouldShowLanguageDialog = false
                            }
                        )
                    }
                }
            }
        }
    }


    Column {


        if (state.isLoading) {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val list = when (selectedTabIndex) {
                0 -> sugarPatients
                1 -> pressurePatients
                else -> tempPatients
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(28.dp),
                    tint = Color.Unspecified
                )

                Text(
                    text = "Hospital", // Fuck You,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .padding(vertical = 12.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = Inter,
                        letterSpacing = 0.1.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                IconButton(
                    onClick = {
                        /* TODO: Handle search icon click  */
                    },
                    modifier = Modifier

                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                        contentDescription = "search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(
                    onClick = {
                        shouldShowLanguageDialog = true
                    },
                    modifier = Modifier
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Translate,
                        contentDescription = "notify",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Tabs Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabTitles.forEachIndexed { index, title ->
                    val selected = selectedTabIndex == index
                    Tab(
                        modifier = Modifier.background(Color.White),
                        selected = selectedTabIndex == index,
                        onClick = {
                            state.tabTypeIndex = selectedTabIndex
                            selectedTabIndex = index
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onBackground,
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = if (selected) 15.sp else 13.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }

//            OutlinedTextField(
//                value = searchText,
//                onValueChange = { searchText = it },
//                label = { Text("Patient name") },
//                singleLine = true,
//                leadingIcon = {
//                    Icon(
//                        imageVector = Icons.Default.Search,
//                        contentDescription = "Search Icon"
//                    )
//                },
//                shape = MaterialTheme.shapes.large,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp)
//            )

            PatientList(
                tabTypeIndex = selectedTabIndex,
                patients = filteredItems,
                onClick = onClick
            )
        }
    }
}

@Composable
fun PatientList(
    tabTypeIndex: Int,
    patients: List<Patient>,
    onClick: (patientId: Int, tabTypeIndex: Int) -> Unit
) {
    if (patients.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.no_patients),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                items = patients,
                key = { "${it.id}, ${it.code}" }
            ) { patient ->
                HomeListItem(
                    tabTypeIndex = tabTypeIndex,
                    patient = patient,
                    modifier = Modifier.animateItem(fadeInSpec = null, fadeOutSpec = null),
                    onCLick = { onClick(patient.id, tabTypeIndex) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

        }
    }
}


@Composable
fun LanguageDialog(
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.background),
        ) {
            content()
        }
    }
}