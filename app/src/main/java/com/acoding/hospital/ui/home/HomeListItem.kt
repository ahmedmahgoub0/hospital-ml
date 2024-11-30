package com.acoding.hospital.ui.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acoding.hospital.R
import com.acoding.hospital.data.model.Patient
import com.acoding.hospital.ui.theme.greenBackground

@Composable
fun HomeListItem(
    tabTypeIndex: Int,
    patient: Patient,
    onCLick: (patientId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black

    val criticalColor = when (tabTypeIndex) {
        0 -> if (patient.healthStatus <= 60) MaterialTheme.colorScheme.error else greenBackground
        1 -> if (patient.healthStatus <= 50) MaterialTheme.colorScheme.error else greenBackground
        else -> if (patient.healthStatus <= 60) MaterialTheme.colorScheme.error else greenBackground
    }

//    val criticalColor = if (patient.healthStatus < 60) MaterialTheme.colorScheme.error
//    else greenBackground
    val critical = when (criticalColor) {
        greenBackground -> stringResource(R.string.normal)
        else -> stringResource(R.string.critical)
    }

    /** previous code
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


     */


    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCLick(patient.id) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = if (critical == "Normal") Color.Green else Color.Red,
                    shape = CircleShape
                )
        ) {
            Image(
                painter = painterResource(patientToImage(patient.id, patient.gender)),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        val ageText = stringResource(R.string.age)
        val codeText = stringResource(R.string.code)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = patient.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$ageText: ${patient.age}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "$codeText: ${patient.code}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(bottom = 0.dp)
                .clip(RoundedCornerShape(100f))
                .background(criticalColor)
                .padding(horizontal = 4.dp, vertical = 1.dp),
            text = critical,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onError,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

fun patientToImage(id: Int, gender: String): Int {
    if (gender == "Male") {
        return when (id % 11) {
            0 -> R.drawable.patient1
            1 -> R.drawable.patient4
            2 -> R.drawable.patient5
            3 -> R.drawable.patient6
            5 -> R.drawable.patient7
            6 -> R.drawable.patient8
            7 -> R.drawable.patient9
            8 -> R.drawable.patient1
            9 -> R.drawable.patient17
            10 -> R.drawable.patient18
            else -> R.drawable.luffy
        }
    }
    return when (id % 11) {
        0 -> R.drawable.patient2
        1 -> R.drawable.patient3
        2 -> R.drawable.patient11
        3 -> R.drawable.patient12
        5 -> R.drawable.patient13
        6 -> R.drawable.patient14
        7 -> R.drawable.patient15
        8 -> R.drawable.patient16
        9 -> R.drawable.patient10
        10 -> R.drawable.patient3
        else -> R.drawable.luffy
    }
}