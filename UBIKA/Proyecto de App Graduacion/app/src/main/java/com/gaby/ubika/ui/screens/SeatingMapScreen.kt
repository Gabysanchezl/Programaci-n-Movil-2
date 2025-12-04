@file:SuppressLint("UnusedBoxWithConstraintsScope")
package com.gaby.ubika.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gaby.ubika.domain.model.Seating
 import kotlinx.coroutines.launch
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
 import com.gaby.ubika.R
 import com.gaby.ubika.ui.components.CareersColorLegend
import com.gaby.ubika.ui.components.PopupGestionSeating
import com.gaby.ubika.viewmodels.SeatingViewModel
import com.gaby.ubika.viewmodels.StudentsViewModel

@Composable
fun SeatingMapScreen(
    navController: NavController,
 ) {
    val seatingvm: SeatingViewModel  = viewModel()
    val studentsvm: StudentsViewModel = viewModel()

    LaunchedEffect(Unit) {
        seatingvm.escucharAsientosTiempoReal()
        studentsvm.escucharEstudiantesTiempoReal()
    }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val listadoAsientos = seatingvm.listadoSeatings
    val listadoStudents = studentsvm.listadoStudents

    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var seatingSeleccionado by remember { mutableStateOf<Seating?>(null) }


///   CONTENIDO

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.6f))
    ) {
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(30.dp)
                .zIndex(10f)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Cerrar",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.medium
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, _ ->
                            scale = (scale * zoom).coerceIn(0.5f, 4f)
                            offset += pan
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.teatro01),
                    contentDescription = "Plano del Teatro",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )

                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val density = LocalDensity.current
                    val screenWidth = constraints.maxWidth.toFloat()
                    val screenHeight = constraints.maxHeight.toFloat()

                    listadoAsientos.forEach { asiento ->
                             val colorAsiento = seatingvm.obtenerColorAsiento(asiento, MaterialTheme.colorScheme)

                        val puntito = seatingvm.calcularPuntitoMapa(asiento, screenWidth, screenHeight, density)
                        val offsetX = puntito.x.dp
                        val offsetY = puntito.y.dp

                        Box(
                            modifier = Modifier
                                .offset(x = offsetX, y = offsetY)
                                .size(15.dp)
                                .background(colorAsiento, shape = CircleShape)
                                .clickable {
                                    seatingSeleccionado = asiento
                                }
                        )
                    }
                }

                seatingSeleccionado?.let { asiento ->
                    val estudiantesAsignadosIds = listadoAsientos.mapNotNull { it.idEstudiante }.toSet()

                    val estudiantesDisponiblesFiltrados = listadoStudents.filter {
                        // Filtra los estudiantes disponibles: solo los que NO están asignados o el que ya está asignado a este asiento.
                        it.id == asiento.idEstudiante || it.id !in estudiantesAsignadosIds
                    }

                    PopupGestionSeating(
                        seating = asiento,
                        estudiantesDisponibles = estudiantesDisponiblesFiltrados,
                        onDismiss = { seatingSeleccionado = null },
                        onActualizarAsiento = { estudianteSeleccionado, carreraSeleccionada ->
                            coroutineScope.launch {
                                if (estudianteSeleccionado != null) {
                                    // 1. Actualiza asiento con el ID del estudiante
                                    seatingvm.asignarEstudianteAAsiento(
                                        student = estudianteSeleccionado,
                                        seating = asiento
                                    )
                                } else {
                                    // Desasignar estudiante del asiento
                                    seatingvm.desasignarEstudianteDeAsiento(
                                        estudianteId = asiento.idEstudiante ?: "",
                                        asientoCodigo = asiento.codigo
                                    )
                                }
                                seatingSeleccionado = null
                                Toast.makeText(
                                    context,
                                    if (estudianteSeleccionado != null)
                                        "Asiento asignado a ${estudianteSeleccionado.nombre}"
                                    else
                                        "Asiento liberado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .zIndex(10f)
        ) {
            CareersColorLegend()
        }
    }
}
