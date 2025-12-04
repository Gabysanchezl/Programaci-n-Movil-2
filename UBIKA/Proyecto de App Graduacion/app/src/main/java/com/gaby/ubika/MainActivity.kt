package com.gaby.ubika

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.gaby.ubika.ui.navigation.AppNavigation
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gaby.ubika.ui.theme.UbikaThemee
 import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        FirebaseApp.initializeApp(this)
        setContent {
            UbikaThemee {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(navController)
                }

            }

        }
    }
}

//refactor
    //funcion para subir mis datos a firebase
//    private fun inicializarDatos() {
//
//        Log.d("MainActivity", "Entrando a inicializarDatos...")
//
//        val db = Firebase.firestore
//
//        db.collection("estudiantes").get()
//            .addOnSuccessListener { snapshot ->
//
//                Log.d("MainActivity", "Consulta Firestore exitosa, documentos: ${snapshot.size()}")
//
//
//                   val estudiantes = repository.precargarEstudiantesMock(this@MainActivity)
//                    val asientos = repository.precargarAsientosMock()
//                    subirEstudiantesYAsientos(estudiantes, asientos)
//                    verificarSubida()
//                  }
//            .addOnFailureListener { e ->
//                Log.e("MainActivity", "Error verificando colección", e)        }
//    }



//private fun verificarSubida() {
//    val db = Firebase.firestore
//
//    db.collection("estudiantes").get()
//        .addOnSuccessListener { snapshot ->
//            Log.d("Verificacion", "Estudiantes en Firestore: ${snapshot.size()}")
//        }
//
//    db.collection("asientos").get()
//        .addOnSuccessListener { snapshot ->
//            Log.d("Verificacion", "Asientos en Firestore: ${snapshot.size()}")
//        }
//}

