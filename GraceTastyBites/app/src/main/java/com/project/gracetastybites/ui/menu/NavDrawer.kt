package com.project.gracetastybites.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NavDrawer(
    drawerItems: List<Triple<String, Any?, String>>,
    onItemClick: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    ModalDrawerSheet {
        Text(
            "GraceTastyBites",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
        drawerItems.forEach { (label, icon, contentDesc) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(label) }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon is androidx.compose.ui.graphics.vector.ImageVector) {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDesc,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Login")
        }
    }
}