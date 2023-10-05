package com.pkm.PrototypePKM.ui.navGraph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.pkm.PrototypePKM.ui.screen.HomeScreen

@Composable
fun RootNavGraph(navController: NavHostController) {

    NavHost(navController = navController, route = "root_graph", startDestination = "home_graph"){
        composable(route = "home_graph"){
            HomeScreen()
        }

    }
}


