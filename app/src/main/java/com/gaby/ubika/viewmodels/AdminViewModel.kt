package com.gaby.ubika.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
 import androidx.lifecycle.viewModelScope
import com.gaby.ubika.data.firebase.AuthService
 import com.gaby.ubika.data.repository.UbikaRepository
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UbikaRepository = UbikaRepository(application.applicationContext)

    var procesando by mutableStateOf(false)
        private set
    var nombreAdmin by mutableStateOf("")
        private set
    var nombreUniversidad by mutableStateOf("")
    var fotoPerfilUrl by mutableStateOf<String?>(null)
        private set
    var fechaGraduacion by mutableStateOf<Long?>(null)
        private set
    var isUploading by mutableStateOf(false)
        private set

    init {
        val uid = AuthService.getUid() //obtenemos inmediatamente el uid del usuario logueado

        if (uid != null) {  //Si no hay usuario, uid será null
            repository.escucharConfiguracionAdmin(uid) { nombre, universidad, fotoUrl ->
                nombreAdmin = nombre
                nombreUniversidad = universidad
                fotoPerfilUrl = fotoUrl
            }
            repository.escucharFechaGraduacion(uid) { fecha ->
                fechaGraduacion = fecha
            }
        } else {
            Log.e("AdminViewModel", "User not logged in")
        }
    }

    fun inicializarPerfilAdminSiNoExiste() {
        repository.inicializarPerfilAdminSiNoExiste()
    }
//new
fun actualizarConfiguracionAdmin(nombre: String, universidad: String, fotoUrl: String?) {
    val uid = AuthService.getUid() ?: return
    repository.guardarConfiguracionAdmin(uid, nombre, universidad, fotoUrl)

    // Actualizar el estado local del ViewModel para que Compose lo observe
    nombreAdmin = nombre
    nombreUniversidad = universidad
}
    val carrerasDisponibles: List<String> = listOf(
        "Administración",
        "Cybersecurity",
        "Administracion",
        "Educacion"
        )
    fun escucharConfiguracionAdmin() {
        val uid = AuthService.getUid() ?: return
        repository.obtenerConfiguracionAdmin(uid) { nombre, universidad, fotoUrl ->
            nombreAdmin = nombre
            nombreUniversidad = universidad
            fotoPerfilUrl = fotoUrl

        }
    }

    fun subirFotoPerfil(uri: Uri, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            try {
                isUploading = true
                val url = repository.subirFotoPerfil(uri)
                if (url != null) {
                    fotoPerfilUrl = url
                    actualizarConfiguracionAdmin(nombreAdmin, nombreUniversidad, url)
                    onSuccess(url)
                } else {
                    Log.e("AdminViewModel", "Error subiendo foto")
                }
            } finally {
                isUploading = false
            }
        }
    }

    fun actualizarFechaGraduacion(fecha: Long) {
        val uid = AuthService.getUid() ?: return
        repository.guardarFechaGraduacion(uid, fecha)
    }

}
