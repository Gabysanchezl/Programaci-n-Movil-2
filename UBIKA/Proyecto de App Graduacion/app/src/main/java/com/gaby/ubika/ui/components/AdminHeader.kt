package com.gaby.ubika.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
 import com.gaby.ubika.viewmodels.AdminViewModel


@Composable
fun AdminHeader(viewModel: AdminViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Perfil",
            modifier = Modifier
                .size(50.dp)
                .padding(top = 1.dp, end = 12.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Column {
            Text(viewModel.nombreAdmin, style = MaterialTheme.typography.titleMedium)
            Text("Panel principal!", style = MaterialTheme.typography.titleLarge)

        }
    }
}