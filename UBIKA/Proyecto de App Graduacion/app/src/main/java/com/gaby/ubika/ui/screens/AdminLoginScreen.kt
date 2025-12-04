package com.gaby.ubika.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.gaby.ubika.R
import com.gaby.ubika.data.firebase.AuthService
 import com.gaby.ubika.viewmodels.AdminLoginViewModel

@Composable
fun AdminLoginScreen(navController: NavController) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    val context = LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }
    var correoRecuperacion by remember { mutableStateOf(correo) }
    var rememberMe by remember { mutableStateOf(false) }
    val adminloginViewModel: AdminLoginViewModel = viewModel()
    val uiState = adminloginViewModel.uiState

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo1),
            contentDescription = stringResource(R.string.fondo_decorativo),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        TextButton(
            onClick = { navController.navigate("main_menu")},
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
                    tint = colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp)) // espacio entre icono y texto
                Text(
                    text = "Volver",
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onPrimary
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.admin_login_header),
                        style = MaterialTheme.typography.headlineSmall,
                        color = colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text(stringResource(R.string.email_label), color = colorScheme.onSurface) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f),
                            focusedLabelColor = colorScheme.primary,
                            unfocusedLabelColor = colorScheme.onSurface.copy(alpha = 0.5f),
                            cursorColor = colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = contrasena,
                        onValueChange = { contrasena = it },
                        label = { Text(stringResource(R.string.password_label), color = colorScheme.onSurface) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.onSurface.copy(alpha = 0.5f),
                            focusedLabelColor = colorScheme.primary,
                            unfocusedLabelColor = colorScheme.onSurface.copy(alpha = 0.5f),
                            cursorColor = colorScheme.primary
                        )
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it }
                        )
                        Text("Recordarme", style = MaterialTheme.typography.bodyMedium)
                    }
                    //   +++++++++  Recuperar contraseña
                    Spacer(modifier = Modifier.height(10.dp))

                    LaunchedEffect(Unit) {
                        adminloginViewModel.loadSavedCredentials()
                    }

                    Button(
                        onClick = {
                            adminloginViewModel.login(
                                correo,
                                contrasena,
                                rememberMe,
                                onSuccess = {
                                    navController.navigate("admin_home") {
                                        popUpTo("admin_login") { inclusive = true }
                                        launchSingleTop = true
                                    }
                                },
                                onError = { mensaje ->
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 20.dp, end=20.dp)
                            .height(60.dp),
                        shape = RoundedCornerShape(8.dp),

                        enabled = !uiState.procesando, // deshabilita mientras procesa
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorScheme.primary,
                            contentColor = colorScheme.onPrimary
                        )
                    ) {
                         Text(
                            text = "Iniciar sesión",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(5.dp))

                    TextButton(onClick = { showResetDialog = true }) {
                        Text(stringResource(R.string.forget_password), color = colorScheme.primary)
                    }
                }
            }
            if (showResetDialog) {
                AlertDialog(
                    onDismissRequest = { showResetDialog = false },
                    title = { Text(stringResource(R.string.recuperar_contrasena)) },
                    text = {
                        Column {
                            Text(stringResource(R.string.recuperar_contrasena_description))
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = correoRecuperacion,
                                onValueChange = { correoRecuperacion = it },
                                label = {  Text(stringResource(R.string.email_label)) },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            if (correoRecuperacion.isBlank()) {
                                Toast.makeText(context,
                                    context.getString(R.string.putemail_toast), Toast.LENGTH_SHORT).show()
                            } else {
                                AuthService.enviarCorreoRecuperacion(correoRecuperacion) { ok ->
                                    if (ok) {
                                        Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_LONG).show()
                                        showResetDialog = false
                                    } else {
                                        Toast.makeText(context, "Error al enviar el correo", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }) { Text("Enviar") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showResetDialog = false }) { Text(stringResource(R.string.cancelar )) }
                    }
                )
            }
        }
    }
}
