package com.gaby.ubika.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gaby.ubika.domain.model.Student


@Composable
fun OcupationBar_Carreers(
    asignadosPorCarrera: Map<String, Int>,
    listadoStudents: List<Student>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        asignadosPorCarrera.forEach { (carrera, asignadosCarrera) ->
            // Total de estudiantes de esa carrera
            val totalEstudiantesCarrera = listadoStudents
                .filter { it.carrera.isNotBlank() }
                .count { it.carrera == carrera }

            // Progreso: asignados / total
            val progreso = if (totalEstudiantesCarrera > 0) {
                asignadosCarrera.toFloat() / totalEstudiantesCarrera.toFloat()
            } else 0f
            val porcentaje = (progreso * 100).toInt()
            val colorCarrera = ColorbyCarreer(carrera)
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = carrera,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorCarrera
                    )
                    Text(
                        text = "$porcentaje%",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = colorCarrera
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(colorCarrera.copy(alpha = 0.4f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progreso.coerceIn(0f, 1f)) // asegura entre 0 y 1
                            .background(colorCarrera)
                    )
                }
            }
        }
    }
}