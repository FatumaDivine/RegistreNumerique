package com.example.registrenumerique

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.registrenumerique.ui.screens.AddEditSellerScreen
import com.example.registrenumerique.ui.screens.SellerListScreen
import com.example.registrenumerique.ui.theme.RegistreNumeriqueTheme
import com.example.registrenumerique.ui.viewmodel.SellerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RegistreNumeriqueTheme {
                val navController = rememberNavController()
                val viewModel: SellerViewModel = viewModel()

                NavHost(navController = navController, startDestination = "list") {
                    composable("list") {
                        SellerListScreen(
                            viewModel = viewModel,
                            onAddSeller = { navController.navigate("add_edit") },
                            onEditSeller = { seller -> 
                                navController.navigate("add_edit?sellerId=${seller.id}")
                            }
                        )
                    }
                    composable(
                        route = "add_edit?sellerId={sellerId}",
                        arguments = listOf(
                            navArgument("sellerId") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            }
                        )
                    ) { backStackEntry ->
                        val sellerId = backStackEntry.arguments?.getString("sellerId")?.toIntOrNull()
                        AddEditSellerScreen(
                            viewModel = viewModel,
                            sellerId = sellerId,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}