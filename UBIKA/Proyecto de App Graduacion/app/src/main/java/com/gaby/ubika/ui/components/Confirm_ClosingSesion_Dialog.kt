package com.gaby.ubika.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun Confirm_ClosingSesion_Dialog(
    visible: Boolean,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    if (visible) {
        AlertDialog(
            onDismissRequest = onCancelar,
            title = { Text("Cerrar sesión") },
            text = { Text("¿Deseas cerrar sesión? Tendrás que iniciar sesión nuevamente para acceder.") },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = onCancelar) {
                        Text("Cancelar")
                    }
                    TextButton(onClick = onConfirmar) {
                        Text("Cerrar sesión")
                    }
                }
            },
            dismissButton = {}
        )
    }
}