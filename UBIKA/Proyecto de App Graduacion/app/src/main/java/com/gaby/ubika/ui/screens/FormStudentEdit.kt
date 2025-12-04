package com.gaby.ubika.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gaby.ubika.R
import com.gaby.ubika.domain.model.Student
import com.gaby.ubika.ui.components.DropdownMenuCarrera

@Composable
fun FormStudentEdit(
    student: Student,
    carrerasDisponibles: List<String>,
    onDismiss: () -> Unit,
    onGuardar: (Student) -> Unit,
    onEliminarAsiento: (String) -> Unit // nuevo callback
) {
    var nombre by remember { mutableStateOf(student.nombre) }
    var matricula by remember { mutableStateOf(student.id) }
    var carreraSeleccionada by remember { mutableStateOf(student.carrera) }
    var merito by remember { mutableStateOf(student.merito) }

    val camposValidos = nombre.isNotBlank() && matricula.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (student.id.isBlank()) "Agregar Estudiante" else "Editar Estudiante",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = matricula,
                    onValueChange = { matricula = it },
                    label = { stringResource(R.string.matricula) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))

                DropdownMenuCarrera(
                    opciones = carrerasDisponibles,
                    seleccionActual = carreraSeleccionada.ifBlank { "Sin carrera" },
                    onSeleccionar = { carreraSeleccionada = it }
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = merito,
                    onValueChange = { merito = it },
                    label = { Text("Mérito") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },


         confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                 if (!student.asientoAsignado.isNullOrBlank()) {
                    TextButton(
                        onClick = {
                            onEliminarAsiento(student.id)
                            onDismiss()
                        }
                    ) {
                        Text("Eliminar asiento", color = MaterialTheme.colorScheme.error)
                    }
                }

                Row {
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancelar))
                    }
                    TextButton(
                        onClick = {
                            if (camposValidos) {
                                val estudianteEditado = student.copy(
                                    nombre = nombre.trim(),
                                    id = matricula.trim(),
                                    carrera = if (carreraSeleccionada == "Sin carrera") "" else carreraSeleccionada.trim(),
                                    merito = merito.trim()
                                )
                                onGuardar(estudianteEditado)
                                onDismiss()
                            }
                        },
                        enabled = camposValidos
                    ) {
                        Text(stringResource(R.string.guardar))
                    }
                }
            }
        }
    )
}
