package com.acoding.hospital.ui.onboarding.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acoding.hospital.R
import com.acoding.hospital.ui.theme.HospitalTheme

@Composable
fun ImageVerticalGradient(
    imagePainter: Painter,
    gradientColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = imagePainter,
            contentDescription = "image",
            modifier = Modifier
                .fillMaxWidth()
                .size(
                    width = 375.dp,
                    height = 490.dp
                ),
            alignment = Alignment.TopCenter
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            gradientColor
                        )
                    )
                )
        )
    }
}

@Preview
@Composable
private fun ImageVerticalGradientPreview() {
    HospitalTheme {
        ImageVerticalGradient(
            imagePainter = painterResource(R.drawable.doctor),
            gradientColor = Color.White
        )

    }
}