package com.gaby.ubika.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConfirmDeleteDialog (
        visible: Boolean,
        onConfirmar: () -> Unit,
        onCancelar: () -> Unit
    ) {
        if (visible) {
            AlertDialog(
                onDismissRequest = onCancelar,
                title = { Text("Eliminar Estudiante") },
                text = { Text("¿Estás segura de que deseas eliminar a este Estudiante? Esta acción no se puede deshacer.") },
                confirmButton = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        TextButton(onClick = onCancelar) {
                            Text("Cancelar")
                        }
                        TextButton(onClick = onConfirmar) {
                            Text("Eliminar", color = MaterialTheme.colorScheme.error)
                        }
                    }
                },
                // No necesitas dismissButton separado, ya que lo incluimos en la fila
                dismissButton = {}
            )
        }
}