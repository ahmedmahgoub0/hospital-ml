package com.acoding.hospital.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acoding.hospital.AdaptiveCoinListDetailPain
import com.acoding.hospital.ui.theme.HospitalTheme
import com.acoding.hospital.ui.theme.SpaceMono

@Composable
fun LoginScreen(
    state: LoginUiState,
    onUsernameChanged: (CharSequence) -> Unit,
    onPasswordChanged: (CharSequence) -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {

        if (state.showHomeScreen) {
            AdaptiveCoinListDetailPain()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
            ) {

                Spacer(modifier = Modifier.height(64.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Welcome Back!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = SpaceMono
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "Sign In To Continue.",
                    color = Color.Gray,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = SpaceMono
                    )
                )


                Spacer(modifier = Modifier.height(84.dp))

                OutlinedTextField(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    value = state.username,
                    onValueChange = { onUsernameChanged(it) },
                    placeholder = { Text("Username") },
                    visualTransformation = VisualTransformation.None,

                    shape = RoundedCornerShape(16.dp)

                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    value = state.password,
                    onValueChange = { onPasswordChanged(it) },
                    placeholder = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(16.dp)

                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                        .padding(horizontal = 16.dp),
                    onClick = onLogin,

                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        "Login",
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clip(RoundedCornerShape(16.dp))
                        .clickable {

                        },
                    text = "Contact us if you have any issues.",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = SpaceMono
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (state.isLoading) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.4f))
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    HospitalTheme {

        LoginScreen(
            state = LoginUiState(),
            onUsernameChanged = {},
            onPasswordChanged = {},
            onLogin = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}