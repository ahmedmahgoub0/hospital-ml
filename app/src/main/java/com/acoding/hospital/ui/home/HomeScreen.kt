package com.acoding.hospital.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.ui.theme.SpaceMono

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
    modifier: Modifier = Modifier
) {
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

    val tabTitles = listOf("Sugar", "Pressure", "Temperature")

    Column {

        Text(
            text = "Hurghada Hospital",
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = SpaceMono
            )
        )

        // Tabs Row
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        state.tabTypeIndex = selectedTabIndex
                        selectedTabIndex = index
                    },
                    text = { Text(title) }
                )
            }
        }

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

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Patient name") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon"
                    )
                },
                shape = MaterialTheme.shapes.large,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

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
        // Show message when no patients are available
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No Patients",
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
            }
        }
    }
}