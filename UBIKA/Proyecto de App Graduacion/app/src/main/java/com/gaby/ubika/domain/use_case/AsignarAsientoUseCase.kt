package com.gaby.ubika.domain.use_case

import com.gaby.ubika.domain.model.Seating
import com.gaby.ubika.domain.model.Student

class AsignarAsientoUseCase {
    operator fun invoke(student: Student, seating: Seating): Seating {
        return seating.copy(
            idEstudiante = student.id,
            carrera = student.carrera
        )
    }
}