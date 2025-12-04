package com.gaby.ubika.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Composable
fun CardCarreersSeating(
    carrera: String, asignados: Int, faltanPorAsignar: Int, colorCarrera: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Círculo outlined del color de la carrera
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .border(2.dp, colorCarrera, CircleShape)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = carrera,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = colorCarrera,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Asientos que faltan por asignar: $faltanPorAsignar",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.6f
                        )
                    )
                )
            }

            Text(
                text = asignados.toString(),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = colorCarrera,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}
