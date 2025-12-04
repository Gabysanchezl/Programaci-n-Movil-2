package com.gaby.ubika.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
 import androidx.compose.runtime.setValue
 import com.gaby.ubika.data.repository.UbikaRepository
 import androidx.lifecycle.AndroidViewModel

class AdminLoginViewModel(application: Application) : AndroidViewModel(application) {
    private val adminRepository = UbikaRepository(application.applicationContext)
    var uiState by mutableStateOf(LoginUiState())
        private set

    fun login(
        correo: String,
        contrasena: String,
        rememberMe: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (correo.isBlank() || contrasena.isBlank()) {
            onError("Completa ambos campos")
            return
        }
        if (uiState.procesando) return

        uiState = uiState.copy(procesando = true)

        adminRepository.login(correo, contrasena) { taskExitosa, user ->
            if (taskExitosa && user != null) {
                val uid = user.uid
                adminRepository.inicializarPerfilAdminSiNoExiste()
                adminRepository.escucharConfiguracionAdmin(uid) { nombre, correo, telefono ->
                    uiState = uiState.copy(
                        nombre = nombre,
                        correo = correo,
                        telefono = telefono
                    )
                }
                if (rememberMe){
                    adminRepository.saveCredentials(correo, contrasena)
                }
                onSuccess()
            } else {
                onError("Credenciales inválidas")
            }
            uiState = uiState.copy(procesando = false)
        }
    }
    fun loadSavedCredentials() {
        val (savedCorreo, savedContrasena) = adminRepository.getSavedCredentials()
        uiState = uiState.copy(correo = savedCorreo, contrasena = savedContrasena)
    }
}

data class LoginUiState(
    val procesando: Boolean = false,
    val nombre: String? = null,
    val correo: String? = null,
    val contrasena: String? = null,
    val telefono: String? = null
)