package com.gaby.ubika.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
 import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.gaby.ubika.data.repository.UbikaRepository
import com.gaby.ubika.domain.model.Student
import com.gaby.ubika.domain.use_case.FiltrarEstudiantesUseCase
import kotlinx.coroutines.launch
import androidx.compose.runtime.State
import androidx.lifecycle.AndroidViewModel


 class StudentsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UbikaRepository = UbikaRepository(application.applicationContext)

    private val filtrarEstudiantesUseCase: FiltrarEstudiantesUseCase = FiltrarEstudiantesUseCase()

    private val _cargando = mutableStateOf(true)
    val cargando: State<Boolean> get() = _cargando

    init {
    repository.escucharEstudiantes { estudiantes ->
        _listadoStudents.clear()
        _listadoStudents.addAll(estudiantes)
        _cargando.value = false
    }
}
    private val _listadoStudents = mutableStateListOf<Student>()
    val listadoStudents: SnapshotStateList<Student> get() = _listadoStudents

    var query by mutableStateOf("")
        private set
    var studentSeleccionado by mutableStateOf<Student?>(null)
        private set

    val sugerencias: List<Student>
        get() = filtrarEstudiantesUseCase(listadoStudents, "Todas", query)
    var buscadorExpandido by mutableStateOf(false)
        private set


//------------------------------------------------------------
    fun escucharEstudiantesTiempoReal() {
        repository.escucharEstudiantesTiempoReal { estudiantes ->
            _listadoStudents.clear()
            _listadoStudents.addAll(estudiantes)
        }
    }
    fun obtenerEstudiantePorId(id: String?): Student? {
        return listadoStudents.find { it.id == id }
    }
    fun filtrarEstudiantes(carrera: String, query: String): List<Student> {
        return filtrarEstudiantesUseCase(listadoStudents, carrera, query)
    }
    fun actualizarEstudiante(student: Student) {
        viewModelScope.launch {
            repository.actualizarEstudiante(student)
        }
    }
     fun eliminarEstudianteSeleccionado() {
         studentSeleccionado?.let { estudiante ->
             viewModelScope.launch {
                 repository.eliminarEstudiante(estudiante.id) { exito ->
                     if (exito) {
                         _listadoStudents.removeAll { it.id == estudiante.id }
                     }
                     cerrarPopup()
                 }
             }
         }
     }

    fun seleccionarEstudiante(student: Student) {
        studentSeleccionado = student
    }
    fun cerrarPopup() {
        studentSeleccionado = null
    }
    fun onQueryChange(newQuery: String) {
        query = newQuery
    }
    fun obtenerCarrerasDisponibles(): List<String> {
        return listOf("Sin carrera") + listadoStudents
            .map { it.carrera }
            .distinct()
            .sorted()
    }
    fun expandirBuscador() {
        buscadorExpandido = true
    }

    fun contraerBuscador() {
        buscadorExpandido = false
    }





}
