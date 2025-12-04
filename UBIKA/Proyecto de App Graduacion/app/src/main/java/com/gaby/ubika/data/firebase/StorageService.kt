package com.gaby.ubika.data.firebase

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
 import com.google.firebase.storage.FirebaseStorage
 import kotlinx.coroutines.tasks.await

object StorageService {

    suspend fun subirFotoPerfil(uri: Uri, uid: String): String? {  //as it is suspend allows me to use coroutines y await
        return try {
            val storageRef = FirebaseStorage.getInstance().reference
            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return null         // si no hay usuario auth no se sube nada ' pues who will it belong to? nobody
            val fileRef = storageRef.child("profile_pics/$uid.jpg")  //define la ruta de mi foto en firebase

            fileRef.putFile(uri).await()  //sube mi foto desde el uri a Firebase Storage
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            Log.e("Repository", "Error subiendo foto: ${e.message}", e)
            null
        }
    }

}


// uri: la referencia local al archivo