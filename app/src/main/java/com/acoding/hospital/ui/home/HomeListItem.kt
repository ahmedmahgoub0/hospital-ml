package com.acoding.hospital.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.ui.theme.greenBackground

@Composable
fun HomeListItem(
    patient: Patient,
    onCLick: (patientCode: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val criticalColor = if (patient.sugarStatus < 90) MaterialTheme.colorScheme.error
    else greenBackground
    val critical = if (patient.sugarStatus < 90) "Critical" else "Normal"

//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { onCLick(patient.code) }
//            .padding(8.dp),
//        horizontalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Column(modifier = Modifier.weight(1f)) {
//            Text(
//                text = patient.name,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = contentColor
//            )
//            Text(
//                text = patient.gender,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Light,
//                color = contentColor
//            )
//            Text(
//                text = patient.age.toString(),
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Light,
//                color = contentColor
//            )
//        }
//        Column(
//            horizontalAlignment = Alignment.End,
//            modifier = Modifier
//                .align(Alignment.Bottom)
//                .padding(bottom = 8.dp)
//        ) {
//            Row(
//                modifier = modifier
//                    .clip(RoundedCornerShape(100f))
//                    .background(criticalColor)
//                    .padding(horizontal = 8.dp)
//            ) {
//                Text(
//                    text = critical,
//                    color = MaterialTheme.colorScheme.onError,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }

//    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCLick(patient.code) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = patient.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${patient.gender} | ${patient.age}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Code: ${patient.code}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            modifier = modifier
                .clip(RoundedCornerShape(100f))
                .background(criticalColor)
                .padding(horizontal = 8.dp),
            text = critical,
            color = MaterialTheme.colorScheme.onError,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}