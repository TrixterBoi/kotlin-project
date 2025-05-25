package com.project.gracetastybites.ui.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation

private fun isEmailValid(email: String): Boolean {
    val atIndex = email.indexOf('@')
    val dotIndex = email.lastIndexOf('.')
    return atIndex > 0 && dotIndex > atIndex + 1 && dotIndex < email.length - 1
}

private fun isPasswordValid(password: String): Boolean {
    val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-={}:;\"'|<>,.?/~`]).{8,}\$")
    return regex.matches(password)
}

@Composable
fun AccountDetailsScreen(
    initialFirstName: String = "",
    initialLastName: String = "",
    initialEmail: String = "",
    initialAddress: String = "",
    initialPayment: String = "",
    onSave: (firstName: String, lastName: String, email: String, password: String?, address: String, payment: String) -> Unit = { _, _, _, _, _, _ -> }
) {
    var firstName by remember { mutableStateOf(initialFirstName) }
    var lastName by remember { mutableStateOf(initialLastName) }
    var email by remember { mutableStateOf(initialEmail) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var address by remember { mutableStateOf(initialAddress) }
    var payment by remember { mutableStateOf(initialPayment) }

    // Dialog state
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    val emailValid = isEmailValid(email)
    val passwordValid = password.isEmpty() || isPasswordValid(password)
    val passwordsMatch = password == confirmPassword
    val canSave = firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() && emailValid &&
            (password.isEmpty() || (passwordValid && passwordsMatch)) &&
            address.isNotBlank() && payment.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Account Details", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = false // Disable direct editing
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { showChangeEmailDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Email")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("New Password") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = false // Disable direct editing
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = { showChangePasswordDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Password")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                singleLine = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { onSave(firstName, lastName, email, if (password.isNotEmpty()) password else null, address, payment) },
                modifier = Modifier.fillMaxWidth(),
                enabled = canSave
            ) {
                Text("Save")
            }
        }
    }

    // Change Email Dialog
    if (showChangeEmailDialog) {
        var newEmail by remember { mutableStateOf("") }
        val newEmailValid = isEmailValid(newEmail)
        AlertDialog(
            onDismissRequest = { showChangeEmailDialog = false },
            title = { Text("Change Email") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newEmail,
                        onValueChange = { newEmail = it },
                        label = { Text("New Email Address") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (newEmail.isNotEmpty() && !newEmailValid) {
                        Text(
                            "Please enter a valid email address.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        email = newEmail
                        showChangeEmailDialog = false
                    },
                    enabled = newEmailValid
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangeEmailDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Change Password Dialog
    if (showChangePasswordDialog) {
        var newPassword by remember { mutableStateOf("") }
        var confirmNewPassword by remember { mutableStateOf("") }
        val newPasswordValid = isPasswordValid(newPassword)
        val newPasswordsMatch = newPassword == confirmNewPassword
        AlertDialog(
            onDismissRequest = { showChangePasswordDialog = false },
            title = { Text("Change Password") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (newPassword.isNotEmpty() && !newPasswordValid) {
                        Text(
                            "Password must be at least 8 characters, include 1 uppercase, 1 lowercase, 1 number, and 1 special character.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = confirmNewPassword,
                        onValueChange = { confirmNewPassword = it },
                        label = { Text("Confirm New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (newPassword.isNotEmpty() && confirmNewPassword.isNotEmpty() && !newPasswordsMatch) {
                        Text(
                            "Passwords do not match.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        password = newPassword
                        confirmPassword = confirmNewPassword
                        showChangePasswordDialog = false
                    },
                    enabled = newPasswordValid && newPasswordsMatch && newPassword.isNotEmpty()
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showChangePasswordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}