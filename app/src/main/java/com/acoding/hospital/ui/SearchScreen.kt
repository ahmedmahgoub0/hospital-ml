package com.acoding.hospital.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.acoding.hospital.R
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.ui.home.HomeListItem
import com.acoding.hospital.ui.theme.Gray
import com.acoding.hospital.ui.theme.HospitalTheme
import com.acoding.hospital.ui.theme.Inter
import com.acoding.hospital.ui.theme.LightGray


@Composable
fun SearchScreen(
    list: List<Patient>,
    onBack: () -> Unit,
    onClickPatient: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    var query by remember { mutableStateOf("") }

    val filteredList = list.filter {
        it.name.contains(query, ignoreCase = true) || it.code.contains(query, ignoreCase = true)
    }

    BackHandler {
        onBack()
    }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        val criticalColor =
//            if (patient.healthStatus < 60) MaterialTheme.colorScheme.error
//            else greenBackground
//        val critical = if (patient.healthStatus < 60) "Critical" else "Normal"
//        val context = LocalContext.current

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
                text = stringResource(R.string.search),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        DocdocSearchTextField(
            text = query,
            placeholderText = stringResource(R.string.search),
            isError = false,
            errorMessage = "",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            onValueChanged = {
                query = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        LazyColumn {
            items(
                items = filteredList,
                key = { "${it.code}, ${it.id}" }
            ) { item ->
                HomeListItem(
                    tabTypeIndex = 0,
                    item,
                    onCLick = { onClickPatient(it, 0) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(fadeInSpec = null, fadeOutSpec = null),
                )
            }
        }
    }


}

@Preview
@Composable
private fun SearchScreenPreview() {
    HospitalTheme {
        SearchScreen(
            list = emptyList(),
            onBack = { },
            onClickPatient = { a, b -> },
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}


@Composable
fun DocdocSearchTextField(
    text: String,
    placeholderText: String,
    errorMessage: String = "",
    isError: Boolean = false,
    singleLine: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {

    var isFocused by remember { mutableStateOf(false) }

    val visualTransformation = VisualTransformation.None

    val icon = ImageVector.vectorResource(R.drawable.ic_search)

    OutlinedTextField(
        value = text,
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .onFocusChanged { isFocused = it.isFocused },
        label = {
            Text(
                text = placeholderText,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontFamily = Inter,
                    color = if (isFocused) MaterialTheme.colorScheme.primary else Gray
                )
            )
        },
        isError = isError,
        visualTransformation = visualTransformation,
        shape = MaterialTheme.shapes.large,
        onValueChange = onValueChanged,
        textStyle = textStyle,
        supportingText = {
            if (errorMessage.isNotEmpty()) Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = if (isFocused) MaterialTheme.colorScheme.primary else
                    LightGray
            )
        },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            focusedPlaceholderColor = Gray,
            unfocusedPlaceholderColor = Gray,
            focusedSupportingTextColor = MaterialTheme.colorScheme.error,
            unfocusedSupportingTextColor = MaterialTheme.colorScheme.error,
            focusedBorderColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = if (isError) MaterialTheme.colorScheme.error else LightGray,
        )
    )
}
