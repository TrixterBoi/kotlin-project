package com.project.gracetastybites.ui.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.launch

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
    onSave: (firstName: String, lastName: String, email: String, password: String?, address: String) -> Unit = { _, _, _, _, _ -> },
    onLogout: () -> Unit = {}
) {
    var firstName by remember { mutableStateOf(initialFirstName) }
    var lastName by remember { mutableStateOf(initialLastName) }
    var email by remember { mutableStateOf(initialEmail) }
    var address by remember { mutableStateOf(initialAddress) }

    // Dialog state
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    // For password change
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // UI state
    var isLoading by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Validation
    val emailValid = isEmailValid(email)
    val firstNameValid = firstName.isNotBlank()
    val lastNameValid = lastName.isNotBlank()
    val addressValid = address.isNotBlank()
    val passwordValid = password.isEmpty() || isPasswordValid(password)
    val passwordsMatch = password == confirmPassword
    val canSave = firstNameValid && lastNameValid && emailValid &&
            (password.isEmpty() || (passwordValid && passwordsMatch)) &&
            addressValid && !isLoading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            Text("Account Details", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "First Name") },
                modifier = Modifier.fillMaxWidth()
            )
            if (!firstNameValid) {
                Text(
                    "First name cannot be empty.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Last Name") },
                modifier = Modifier.fillMaxWidth()
            )
            if (!lastNameValid) {
                Text(
                    "Last name cannot be empty.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            // Email field (read-only, clickable)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .clickable { showChangeEmailDialog = true }
                    .padding(vertical = 4.dp)
                    .semantics { contentDescription = "Email" }
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("Email Address") },
                    singleLine = true,
                    readOnly = true,
                    enabled = false,
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                    trailingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Email")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (!emailValid) {
                Text(
                    "Please enter a valid email address.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            // Password field (masked, read-only, clickable)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .clickable { showChangePasswordDialog = true }
                    .padding(vertical = 4.dp)
                    .semantics { contentDescription = "Password" }
            ) {
                OutlinedTextField(
                    value = if (password.isEmpty()) "********" else password,
                    onValueChange = {},
                    label = { Text("Password") },
                    singleLine = true,
                    readOnly = true,
                    enabled = false,
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                    trailingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Password")
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            if (!addressValid) {
                Text(
                    "Address cannot be empty.",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        onSave(firstName, lastName, email, if (password.isEmpty()) null else password, address)
                        snackbarMessage = "Account details saved!"
                        isLoading = false
                    }
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text("Save")
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }

        // Snackbar for feedback
        snackbarMessage?.let { msg ->
            LaunchedEffect(msg) {
                snackbarHostState.showSnackbar(msg)
                snackbarMessage = null
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
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
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        email = newEmail
                        showChangeEmailDialog = false
                        snackbarMessage = "Email updated."
                    },
                    enabled = newEmailValid
                ) {
                    Text("Save")
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
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
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
                    if (confirmNewPassword.isNotEmpty() && !newPasswordsMatch) {
                        Text(
                            "Passwords do not match.",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
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
                        snackbarMessage = "Password updated."
                    },
                    enabled = newPasswordValid && newPasswordsMatch
                ) {
                    Text("Save")
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