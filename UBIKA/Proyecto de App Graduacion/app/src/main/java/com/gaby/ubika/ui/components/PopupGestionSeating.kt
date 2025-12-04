package com.gaby.ubika.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gaby.ubika.domain.model.Seating
import com.gaby.ubika.domain.model.Student
import kotlinx.coroutines.launch

@Composable
fun PopupGestionSeating(
    seating: Seating,
    estudiantesDisponibles: List<Student>,
    onDismiss: () -> Unit,
    onActualizarAsiento: (Student?, String) -> Unit
)  {
    val scope =
        rememberCoroutineScope()                    //pa lanzar coroutines desde este composable
    var carreraSeleccionada by remember {
        mutableStateOf(
            seating.carrera ?: ""
        )
    }    //vacio sin carrera asignada //Se actualiza al actualziar el drpdnwmenu
    var filtroTexto by remember { mutableStateOf("") }
    var estudianteSeleccionado by remember {
        mutableStateOf(
            estudiantesDisponibles.find { it.id == seating.idEstudiante }
        )
    }

    val carrerasDisponibles = listOf("Software", "Educacion", "Administracion", "Cibersecurity")

    val estudiantesFiltrados = estudiantesDisponibles
        .filter { it.id.isNotBlank() }      //Filtro estudiantes que tienen ID válido
        .filter { carreraSeleccionada.isBlank() || it.carrera == carreraSeleccionada }
        .filter {                              //búsqueda por nombre o matrícula
            it.nombre.contains(filtroTexto, ignoreCase = true) ||
                    it.id.contains(filtroTexto, ignoreCase = true)
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Gestionar Asiento", style = MaterialTheme.typography.headlineSmall)
        },
        text = {
            Column {
                Text("Carrera asignada:")
                Spacer(modifier = Modifier.Companion.height(8.dp))

                DropdownMenuCarrera(
                    opciones = carrerasDisponibles,
                    seleccionActual = carreraSeleccionada,
                    onSeleccionar = { carreraSeleccionada = it }
                )

                Spacer(modifier = Modifier.Companion.height(16.dp))

                OutlinedTextField(
                    value = filtroTexto,
                    onValueChange = { filtroTexto = it },
                    label = { Text("Buscar estudiante") },
                    modifier = Modifier.Companion.fillMaxWidth()
                )

                Spacer(modifier = Modifier.Companion.height(8.dp))

                LazyColumn(modifier = Modifier.Companion.height(200.dp)) {
                    items(estudiantesFiltrados) { estudiante ->
                        StudentItem(
                            student = estudiante,
                            isSelected = estudianteSeleccionado?.id == estudiante.id,
                            onClick = {
                                estudianteSeleccionado = estudiante
                                if (carreraSeleccionada.isBlank()) { //Aunque no seleccione carrera primero, el asiento tomara el del estudiante seleccionado
                                    carreraSeleccionada = estudiante.carrera
                                }
                            }
                        )
                    }
                }

                if (estudiantesFiltrados.isEmpty()) {
                    Spacer(modifier = Modifier.Companion.height(12.dp))
                    Text(
                        "No hay estudiantes disponibles para esta carrera.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Spacer(modifier = Modifier.Companion.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.Companion.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            val nuevoAsiento = seating.copy(
                                idEstudiante = estudianteSeleccionado?.id,
                                carrera = carreraSeleccionada
                            )
                            scope.launch {
                                onActualizarAsiento(
                                    estudianteSeleccionado,
                                    carreraSeleccionada
                                )
                            }
                        },
                        enabled = estudianteSeleccionado != null || carreraSeleccionada.isNotBlank()
                    ) {
                        Text("Asignar / Actualizar")
                    }

                    OutlinedButton(
                        onClick = {
                            val nuevoAsiento = seating.copy(idEstudiante = null)
                            scope.launch { onActualizarAsiento(null, carreraSeleccionada) }
                        },
                        enabled = seating.idEstudiante != null
                    ) {
                        Text("Desasignar estudiante")
                    }

                    OutlinedButton(
                        onClick = {
                            val nuevoAsiento = seating.copy(idEstudiante = null, carrera = null)
                            scope.launch { onActualizarAsiento(null, "") }
                        }
                    ) {
                        Text("Vaciar asiento")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}