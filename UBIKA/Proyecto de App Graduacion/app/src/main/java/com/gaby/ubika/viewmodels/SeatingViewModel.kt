package com.gaby.ubika.viewmodels

 import android.app.Application
 import android.content.Context
 import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
 import androidx.lifecycle.AndroidViewModel
 import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gaby.ubika.data.repository.UbikaRepository
import com.gaby.ubika.domain.model.Seating
import com.gaby.ubika.domain.model.SeatingStatus
import com.gaby.ubika.domain.model.Student
import com.gaby.ubika.domain.use_case.AsignarAsientoUseCase
 import com.google.firebase.firestore.FirebaseFirestore
 import kotlinx.coroutines.launch

class SeatingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UbikaRepository = UbikaRepository(application.applicationContext)
    private val asignarAsientoUseCase: AsignarAsientoUseCase = AsignarAsientoUseCase()

    private val firestore = FirebaseFirestore.getInstance()

    init {
        repository.escucharAsientos { asientos ->
            _listadoSeatings.clear()
            _listadoSeatings.addAll(asientos)
        }
    }
    private val _listadoSeatings = mutableStateListOf<Seating>()
    val listadoSeatings: SnapshotStateList<Seating> get() = _listadoSeatings

    fun escucharAsientosTiempoReal() {
        repository.escucharAsientosTiempoReal { asientos ->
            _listadoSeatings.clear()
            _listadoSeatings.addAll(asientos)
        }
    }
    fun asignarEstudianteAAsiento(student: Student, seating: Seating) {
        val asientoActualizado = asignarAsientoUseCase(student, seating)
        val estudianteActualizado = student.copy(asientoAsignado = seating.codigo)

        viewModelScope.launch {
            repository.actualizarAsiento(asientoActualizado)
            repository.actualizarEstudiante(estudianteActualizado)
        }
    }

    fun desasignarEstudianteDeAsiento(estudianteId: String, asientoCodigo: String) {
        repository.desasignarEstudianteDeAsiento(estudianteId, asientoCodigo) {
        }
    }
    fun obtenerAsientoDeEstudiante(id: String): Seating? {
        return listadoSeatings.find { it.idEstudiante == id }
    }
    fun obtenerColorAsiento(seating: Seating, colorScheme: ColorScheme): androidx.compose.ui.graphics.Color {
        val baseColor = when (seating.carrera) {
            "Software" -> Color(0xFF2196F3)
            "Educacion" -> Color(0xFF427900)
            "Administracion" -> Color(0xFFCE7B00)
            "Cibersecurity" -> Color(0xFF673AB7)
            else -> colorScheme.surfaceVariant
        }
        return when (seating.estado) {
            SeatingStatus.OCUPADO -> baseColor
            SeatingStatus.RESERVADO -> colorScheme.primary.copy(alpha = 0.3f)
            SeatingStatus.LIBRE -> colorScheme.surfaceVariant
        }
    }
    fun obtenerAsientoPorMatricula(matricula: String): Seating? {
        return listadoSeatings.find { it.idEstudiante == matricula }
    }
    fun calcularPuntitoMapa(seating: Seating, screenWidth: Float, screenHeight: Float, density: Density): Offset {
        val x = with(density) { (screenWidth * seating.x).toInt().toDp() }
        val y = with(density) { (screenHeight * seating.y).toInt().toDp() }
        return Offset(x.value, y.value)
    }


}
