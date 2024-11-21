package com.acoding.hospital.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acoding.hospital.R
import com.acoding.hospital.ui.onboarding.components.DocdocButton
import com.acoding.hospital.ui.onboarding.components.ImageVerticalGradient
import com.acoding.hospital.ui.theme.Gray
import com.acoding.hospital.ui.theme.HospitalTheme

@Composable
fun OnboardingScreen(
    onGetStartedClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.onboarding_background),
            contentDescription = "Background",
            modifier = Modifier
                .padding(top = 110.dp, end = 16.dp)
                .align(Alignment.TopCenter)
        )

        ImageVerticalGradient(
            modifier = Modifier.padding(bottom = 90.dp),
            imagePainter = painterResource(R.drawable.doctor),
            gradientColor = Color.White
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(top = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .size(38.dp),
                    imageVector = ImageVector.vectorResource(R.drawable.logo),
                    contentDescription = "splashLogo",
                    tint = Color.Unspecified
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Best Doctor Appointment App",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier

                    .padding(horizontal = 16.dp), textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(0.15f))
            Text(
                text = "Manage and schedule all of your medical appointments easily with Docdoc to get a new experience.",
                style = MaterialTheme.typography.labelSmall,
                color = Gray,
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = TextAlign.Center
            )

            DocdocButton(
                text = "Get started",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = onGetStartedClick
            )
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    HospitalTheme {
        OnboardingScreen(
            onGetStartedClick = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}