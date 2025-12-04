package com.gaby.ubika.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.gaby.ubika.ui.screens.AdminHomeScreen
import com.gaby.ubika.ui.screens.MainMenuScreen
import com.gaby.ubika.ui.screens.SplashScreen
import com.gaby.ubika.ui.screens.AdminLoginScreen
import com.gaby.ubika.ui.screens.AdminProfileScreen
import com.gaby.ubika.ui.screens.ConsultingSeat_Screen
import com.gaby.ubika.ui.screens.StudentListScreen
import com.gaby.ubika.ui.screens.SeatingMapScreen
import com.gaby.ubika.ui.screens.ConsultingResult_Screen
import com.gaby.ubika.viewmodels.SplashViewModel


@Composable
fun AppNavigation(navController: NavHostController ) {

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") { val splashViewModel: SplashViewModel = viewModel()
            SplashScreen(navController, splashViewModel) }
        composable("main_menu") { MainMenuScreen(navController ) }
        composable("admin_login") { AdminLoginScreen(navController ) }
        composable("consulta_seccion") { ConsultingSeat_Screen(navController  ) }
        composable("admin_home") { AdminHomeScreen(navController ) }

        // Navegación del menú inferior
        composable("plano_asientos") { SeatingMapScreen(navController) }
        composable("lista_estudiantes") { StudentListScreen(navController) }

         composable(
            route = "resultado_consulta/{nombre}/{codigo}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType },
                navArgument("codigo") { type = NavType.StringType }
            )
        ) { backStackEntry ->
             val nombre = Uri.decode(backStackEntry.arguments?.getString("nombre") ?: "")
             val codigo = Uri.decode(backStackEntry.arguments?.getString("codigo") ?: "")

            ConsultingResult_Screen(nombre, codigo, navController)
        }
        composable("admin_perfil") { AdminProfileScreen(navController) }

    }
}
