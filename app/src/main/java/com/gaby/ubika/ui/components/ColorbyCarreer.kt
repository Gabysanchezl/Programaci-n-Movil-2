package com.gaby.ubika.ui.components

import androidx.compose.ui.graphics.Color

fun ColorbyCarreer(carrera: String): Color {
    return when (carrera) {
        "Software" -> Color(0xFF2196F3)
        "Educacion" -> Color(0xFF427900)
        "Administracion" -> Color(0xFFCE7B00)
        "Cibersecurity" -> Color(0xFF673AB7)
        else -> Color.Gray
    }
}
