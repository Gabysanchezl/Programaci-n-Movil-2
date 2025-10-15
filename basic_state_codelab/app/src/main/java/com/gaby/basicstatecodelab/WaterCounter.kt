package com.gaby.basicstatecodelab
import WellnessTaskItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
 import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WaterCounter(modifier: Modifier = Modifier) {

        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var count by rememberSaveable  { mutableStateOf(0) }  //rememberSaveable permite que no se olvide el estado si se rota la pantalla
            if (count > 0) {
                Text(
                    text = "Te has tomado $count vasos de agua.")
            }
                Button(onClick = { count++ }, Modifier.padding(top = 8.dp), enabled = count < 10) {
                    Text("Agregar vaso")
                }

            }
}

@Composable
fun StatelessCounter(count: Int, onIncrement: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        if (count > 0) {
            Text("Te has tomado $count vasos de agua.")
        }
        Button(onClick = onIncrement, Modifier.padding(top = 8.dp), enabled = count < 10) {
            Text("Agregar vaso")
        }
    }
}

@Composable  //StatefulCounter es propietario del estado
fun StatefulCounter(modifier: Modifier = Modifier) {  //contiene el estado count y lo modifica cuando se llama a la función
    var count by rememberSaveable { mutableIntStateOf(0) }
    StatelessCounter(count, { count++ }, modifier)
}

//
//@Composable
//fun WaterCounter(modifier: Modifier = Modifier) {
//
//    Column(
//        modifier = modifier.padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        var count by remember {mutableStateOf(0) }
//        if (count > 0) {
//            var showTask by remember { mutableStateOf (true) } // como count es 0, se define una variable "showTask"
//            if (showTask) {  //si showTask existe entoces se muestra un task de WellnessTaskItem ... ese mimito "taskName"
//                WellnessTaskItem( //eso permite que exista una task si ya bebiste agua.
//                    onClose = { showTask = false },  //close button
//                    taskName = "Ya hiciste tu caminata de 15 minutos hoy?"
//                )
//                Text( text = "Te has tomado $count vasos de agua.")
//            }
//        }
//        Row(Modifier.padding(top = 8.dp)) {
//            Button(onClick = { count++ }, Modifier.padding(top = 0.dp), enabled = count < 10) {
//                Text("Agregar un vaso")
//            }
//            Button(
//                onClick = { count = 0 },
//                Modifier.padding(start = 8.dp)
//            ) {
//                Text("Eliminar conteo")
//            }
//        }
//    }
//}
//

//@Composable
//fun WaterCounter(modifier: Modifier = Modifier) {
//
//        Column(
//            modifier = modifier.padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            val count : MutableState<Int> = remember {mutableStateOf(value = 0) }
//            Text(
//                text = "Te has tomado $count vasos de agua."
//            )
//            Button(onClick = { count.value++ }, Modifier.padding(top = 8.dp)) {
//                Text("Agregar un vaso")
//            }
//
//
//        }
//}