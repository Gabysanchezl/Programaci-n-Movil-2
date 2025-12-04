package com.gaby.ubika.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import com.gaby.ubika.domain.model.Student
 import androidx.compose.material3.TopAppBar
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gaby.ubika.R
import com.gaby.ubika.ui.components.ConfirmDeleteDialog
import com.gaby.ubika.ui.components.DropdownMenuCarrera
import com.gaby.ubika.viewmodels.SeatingViewModel
import com.gaby.ubika.viewmodels.StudentsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    navController: NavController,
     ) {
    val studentsvm: StudentsViewModel = viewModel()
    val seatingvm: SeatingViewModel  = viewModel()

    val cargando = studentsvm.cargando.value

    val estudiantes = studentsvm.listadoStudents

    if (estudiantes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    var searchText by remember { mutableStateOf("") }
    var carreraSeleccionada by remember { mutableStateOf("Todas") }
    var studentSeleccionado by remember { mutableStateOf<Student?>(null) }

    val carrerasDisponibles = studentsvm.obtenerCarrerasDisponibles()

    val estudiantesFiltrados = studentsvm.filtrarEstudiantes(carreraSeleccionada, searchText)
    var mostrarDialogo by remember { mutableStateOf(false) }

    if (cargando) {
        // Mostrar spinner mientras carga
        CircularProgressIndicator()
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Listado de Estudiantes") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.arrow_back))
                        }
                    }
                )
            }, bottomBar = {
                BottomNavigationBar(navController = navController)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    studentSeleccionado =
                        Student(id = "", nombre = "", carrera = "", merito = "")
                    mostrarDialogo = true   // AQIO SE ACTIVA EL FORM
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Estudiante")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text(stringResource(R.string.search_placeholder)) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                DropdownMenuCarrera(
                    opciones = carrerasDisponibles,
                    seleccionActual = carreraSeleccionada,
                    onSeleccionar = { carreraSeleccionada = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(estudiantesFiltrados) { estudiante ->
                        val asiento = seatingvm.obtenerAsientoDeEstudiante(estudiante.id)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = if (asiento != null)
                                CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                            else
                                CardDefaults.cardColors()
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        estudiante.nombre,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text("🎓 Carrera: ${estudiante.carrera}")
                                    Text("📚 Matrícula: ${estudiante.id}")
                                    Text("⭐ Mérito: ${estudiante.merito}")
                                    Text("🪑 Asiento: ${asiento?.codigo ?: "Sin asignar"}")
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    IconButton(onClick = {
                                        studentSeleccionado = estudiante
                                        mostrarDialogo = true
                                    }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                                    }
                                         IconButton(onClick = {
                                            studentSeleccionado = estudiante
                                            mostrarDialogoEliminar = true
                                        }) {
                                            Icon(Icons.Default.Delete, contentDescription = "Eliminar asignación")
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }

            studentSeleccionado?.let { estudiante ->
                if (mostrarDialogo && studentSeleccionado != null) {
                    FormStudentEdit(
                        student = estudiante,
                        carrerasDisponibles = studentsvm.obtenerCarrerasDisponibles(),
                        onDismiss = { mostrarDialogo = false },
                        onGuardar = { estudianteEditado ->
                            studentsvm.actualizarEstudiante(estudianteEditado)
                        },
                        onEliminarAsiento = { studentId ->
                            seatingvm.obtenerAsientoDeEstudiante(studentId)?.let { asiento ->
                                seatingvm.desasignarEstudianteDeAsiento(studentId, asiento.codigo)
                            }
                        }
                    )
                }
            }
        }

        ConfirmDeleteDialog(
            visible = mostrarDialogoEliminar,
            onConfirmar = {
                studentSeleccionado?.let {
                    studentsvm.seleccionarEstudiante(it)
                    studentsvm.eliminarEstudianteSeleccionado()
                }
                mostrarDialogoEliminar = false
            },
            onCancelar = { mostrarDialogoEliminar = false }
        )


    }
