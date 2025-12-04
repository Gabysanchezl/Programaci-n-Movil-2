package com.gaby.ubika.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavController
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import com.gaby.ubika.R
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gaby.ubika.domain.model.Student
import com.gaby.ubika.ui.components.OcupationBar_Carreers
import com.gaby.ubika.ui.components.AdminHeader
import com.gaby.ubika.ui.components.GraduationDateCardButton
import com.gaby.ubika.ui.components.StudentPopUp
import com.gaby.ubika.viewmodels.AdminViewModel
import com.gaby.ubika.viewmodels.NotificationViewModel
import com.gaby.ubika.viewmodels.SeatingViewModel
 import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.gaby.ubika.viewmodels.StudentsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHomeScreen(navController: NavController) {
//FECHA
    val studentsviewmodel: StudentsViewModel = viewModel()
    val listadoStudents = studentsviewmodel.listadoStudents
    val seatingViewModel: SeatingViewModel = viewModel()
    val adminViewModel: AdminViewModel = viewModel()
    val notificationvm: NotificationViewModel = viewModel()

    LaunchedEffect(Unit) {
        adminViewModel.inicializarPerfilAdminSiNoExiste()
        adminViewModel.escucharConfiguracionAdmin()
    }

    ////
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AdminHeader(adminViewModel) },
                 colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                 .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
             item { SearchSection(viewModel = studentsviewmodel,
                 carrerasDisponibles = adminViewModel.carrerasDisponibles,
                 onEditar = { estudianteEditado ->
                     studentsviewmodel.actualizarEstudiante(estudianteEditado)
                 },
                 onEliminar = {
                     studentsviewmodel.eliminarEstudianteSeleccionado()
                     }
             )
             }
            item {Spacer(modifier = Modifier.height(32.dp))}


            item { GraduationDateCardButton(
                admivm = adminViewModel,
                notificationvm = notificationvm
            )}
            item { Spacer(modifier = Modifier.height(32.dp))}

            item {SeatingMap {
                navController.navigate("plano_asientos")
            }}

            item { Spacer(modifier = Modifier.height(32.dp))}

            item {Text(
                stringResource(R.string.gestionar),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 14.dp, end = 14.dp)
            )}

            item { Spacer(modifier = Modifier.height(16.dp))}

            item {Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                GestionCardVertical(
                    titulo = stringResource(R.string.estudiantes),
                    descripcion = stringResource(R.string.descripcion_students_card),
                    imageId = R.drawable.ic_estudiante,
                    onClick = { navController.navigate("lista_estudiantes") },
                    modifier = Modifier.weight(1f)
                )
            }}
                item { Spacer(modifier = Modifier.height(40.dp)) }
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), // padding interno para aire
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.ocupacion_asientos_title),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        OcupationBar_Carreers(
                            asignadosPorCarrera = seatingViewModel.listadoSeatings
                                .filter { it.idEstudiante != null && !it.carrera.isNullOrBlank() }
                                .groupingBy { it.carrera!! }
                                .eachCount(),
                            listadoStudents = listadoStudents,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)   //i knooooow this is exp
@Composable
fun SearchSection(
    viewModel: StudentsViewModel,
    carrerasDisponibles: List<String>,
    onEditar: (Student) -> Unit,
    onEliminar: () -> Unit
) {
     val query = viewModel.query
    val sugerencias = viewModel.sugerencias
     val buscadorExpandido = viewModel.buscadorExpandido

    ExposedDropdownMenuBox(
        expanded = buscadorExpandido,
        onExpandedChange = { expanded ->
            if (expanded) viewModel.expandirBuscador() else viewModel.contraerBuscador()
        }
    ){
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.onQueryChange(it) },
                placeholder = { Text(stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
        ExposedDropdownMenu(
            expanded = buscadorExpandido,
            onDismissRequest = { viewModel.contraerBuscador() },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
        ) {
            sugerencias.forEach { estudiante ->
                DropdownMenuItem(
                    modifier = Modifier.padding(15.dp),
                    text = { Column {
                        Text(
                            text = estudiante.nombre,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "ID: ${estudiante.id}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                           },
                    onClick = {
                        viewModel.seleccionarEstudiante(estudiante)
                        viewModel.contraerBuscador()
                    }
                )
            }
        }
    }
        // Popup reutilizable
        viewModel.studentSeleccionado?.let { estudiante ->
            StudentPopUp(
                student = estudiante,
                carrerasDisponibles = carrerasDisponibles,
                onDismiss = { viewModel.cerrarPopup() },
                onEditar = onEditar,
                onEliminar = onEliminar
            )
        }
    }


@Composable
fun SeatingMap(onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.asignar_asientos_title),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = stringResource(R.string.asignar_asientos_description),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF0A0700)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Image(
                painter = painterResource(id = R.drawable.preview_teatro),
                contentDescription = stringResource(R.string.image_description),
                modifier = Modifier.size(130.dp)
            )
        }
    }

}

@Composable
fun GestionCardVertical(titulo: String, descripcion: String, imageId: Int, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.95f else 1f,
        animationSpec = tween(durationMillis = 150)
    )  //mi animacion de push :v
    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier
            .height(160.dp)
            .scale(scale) // aqui se aplica mi escala "animada"
            .clickable {
                scope.launch {
                    pressed = true
                    delay(100)
                    pressed = false
                    onClick()
                }
            },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF1E1201),
                    maxLines = 3
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = imageId),
                contentDescription = titulo,
                modifier = Modifier
                    .size(100.dp)
            )
        }

    }
}

