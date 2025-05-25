package com.project.gracetastybites.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ForgotPasswordScreen(
    onSubmit: (String) -> Unit = {}
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var submitted by remember { mutableStateOf(false) }

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
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    onSubmit(email.text)
                    submitted = true
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = email.text.isNotBlank() && !submitted
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