package com.gaby.ubika.ui.screens

import android.net.Uri
import android.os.Build
import android.widget.Toast
import coil.compose.AsyncImage
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
 import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gaby.ubika.R
import com.gaby.ubika.ui.components.PasswordChangeDialog
import com.gaby.ubika.ui.components.CardCarreersSeating
import com.gaby.ubika.ui.components.Confirm_ClosingSesion_Dialog
import com.gaby.ubika.ui.components.InfoCardSeating
import com.gaby.ubika.ui.components.ColorbyCarreer
 import com.gaby.ubika.viewmodels.AdminViewModel
import com.gaby.ubika.viewmodels.NotificationViewModel
import com.gaby.ubika.viewmodels.SeatingViewModel
import com.gaby.ubika.viewmodels.StudentsViewModel
import com.google.firebase.auth.FirebaseAuth



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminProfileScreen(navController: NavController) {

    val admivm: AdminViewModel = viewModel()
    val studentsvm: StudentsViewModel = viewModel()
    val notificationvm: NotificationViewModel = viewModel()
    val seatingvm: SeatingViewModel = viewModel()
    val context = LocalContext.current
    var tempUri by remember { mutableStateOf<Uri?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    val totalDisponibles = seatingvm.listadoSeatings.count { it.idEstudiante == null }
    var showDatePicker by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            tempUri = it
            showConfirmDialog = true
        }
    }
    // Diálogo de confirmación
    if (showConfirmDialog && tempUri != null) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Confirmar imagen") },
            text = {
                AsyncImage(
                    model = tempUri,
                    contentDescription = "Preview",
                    modifier = Modifier
                        .size(200.dp)
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    admivm.subirFotoPerfil(tempUri!!) {
                        Toast.makeText(context, "Foto actualizada", Toast.LENGTH_SHORT).show()
                    }
                    showConfirmDialog = false
                }) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
    var editNombre by remember { mutableStateOf(false) }
    var editUni by remember { mutableStateOf(false) }

    // Buffers de edición que se inicializan con el valor actual del VM
    var nombreBuffer by remember(admivm.nombreAdmin) { mutableStateOf(admivm.nombreAdmin) }
    var universidadBuffer by remember(admivm.nombreUniversidad) { mutableStateOf(admivm.nombreUniversidad) }

    val asientos = seatingvm.listadoSeatings
    val asignados = asientos.filter { it.idEstudiante != null }
    val asignadosPorCarrera: Map<String, Int> = asignados
        .filter { !it.carrera.isNullOrBlank() }
        .groupingBy { it.carrera!! }
        .eachCount()
    val totalAsignadosFiltrados = asignadosPorCarrera.values.sum()
    var mostrarDialogoCerrarSesion by remember { mutableStateOf(false) }

    val carreras: List<String> = asignadosPorCarrera.keys.filterNot { it.isBlank() }
    var showDialog by remember { mutableStateOf(false) }


    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 50.dp, start = 20.dp, end = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Card principal marrón
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF795548)), // Marrón
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .aspectRatio(1f)
                                .border(2.dp, Color(0xFFFFF3E0), CircleShape)
                                .clickable { launcher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                admivm.isUploading -> {
                                    CircularProgressIndicator(modifier = Modifier.size(48.dp))
                                }

                                admivm.fotoPerfilUrl != null -> {
                                    AsyncImage(
                                        model = admivm.fotoPerfilUrl,
                                        contentDescription = "Foto de perfil",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                else -> {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = "Agregar foto",
                                        tint = Color(0xFFFFF3E0),
                                        modifier = Modifier.size(64.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
 // Nombre del admin
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!editNombre) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = admivm.nombreAdmin,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "Administrador",
                                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        nombreBuffer = admivm.nombreAdmin
                                        editNombre = true},
                                    modifier = Modifier.size(15.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Editar nombre",
                                        tint = Color.White.copy(alpha = 0.5f)
                                    )
                                }
                            } else {
                                OutlinedTextField(
                                    value = nombreBuffer,
                                    onValueChange = { nombreBuffer  = it },
                                    singleLine = true,
                                    label = { Text("Nombre") },
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    admivm.actualizarConfiguracionAdmin(
                                        nombreBuffer,
                                        admivm.nombreUniversidad,
                                        admivm.fotoPerfilUrl
                                    )
                                    editNombre = false
                                    Toast.makeText(
                                        context,
                                        "Nombre actualizado",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Guardar",
                                        tint = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

// Universidad dentro del card
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = stringResource(R.string.universidad ),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.White.copy(
                                            alpha = 0.9f
                                        )
                                    )
                                )
                                Text(
                                    text = admivm.nombreUniversidad,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            IconButton(
                                onClick = { editUni = true },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Editar universidad",
                                    tint = Color.White.copy(alpha = 0.5f)
                                )
                            }
                        }

                        if (editUni) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = admivm.nombreUniversidad,
                                    onValueChange = { admivm.nombreUniversidad = it },
                                    singleLine = true,
                                    label = { Text("Universidad") },
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    admivm.actualizarConfiguracionAdmin(
                                        admivm.nombreAdmin,
                                        universidadBuffer,
                                        admivm.fotoPerfilUrl
                                    )
                                    editUni = false
                                    Toast.makeText(
                                        context,
                                        "Universidad actualizada",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Guardar",
                                        tint = Color.White
                                    )
                                }
                            }
                        }


                        TextButton(
                            onClick = { showDialog = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.Start)
                        ) {
                            Text(
                                text = "Cambiar contraseña",
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Light
                                )
                            )
                        }
                        PasswordChangeDialog(
                            showDialog = showDialog,
                            onDismiss = { showDialog = false }
                        )

                    }
                }
            }


            // Dos cards: Asientos disponibles / Asientos asignados
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InfoCardSeating(
                        tituloSuperior = stringResource(R.string.asientos),
                        tituloPrincipal = stringResource(R.string.disponibles),
                        valor = totalDisponibles,
                        colorTexto = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    InfoCardSeating(
                        tituloSuperior = stringResource(R.string.asientos),
                        tituloPrincipal = stringResource(R.string.asignados),
                        valor = totalAsignadosFiltrados,
                        colorTexto = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }


            // Sección: Asientos asignados (por carrera)
            item {
                Text(
                    text = stringResource(R.string.asientos_asignados),
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            items(carreras) { carrera: String ->
                val asignadosCarrera = asignadosPorCarrera[carrera] ?: 0
                val listadoStudents = studentsvm.listadoStudents

                val totalEstudiantesCarrera = listadoStudents
                    .filter { it.carrera.isNotBlank() }
                    .count { it.carrera == carrera }

                val faltan = (totalEstudiantesCarrera - asignadosCarrera).coerceAtLeast(0)

                CardCarreersSeating(
                    carrera = carrera,
                    asignados = asignadosCarrera,
                    faltanPorAsignar = faltan,
                    colorCarrera = ColorbyCarreer(carrera)
                )

            }

            // Cerrar sesión
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.cerrar_sesion),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textDecoration = TextDecoration.Underline
                        )
                    )
                    IconButton(onClick = {
                        mostrarDialogoCerrarSesion = true
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }

                Confirm_ClosingSesion_Dialog(
                    visible = mostrarDialogoCerrarSesion,
                    onConfirmar = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("admin_login") {
                            popUpTo("admin_home") { inclusive = true }
                        }
                        mostrarDialogoCerrarSesion = false
                    },
                    onCancelar = { mostrarDialogoCerrarSesion = false }
                )

            }
        }


    }
}




