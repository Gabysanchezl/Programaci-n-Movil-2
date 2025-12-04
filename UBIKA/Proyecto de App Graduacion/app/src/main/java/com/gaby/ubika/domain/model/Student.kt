package com.gaby.ubika.domain.model

data class Student(
    val id: String = "",
    val nombre: String = "",
    val merito: String = "",
    val carrera: String = "",
    val asientoAsignado: String? = null
)
