package com.acoding.hospital.ui.onboarding.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.acoding.hospital.R
import com.acoding.hospital.ui.theme.Gray
import com.acoding.hospital.ui.theme.HospitalTheme
import com.acoding.hospital.ui.theme.Inter
import com.acoding.hospital.ui.theme.LightGray

@Composable
fun DocdocTextField(
    text: String,
    placeholderText: String,
    leadingIcon: Int? = null,
    isPassword: Boolean = false,
    match: Boolean = false,
    isError: Boolean = false,
    errorMessage: String = "",
    singleLine: Boolean = false,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    val visualTransformation = if (isPassword && !passwordVisible)
        PasswordVisualTransformation()
    else
        VisualTransformation.None

    val icon =
        if (passwordVisible)
            ImageVector.vectorResource(R.drawable.ic_eye_off) else
            ImageVector.vectorResource(R.drawable.ic_eye_on)

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
                    color = if (isFocused) MaterialTheme.colorScheme.primary
                    else if (isError) MaterialTheme.colorScheme.error
                    else Gray
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
        trailingIcon = {
            if (isPassword) {
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {

                    Icon(
                        imageVector = icon,
                        contentDescription = "icon visible",
                        tint = if (isFocused && isError) MaterialTheme.colorScheme.error
                        else if (isFocused) MaterialTheme.colorScheme.primary
                        else if (isError) MaterialTheme.colorScheme.error
                        else Gray
                    )

                }
            }
        },
        leadingIcon = {
            if (leadingIcon != null)
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = ImageVector.vectorResource(leadingIcon),
                    contentDescription = "icon visible",
                    tint = if (isFocused && isError) MaterialTheme.colorScheme.error
                    else if (isFocused) MaterialTheme.colorScheme.primary
                    else if (isError) MaterialTheme.colorScheme.error
                    else Gray
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

@Preview
@Composable
private fun DocdocTextFieldPreview() {
    HospitalTheme {
        DocdocTextField(
            text = "",
            placeholderText = "Email",
            isError = false,
            errorMessage = "",
            keyboardOptions = KeyboardOptions.Default,
            singleLine = false,
            onValueChanged = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}