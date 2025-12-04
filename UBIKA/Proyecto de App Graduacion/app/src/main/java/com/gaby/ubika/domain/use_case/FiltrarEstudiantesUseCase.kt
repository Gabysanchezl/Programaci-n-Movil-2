package com.gaby.ubika.domain.use_case

import com.gaby.ubika.domain.model.Student

class FiltrarEstudiantesUseCase {
    operator fun invoke(students: List<Student>, carrera: String, query: String): List<Student> {
        return students.filter {
            (carrera == "Todas" || it.carrera == carrera) &&
                    (it.nombre.contains(query, ignoreCase = true) || it.id.contains(
                        query,
                        ignoreCase = true))
        }
    }
}
