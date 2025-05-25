package com.project.gracetastybites.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.graphics.Color

private fun isEmailValid(email: String): Boolean {
    val atIndex = email.indexOf('@')
    val dotIndex = email.lastIndexOf('.')
    return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length - 1
}

@Composable
fun ForgotPasswordScreen(
    onSubmit: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var submitted by remember { mutableStateOf(false) }

    val emailValid = isEmailValid(email.text)
    val canSubmit = email.text.isNotBlank() && emailValid && !submitted

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Forgot Password", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (email.text.isNotEmpty() && !emailValid) {
                Text(
                    "Please enter a valid email address.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    onSubmit(email.text)
                    submitted = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canSubmit
            ) {
                Text("Submit")
            }
            if (submitted) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "An email has been sent with a reset password link.",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}