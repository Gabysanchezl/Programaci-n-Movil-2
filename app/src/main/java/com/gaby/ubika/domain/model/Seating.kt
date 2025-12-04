package com.gaby.ubika.domain.model

data class Seating(
    val codigo: String = "",
    val idEstudiante: String? = null,
    val carrera: String? = null,
    val x: Float = 0f,
    val y: Float = 0f,
){
    val estado: SeatingStatus
        get() = when {
            idEstudiante != null -> SeatingStatus.OCUPADO
            carrera != null -> SeatingStatus.RESERVADO
            else -> SeatingStatus.LIBRE
        }
}