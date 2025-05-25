package com.project.gracetastybites.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.gracetastybites.data.db.AppDatabase
import com.project.gracetastybites.data.db.authenticateStaff

private fun isEmailValid(email: String): Boolean {
    val atIndex = email.indexOf('@')
    val dotIndex = email.lastIndexOf('.')
    return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length - 1
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onCreateAccountClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val appDatabase = remember { AppDatabase.getInstance(context) }

    val emailValid = isEmailValid(email)
    val canLogin = email.isNotBlank() && password.isNotBlank() && emailValid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(32.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (email.isNotEmpty() && !emailValid) {
                Text(
                    "Please enter a valid email address.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ClickableText(
                    text = AnnotatedString("Forgot Password?"),
                    onClick = { onForgotPasswordClick() },
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    val staff = authenticateStaff(appDatabase.database, email, password)
                    if (staff != null) {
                        errorMessage = null
                        onLoginSuccess()
                    } else {
                        errorMessage = "Invalid email or password."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = canLogin
            ) {
                Text("Login")
            }
            if (errorMessage != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClickableText(
                text = AnnotatedString("Create Account"),
                onClick = { onCreateAccountClick() },
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
            )
        }
    }
}