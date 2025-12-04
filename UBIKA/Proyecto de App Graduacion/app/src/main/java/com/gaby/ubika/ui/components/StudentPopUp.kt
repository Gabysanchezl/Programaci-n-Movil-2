package com.gaby.ubika.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
 import androidx.compose.foundation.layout.fillMaxWidth
 import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gaby.ubika.domain.model.Student
import com.gaby.ubika.ui.screens.FormStudentEdit
import com.gaby.ubika.viewmodels.AdminLoginViewModel
import com.gaby.ubika.viewmodels.SeatingViewModel


@Composable
fun StudentPopUp(
    student: Student,
    carrerasDisponibles: List<String>,
    onDismiss: () -> Unit,
    onEditar: (Student) -> Unit,
    onEliminar: () -> Unit
) {
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var mostrarFormularioEdicion by remember { mutableStateOf(false) }
    val seatingViewModel: SeatingViewModel = viewModel()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Encabezado con icono de persona y botón de cerrar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Estudiante",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                }

                // Student del estudiante
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = student.nombre,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = student.id,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = student.carrera ?: "Sin carrera",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = "Mérito: ${student.merito.ifBlank { "Sin mérito" }}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "Asiento: ${student.asientoAsignado ?: "Sin asignar"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(onClick = { mostrarFormularioEdicion = true }) {
                        Text("Editar")
                    }
                    TextButton(onClick = { mostrarConfirmacion = true }) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }

    ConfirmDeleteDialog(
        visible = mostrarConfirmacion,
        onConfirmar = {
            mostrarConfirmacion = false
            onEliminar()
            onDismiss()
        },
        onCancelar = { mostrarConfirmacion = false }
    )


    if (mostrarFormularioEdicion) {
        FormStudentEdit(
            student = student,
            carrerasDisponibles = carrerasDisponibles,
            onDismiss = { mostrarFormularioEdicion = false },
            onGuardar = {
                onEditar(it)
                mostrarFormularioEdicion = false
                onDismiss()
            },
            onEliminarAsiento = { studentId ->
                seatingViewModel.obtenerAsientoDeEstudiante(studentId)?.let { asiento ->
                    seatingViewModel.desasignarEstudianteDeAsiento(studentId, asiento.codigo)
                }
                mostrarFormularioEdicion = false
                onDismiss()
            }
        )
    }
}