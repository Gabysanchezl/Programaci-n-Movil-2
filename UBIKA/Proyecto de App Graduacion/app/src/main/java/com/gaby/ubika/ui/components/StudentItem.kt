package com.gaby.ubika.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.gaby.ubika.domain.model.Student


@Composable
fun StudentItem(
    student: Student,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Column {

            Text("👤 ${student.nombre}", style = MaterialTheme.typography.bodyLarge)
            Text("📚 Matrícula: ${student.id}", style = MaterialTheme.typography.bodySmall)
            Text("🏫 Carrera: ${student.carrera}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
