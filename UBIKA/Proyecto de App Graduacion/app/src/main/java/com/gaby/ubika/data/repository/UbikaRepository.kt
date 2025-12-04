package com.gaby.ubika.data.repository

import android.net.Uri
import com.gaby.ubika.data.firebase.AuthService
import com.gaby.ubika.data.firebase.FirestoreService
import com.gaby.ubika.data.firebase.StorageService
import com.gaby.ubika.domain.model.Seating
import com.gaby.ubika.domain.model.Student
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class UbikaRepository (private val context: Context){
     private val prefs = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    //FirestoreService
    fun escucharEstudiantes(onChange: (List<Student>) -> Unit) =
        FirestoreService.escucharEstudiantes(onChange)
    fun escucharAsientos(onChange: (List<Seating>) -> Unit) =
        FirestoreService.escucharAsientos(onChange)

    fun escucharConfiguracionAdmin(uid: String, onChange: (String, String, String?) -> Unit) {
        FirestoreService.escucharConfiguracionAdmin(uid, onChange)
    }
    fun obtenerConfiguracionAdmin(uid: String, onResult: (String, String, String?) -> Unit)=
    FirestoreService.obtenerConfiguracionAdmin(uid, onResult)

    fun guardarConfiguracionAdmin(uid: String, nombre: String, universidad: String, fotoUrl: String?) {
        FirestoreService.guardarConfiguracionAdmin(uid, nombre, universidad, fotoUrl)
    }

    fun escucharFechaGraduacion(uid: String, onChange: (Long?) -> Unit) {
        FirestoreService.escucharFechaGraduacion(uid, onChange)
    }
    fun eliminarEstudiante(id: String, onResult: (Boolean) -> Unit = {}) {
        firestore.collection("estudiantes")
            .document(id)
            .delete()
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
    fun guardarFechaGraduacion(uid: String, fecha: Long) {
        FirestoreService.guardarFechaGraduacion(uid, fecha)
    }
    fun escucharAsientosTiempoReal(onChange: (List<Seating>) -> Unit)=
        FirestoreService.escucharAsientosTiempoReal(onChange)
    fun escucharEstudiantesTiempoReal(onChange: (List<Student>) -> Unit)=
        FirestoreService.escucharEstudiantesTiempoReal(onChange)
    suspend fun actualizarEstudiante(student: Student)=
        FirestoreService.actualizarEstudiante(student)
     fun desasignarEstudianteDeAsiento(estudianteId: String, asientoCodigo: String, onComplete: () -> Unit) =
        FirestoreService.desasignarEstudianteDeAsiento(estudianteId, asientoCodigo, onComplete)
    suspend fun actualizarAsiento(seating: Seating) =
        FirestoreService.actualizarAsiento(seating)
    fun inicializarPerfilAdminSiNoExiste() =
        FirestoreService.inicializarPerfilAdminSiNoExiste()

/// AuthService
    suspend fun subirFotoPerfil(uri: Uri): String? {
        val uid = AuthService.getUid() ?: return null
        return StorageService.subirFotoPerfil(uri, uid)
    }
    fun enviarCorreoRecuperacion(correo: String, onResult: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun login(correo: String, contrasena: String, onResult: (Boolean, FirebaseUser?) -> Unit) {
        AuthService.login(correo, contrasena, onResult)
    }

    fun saveCredentials(correo: String, contrasena: String) {
        prefs.edit()
            .putString("correo", correo)
            .putString("contrasena", contrasena)
            .apply()
    }

    fun getSavedCredentials(): Pair<String, String> {
        val correo = prefs.getString("correo", "") ?: ""
        val contrasena = prefs.getString("contrasena", "") ?: ""
        return Pair(correo, contrasena)
    }

}