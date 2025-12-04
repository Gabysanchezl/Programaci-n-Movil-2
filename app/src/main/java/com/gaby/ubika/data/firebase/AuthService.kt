package com.gaby.ubika.data.firebase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
 import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthService {
    var procesando by mutableStateOf(false)
        private set

    val auth = FirebaseAuth.getInstance()
    fun getCurrentUser() = FirebaseAuth.getInstance().currentUser
    fun isLoggedIn(): Boolean = getCurrentUser() != null

    fun getUid(): String? = auth.currentUser?.uid

    fun enviarCorreoRecuperacion(correo: String, onResult: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener { onResult(it.isSuccessful) }
    }


    fun login(correo: String, contrasena: String, onResult: (Boolean, FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, auth.currentUser)
                } else {
                    onResult(false, null)
                }
            }
    }

    fun logout() {
        auth.signOut()
    }
    fun cambiarContrasena(actualContrasena: String, nuevaContrasena: String, onResult: (Boolean) -> Unit) {
        if (procesando) return
        procesando = true
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, actualContrasena)
            user.reauthenticate(credential).addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    user.updatePassword(nuevaContrasena)
                        .addOnCompleteListener { updateTask ->
                            procesando = false
                            onResult(updateTask.isSuccessful)
                        }
                } else {
                    procesando = false
                    onResult(false)
                }
            }
        } else {
            procesando = false
            onResult(false)
        }
    }
}
