package com.gaby.ubika.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gaby.ubika.R
import com.gaby.ubika.viewmodels.SeatingViewModel
import com.gaby.ubika.viewmodels.StudentsViewModel

@Composable
fun ConsultingSeat_Screen(navController: NavController) {
    var matricula by remember { mutableStateOf("") }

    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography
    val seatingvm: SeatingViewModel = viewModel ()
    val studentsvm: StudentsViewModel = viewModel()
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 5.dp, top = 20.dp)
    ) {
        TextButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 30.dp, start = 5.dp)
                .align(Alignment.TopStart),
            colors = ButtonDefaults.textButtonColors(
                contentColor = colorScheme.onBackground
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(8.dp)) // espacio entre icono y texto
                Text(
                    text = "Volver",
                    style = MaterialTheme.typography.labelLarge,
                    color = colorScheme.onBackground
                )
            }
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.consultar_asiento ),
                style = typography.headlineMedium,
                color = colorScheme.primary
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = matricula,
                onValueChange = { matricula = it },
                label = {
                    Text(
                        text = stringResource(R.string.matricula),
                        style = typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                textStyle = typography.bodyMedium.copy(textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (matricula.isBlank()) {
                        Toast.makeText(context, "Por favor ingresa una matrícula", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val estudiante = studentsvm.obtenerEstudiantePorId(matricula)
                    val asiento = seatingvm.obtenerAsientoPorMatricula(matricula)

                    if (estudiante  != null && asiento  != null) {
                        val nombre = estudiante.nombre
                        val codigo = asiento.codigo

                        val encodedNombre = Uri.encode(nombre)
                        val encodedCodigo = Uri.encode(codigo)

                        navController.navigate("resultado_consulta/$encodedNombre/$encodedCodigo")
                    } else {
                        Toast.makeText(context, "No se encontró asignación para esta matrícula", Toast.LENGTH_SHORT).show()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end=20.dp)
                    .height(48.dp)
            ) {
                Text("Consultar", style = typography.bodyLarge)
            }

        }
    }
}
