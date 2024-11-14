package com.acoding.hospital.ui.bio

import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.util.Calendar

@Composable
fun DateRangePicker(
    minStartDate: LocalDate,
    onDateRangeSelected: (startDate: LocalDate, endDate: LocalDate) -> Unit
) {
    val context = LocalContext.current

    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }

    fun showDatePicker(
        context: Context,
        onDateSelected: (LocalDate) -> Unit,
        maxDate: LocalDate? = null
    ) {
        val calendar = Calendar.getInstance()

        val maxYear = maxDate?.year ?: calendar.get(Calendar.YEAR)
        val maxMonth = maxDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
        val maxDay = maxDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        if (maxDate != null) {
            datePickerDialog.datePicker.maxDate = Calendar.getInstance().apply {
                set(maxYear, maxMonth, maxDay)
            }.timeInMillis
        }

        datePickerDialog.show()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                onClick = {
                    showDatePicker(context, { startDate = it }, LocalDate.now())
                }
            ) {
                Text(
                    text = startDate?.toString() ?: "Start Date",
                    style = MaterialTheme.typography.bodyMedium
                )

            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                onClick = {
                    showDatePicker(context, { endDate = it }, LocalDate.now())
                }) {
                Text(
                    text = endDate?.toString() ?: "End Date",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (startDate != null && endDate != null) {
                    onDateRangeSelected(startDate!!, endDate!!)
                }
            },
            enabled = startDate != null && endDate != null
        ) {
            Text(
                text = "Apply Date Range",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
