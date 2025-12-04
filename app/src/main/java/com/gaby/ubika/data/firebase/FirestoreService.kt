package com.gaby.ubika.data.firebase

import android.util.Log
import com.gaby.ubika.domain.model.Seating
import com.gaby.ubika.domain.model.Student
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

object FirestoreService {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun escucharEstudiantes(onChange: (List<Student>) -> Unit) {
        db.collection("estudiantes").addSnapshotListener { snapshot, e ->
            if (e == null && snapshot != null) {
                onChange(snapshot.toObjects(Student::class.java))
            }
        }
    }

    fun escucharAsientos(onChange: (List<Seating>) -> Unit) {
        db.collection("asientos").addSnapshotListener { snapshot, e ->
            if (e == null && snapshot != null) {
                onChange(snapshot.toObjects(Seating::class.java))
            }
        }
    }

    fun guardarConfiguracionAdmin(
        uid: String,
        nombre: String,
        universidad: String,
        fotoUrl: String?
    ) {
        db.collection("admins").document(uid).set(
            mapOf("nombre" to nombre, "universidad" to universidad, "fotoUrl" to fotoUrl),
            SetOptions.merge()
        )
    }

    fun obtenerConfiguracionAdmin(uid: String, onResult: (String, String, String?) -> Unit) {
        db.collection("admins").document(uid).get()
            .addOnSuccessListener { doc ->
                val nombre = doc.getString("nombre") ?: "Administrador"
                val universidad = doc.getString("universidad") ?: "Universidad"
                val fotoUrl = doc.getString("fotoUrl")
                onResult(nombre, universidad, fotoUrl)
            }
    }

    fun escucharConfiguracionAdmin(uid: String, onChange: (String, String, String?) -> Unit) {
        db.collection("admins").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener
                val nombre = snapshot.getString("nombre") ?: "Administrador"
                val universidad = snapshot.getString("universidad") ?: "Universidad"
                val fotoUrl = snapshot.getString("fotoUrl")
                onChange(nombre, universidad, fotoUrl)
            }
    }


    fun escucharAsientosTiempoReal(onChange: (List<Seating>) -> Unit) {
        db.collection("asientos")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val lista = snapshot.toObjects(Seating::class.java) //Convierte todos los documentos del snapshot en una lista de objetos Seating
                onChange(lista)                  //Se pasa la lista de asientos, por lo tanto se pude actualizar su estado (CallBack)
            }
    }

    fun escucharEstudiantesTiempoReal(onChange: (List<Student>) -> Unit) {
        db.collection("estudiantes")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val lista = snapshot.toObjects(Student::class.java)
                onChange(lista)
            }
    }

    suspend fun actualizarEstudiante(student: Student) { //puede ejecutarse dentro de una coroutine
        try {
            // Guardar estudiante
            db.collection("estudiantes")
                .document(student.id)
                .set(student)
                .await()
            // Si tiene asiento asignado, actualizar también el asiento
            student.asientoAsignado?.let { asientoCodigo -> //keeps it Synchronized
                db.collection("asientos")
                    .document(asientoCodigo)
                    .update("carrera", student.carrera)
                    .await()
            }
            Log.d("Repository", "Estudiante ${student.id} actualizado correctamente")
        } catch (e: Exception) {
            Log.e("Repository", "Error al actualizar estudiante: ${e.message}")
        }
    }

    fun desasignarEstudianteDeAsiento(
        estudianteId: String,
        asientoCodigo: String,
        onComplete: () -> Unit
    ) {
        val db = FirebaseFirestore.getInstance()

        db.collection("asientos")
            .document(asientoCodigo)
            .update("idEstudiante", null)
        db.collection("estudiantes")
            .document(estudianteId)
            .update("asientoAsignado", null)
        onComplete()
    }

    suspend fun actualizarAsiento(seating: Seating) {
        try {
            db.collection("asientos").document(seating.codigo).update(
                mapOf(
                    "idEstudiante" to seating.idEstudiante,
                    "carrera" to seating.carrera,
                    "x" to seating.x,
                    "y" to seating.y
                )
            ).await()
        } catch (e: Exception) {
            Log.e("Repository", "Error al actualizar asiento: ${e.message}")
        }
    }

    fun inicializarPerfilAdminSiNoExiste() {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("admins").document(uid)
        docRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                docRef.set(
                    mapOf(
                        "nombre" to "Administrador",
                        "universidad" to "Universidad Dominico Americano",
                        "fotoUrl" to null
                    )
                )
            }
        }
    }

    fun guardarFechaGraduacion(uid: String, fecha: Long) {
        db.collection("admins").document(uid).set(
            mapOf("graduacionFecha" to fecha),
            SetOptions.merge()
        )
    }

    fun escucharFechaGraduacion(uid: String, onChange: (Long?) -> Unit) {
        db.collection("admins").document(uid)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null || !snapshot.exists()) return@addSnapshotListener
                val fecha = snapshot.getLong("graduacionFecha")
                onChange(fecha)
            }
    }


}